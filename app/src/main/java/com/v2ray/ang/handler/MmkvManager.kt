package com.v2ray.ang.handler

import com.tencent.mmkv.MMKV
import com.v2ray.ang.AppConfig.PREF_IS_BOOTED
import com.v2ray.ang.AppConfig.PREF_ROUTING_RULESET
import com.v2ray.ang.AppConfig.PREF_UI_MODE_NIGHT // این خط مهمه
import com.v2ray.ang.dto.AssetUrlItem
import com.v2ray.ang.dto.ProfileItem
import com.v2ray.ang.dto.RulesetItem
import com.v2ray.ang.dto.ServerAffiliationInfo
import com.v2ray.ang.dto.SubscriptionItem
import com.v2ray.ang.util.JsonUtil
import com.v2ray.ang.util.Utils

object MmkvManager {

    private const val ID_MAIN = "MAIN"
    private const val ID_PROFILE_FULL_CONFIG = "PROFILE_FULL_CONFIG"
    private const val ID_SERVER_AFF = "SERVER_AFF"

    private const val KEY_SELECTED_SERVER = "SELECTED_SERVER"
    private const val KEY_ANG_CONFIGS = "ANG_CONFIGS"

    private val mainStorage by lazy { MMKV.mmkvWithID(ID_MAIN, MMKV.MULTI_PROCESS_MODE) }
    private val profileFullStorage by lazy { MMKV.mmkvWithID(ID_PROFILE_FULL_CONFIG, MMKV.MULTI_PROCESS_MODE) }
    private val serverAffStorage by lazy { MMKV.mmkvWithID(ID_SERVER_AFF, MMKV.MULTI_PROCESS_MODE) }

    fun getSelectServer(): String? = mainStorage.decodeString(KEY_SELECTED_SERVER)

    fun setSelectServer(guid: String) {
        mainStorage.encode(KEY_SELECTED_SERVER, guid)
    }

    fun encodeServerList(serverList: MutableList<String>) {
        mainStorage.encode(KEY_ANG_CONFIGS, JsonUtil.toJson(serverList))
    }

    fun decodeServerList(): MutableList<String> {
        val json = mainStorage.decodeString(KEY_ANG_CONFIGS)
        return if (json.isNullOrBlank()) mutableListOf() else JsonUtil.fromJson(json, Array<String>::class.java).toMutableList()
    }

    fun decodeServerConfig(guid: String): ProfileItem? {
        val json = profileFullStorage.decodeString(guid)
        return if (json.isNullOrBlank()) null else JsonUtil.fromJson(json, ProfileItem::class.java)
    }

    fun encodeServerConfig(guid: String, config: ProfileItem): String {
        val key = guid.ifBlank { Utils.getUuid() }
        profileFullStorage.encode(key, JsonUtil.toJson(config))
        val serverList = decodeServerList()
        if (!serverList.contains(key)) {
            serverList.add(0, key)
            encodeServerList(serverList)
            if (getSelectServer().isNullOrBlank()) {
                setSelectServer(key)
            }
        }
        return key
    }

    fun removeServer(guid: String) {
        if (getSelectServer() == guid) {
            mainStorage.remove(KEY_SELECTED_SERVER)
        }
        val serverList = decodeServerList()
        serverList.remove(guid)
        encodeServerList(serverList)
        profileFullStorage.remove(guid)
        serverAffStorage.remove(guid)
    }

    fun decodeServerAffiliationInfo(guid: String): ServerAffiliationInfo? {
        val json = serverAffStorage.decodeString(guid)
        return if (json.isNullOrBlank()) null else JsonUtil.fromJson(json, ServerAffiliationInfo::class.java)
    }

    fun encodeServerTestDelayMillis(guid: String, testResult: Long) {
        val aff = decodeServerAffiliationInfo(guid) ?: ServerAffiliationInfo()
        aff.testDelayMillis = testResult
        serverAffStorage.encode(guid, JsonUtil.toJson(aff))
    }

    fun clearAllTestDelayResults(keys: List<String>?) {
        keys?.forEach { key ->
            decodeServerAffiliationInfo(key)?.let {
                it.testDelayMillis = 0
                serverAffStorage.encode(key, JsonUtil.toJson(it))
            }
        }
    }

    fun removeAllServer(): Int {
        val count = profileFullStorage.allKeys()?.count() ?: 0
        mainStorage.clearAll()
        profileFullStorage.clearAll()
        serverAffStorage.clearAll()
        return count
    }

    fun removeInvalidServer(guid: String): Int {
        var count = 0
        val keys = if (guid.isNotEmpty()) listOf(guid) else serverAffStorage.allKeys()?.toList() ?: emptyList()
        keys.forEach { key ->
            decodeServerAffiliationInfo(key)?.let {
                if (it.testDelayMillis < 0L) {
                    removeServer(key)
                    count++
                }
            }
        }
        return count
    }

    fun encodeStartOnBoot(startOnBoot: Boolean) {
        mainStorage.encode(PREF_IS_BOOTED, startOnBoot)
    }

    fun decodeStartOnBoot(): Boolean = mainStorage.decodeBool(PREF_IS_BOOTED, false)

    fun decodeRoutingRulesets(): MutableList<RulesetItem>? {
        val ruleset = mainStorage.decodeString(PREF_ROUTING_RULESET)
        return if (ruleset.isNullOrEmpty()) null else JsonUtil.fromJson(ruleset, Array<RulesetItem>::class.java).toMutableList()
    }

    fun encodeRoutingRulesets(rulesetList: MutableList<RulesetItem>?) {
        if (rulesetList.isNullOrEmpty())
            mainStorage.encode(PREF_ROUTING_RULESET, "")
        else
            mainStorage.encode(PREF_ROUTING_RULESET, JsonUtil.toJson(rulesetList))
    }

    fun encodeUiModeNight(isNight: Boolean) {
        mainStorage.encode(PREF_UI_MODE_NIGHT, isNight)
    }

    fun decodeUiModeNight(): Boolean {
        return mainStorage.decodeBool(PREF_UI_MODE_NIGHT, false)
    }
}
