package com.fourwin.vpn.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.AssetManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fourwin.vpn.AngApplication
import com.fourwin.vpn.AppConfig
import com.fourwin.vpn.R
import com.fourwin.vpn.dto.ProfileItem
import com.fourwin.vpn.dto.ServersCache
import com.fourwin.vpn.extension.serializable
import com.fourwin.vpn.extension.toastError
import com.fourwin.vpn.extension.toastSuccess
import com.fourwin.vpn.handler.AngConfigManager
import com.fourwin.vpn.handler.MmkvManager
import com.fourwin.vpn.handler.SettingsManager
import com.fourwin.vpn.handler.SpeedtestManager
import com.fourwin.vpn.util.MessageUtil
import com.fourwin.vpn.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.Collections

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var serverList = MmkvManager.decodeServerList()
    var subscriptionId: String =
        MmkvManager.decodeSettingsString(AppConfig.CACHE_SUBSCRIPTION_ID, "").orEmpty()

    var keywordFilter = ""
    val serversCache = mutableListOf<ServersCache>()
    val isRunning = MutableLiveData<Boolean>()
    val updateListAction = MutableLiveData<Int>()
    val updateTestResultAction = MutableLiveData<String>()
    private val tcpingTestScope = CoroutineScope(Dispatchers.IO)

    fun startListenBroadcast() {
        isRunning.value = false
        val filter = IntentFilter(AppConfig.BROADCAST_ACTION_ACTIVITY)
        ContextCompat.registerReceiver(
            getApplication(),
            mMsgReceiver,
            filter,
            Utils.receiverFlags()
        )
        MessageUtil.sendMsg2Service(getApplication(), AppConfig.MSG_REGISTER_CLIENT, "")
    }

    override fun onCleared() {
        getApplication<Application>().unregisterReceiver(mMsgReceiver)
        tcpingTestScope.coroutineContext[Job]?.cancelChildren()
        SpeedtestManager.closeAllTcpSockets()
        Log.i(AppConfig.TAG, "MainViewModel cleared")
        super.onCleared()
    }

    fun reloadServerList() {
        serverList = MmkvManager.decodeServerList()
        updateCache()
        updateListAction.value = -1
    }

    fun removeServer(guid: String) {
        serverList.remove(guid)
        MmkvManager.removeServer(guid)
        val index = getPosition(guid)
        if (index >= 0) serversCache.removeAt(index)
    }

    fun swapServer(fromPosition: Int, toPosition: Int) {
        if (subscriptionId.isEmpty()) {
            Collections.swap(serverList, fromPosition, toPosition)
        } else {
            val from = serverList.indexOf(serversCache[fromPosition].guid)
            val to = serverList.indexOf(serversCache[toPosition].guid)
            Collections.swap(serverList, from, to)
        }
        Collections.swap(serversCache, fromPosition, toPosition)
        MmkvManager.encodeServerList(serverList)
    }

    @Synchronized
    fun updateCache() {
        serversCache.clear()
        for (guid in serverList) {
            val profile = MmkvManager.decodeServerConfig(guid) ?: continue
            if (subscriptionId.isNotEmpty() && subscriptionId != profile.subscriptionId) continue
            if (keywordFilter.isEmpty() ||
                profile.remarks.contains(keywordFilter, ignoreCase = true)
            ) {
                serversCache.add(ServersCache(guid, profile))
            }
        }
    }

    fun updateConfigViaSubAll(): Int {
        return if (subscriptionId.isEmpty()) {
            AngConfigManager.updateConfigViaSubAll()
        } else {
            val subItem = MmkvManager.decodeSubscription(subscriptionId) ?: return 0
            AngConfigManager.updateConfigViaSub(subscriptionId to subItem)
        }
    }

    fun exportAllServer(): Int {
        val list = if (subscriptionId.isEmpty() && keywordFilter.isEmpty()) {
            serverList
        } else {
            serversCache.map { it.guid }
        }
        return AngConfigManager.shareNonCustomConfigsToClipboard(
            getApplication<AngApplication>(),
            list
        )
    }

    fun testAllTcping() {
        tcpingTestScope.coroutineContext[Job]?.cancelChildren()
        SpeedtestManager.closeAllTcpSockets()
        MmkvManager.clearAllTestDelayResults(serversCache.map { it.guid })

        serversCache.toList().forEach { item ->
            item.profile.server?.let { host ->
                item.profile.serverPort?.toIntOrNull()?.let { port ->
                    tcpingTestScope.launch {
                        val delay = SpeedtestManager.tcping(host, port)
                        launch(Dispatchers.Main) {
                            MmkvManager.encodeServerTestDelayMillis(item.guid, delay)
                            updateListAction.value = getPosition(item.guid)
                        }
                    }
                }
            }
        }
    }

    fun testAllRealPing() {
        MessageUtil.sendMsg2TestService(getApplication(), AppConfig.MSG_MEASURE_CONFIG_CANCEL, "")
        MmkvManager.clearAllTestDelayResults(serversCache.map { it.guid })
        updateListAction.value = -1

        viewModelScope.launch(Dispatchers.Default) {
            serversCache.toList().forEach {
                MessageUtil.sendMsg2TestService(
                    getApplication(),
                    AppConfig.MSG_MEASURE_CONFIG,
                    it.guid
                )
            }
        }
    }

    fun subscriptionIdChanged(id: String) {
        if (subscriptionId != id) {
            subscriptionId = id
            MmkvManager.encodeSettings(AppConfig.CACHE_SUBSCRIPTION_ID, subscriptionId)
            reloadServerList()
        }
    }

    fun getSubscriptions(ctx: Context): Pair<MutableList<String>, MutableList<String>>? {
        val subs = MmkvManager.decodeSubscriptions()
        if (subscriptionId.isNotEmpty() && subs.none { it.first == subscriptionId }) {
            subscriptionIdChanged("")
        }
        if (subs.isEmpty()) return null
        val ids = subs.map { it.first }.toMutableList().apply { add(0, "") }
        val names = subs.map { it.second.remarks }.toMutableList().apply {
            add(0, ctx.getString(R.string.filter_config_all))
        }
        return ids to names
    }

    fun getPosition(guid: String) = serversCache.indexOfFirst { it.guid == guid }

    fun removeDuplicateServer(): Int {
        val delete = mutableSetOf<String>()
        serversCache.mapNotNull { MmkvManager.decodeServerConfig(it.guid) }
            .forEachIndexed { i, p1 ->
                serversCache.drop(i + 1).forEach { sc2 ->
                    if (p1 == sc2.profile) delete.add(sc2.guid)
                }
            }
        delete.forEach { MmkvManager.removeServer(it) }
        return delete.size
    }

    fun removeAllServer(): Int {
        return if (subscriptionId.isEmpty() && keywordFilter.isEmpty()) {
            MmkvManager.removeAllServer()
        } else {
            serversCache.map { it.guid }.also { list ->
                list.forEach { MmkvManager.removeServer(it) }
            }.size
        }
    }

    fun removeInvalidServer(): Int {
        var count = 0
        serversCache.map { it.guid }.forEach {
            count += MmkvManager.removeInvalidServer(it)
        }
        return count
    }

    fun sortByTestResults() {
        data class ServerDelay(val guid: String, val delay: Long)
        val delays = MmkvManager.decodeServerList().map { key ->
            ServerDelay(key, MmkvManager.decodeServerAffiliationInfo(key)?.testDelayMillis ?: Long.MAX_VALUE)
        }.sortedBy { it.delay }
        val newList = delays.map { it.guid }
        MmkvManager.encodeServerList(newList)
    }

    fun initAssets(assets: AssetManager) {
        viewModelScope.launch(Dispatchers.Default) {
            SettingsManager.initAssets(getApplication<AngApplication>(), assets)
        }
    }

    fun filterConfig(keyword: String) {
        if (keyword != keywordFilter) {
            keywordFilter = keyword
            MmkvManager.encodeSettings(AppConfig.CACHE_KEYWORD_FILTER, keyword)
            reloadServerList()
        }
    }

    private val mMsgReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            when (intent?.getIntExtra("key", 0)) {
                AppConfig.MSG_STATE_RUNNING -> isRunning.value = true
                AppConfig.MSG_STATE_NOT_RUNNING -> isRunning.value = false

                AppConfig.MSG_STATE_START_SUCCESS -> {
                    getApplication<Application>().toastSuccess(R.string.toast_services_success)
                    isRunning.value = true
                }
                AppConfig.MSG_STATE_START_FAILURE -> {
                    getApplication<Application>().toastError(R.string.toast_services_failure)
                    isRunning.value = false
                }
                AppConfig.MSG_MEASURE_DELAY_SUCCESS ->
                    updateTestResultAction.value = intent.getStringExtra("content")

                AppConfig.MSG_MEASURE_CONFIG_SUCCESS -> {
                    val (guid, delay) = intent.serializable<Pair<String, Long>>("content") ?: return
                    MmkvManager.encodeServerTestDelayMillis(guid, delay)
                    updateListAction.value = getPosition(guid)
                }
            }
        }
    }
}
