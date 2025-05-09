package com.v2ray.ang.handler

import com.tencent.mmkv.MMKV
import com.v2ray.ang.AppConfig
import com.v2ray.ang.dto.ProfileItem
import com.v2ray.ang.dto.RulesetItem
import com.v2ray.ang.dto.ServerAffiliationInfo
import com.v2ray.ang.util.JsonUtil
import com.v2ray.ang.util.Utils

object MmkvManager {
    private const val ID_MAIN = "MAIN"
    private const val ID_PROFILE = "PROFILE_FULL_CONFIG"

    private const val KEY_SELECTED = "SELECTED_SERVER"
    private const val KEY_SERVERS = "SERVERS_LIST"

    private val mainKV by lazy { MMKV.mmkvWithID(ID_MAIN, MMKV.MULTI_PROCESS_MODE) }
    private val profileKV by lazy { MMKV.mmkvWithID(ID_PROFILE, MMKV.MULTI_PROCESS_MODE) }

    //—— سرورها ——————————————————————————————
    fun getSelectedServer(): String? = mainKV.decodeString(KEY_SELECTED)
    fun setSelectedServer(id: String) = mainKV.encode(KEY_SELECTED, id)

    fun saveServerList(list: List<String>) {
        mainKV.encode(KEY_SERVERS, JsonUtil.toJson(list))
    }
    fun loadServerList(): MutableList<String> {
        val json = mainKV.decodeString(KEY_SERVERS) ?: return mutableListOf()
        return JsonUtil.fromJson(json, Array<String>::class.java).toMutableList()
    }

    fun loadProfile(id: String): ProfileItem? {
        val json = profileKV.decodeString(id) ?: return null
        return JsonUtil.fromJson(json, ProfileItem::class.java)
    }
    fun saveProfile(id: String, profile: ProfileItem): String {
        val key = id.ifBlank { Utils.getUuid() }
        profileKV.encode(key, JsonUtil.toJson(profile))
        val list = loadServerList().apply { if (!contains(key)) add(0, key) }
        saveServerList(list)
        if (getSelectedServer().isNullOrBlank()) setSelectedServer(key)
        return key
    }
    fun removeProfile(id: String) {
        if (getSelectedServer() == id) mainKV.remove(KEY_SELECTED)
        profileKV.remove(id)
        saveServerList(loadServerList().apply { remove(id) })
    }

    //—— تنظیمات عمومی ——————————————————————————
    fun encodeString(key: String, value: String) = mainKV.encode(key, value)
    fun encodeBoolean(key: String, value: Boolean) = mainKV.encode(key, value)
    fun encodeInt(key: String, value: Int) = mainKV.encode(key, value)

    fun decodeString(key: String, default: String = ""): String =
        mainKV.decodeString(key, default) ?: default
    fun decodeBoolean(key: String, default: Boolean = false): Boolean =
        mainKV.decodeBool(key, default)
    fun decodeInt(key: String, default: Int = 0): Int =
        mainKV.decodeInt(key, default)

    //—— تست تأخیر TCP ——————————————————————————
    fun decodeAffiliation(id: String): ServerAffiliationInfo? {
        val json = mainKV.decodeString(id) ?: return null
        return JsonUtil.fromJson(json, ServerAffiliationInfo::class.java)
    }
    fun encodeTestDelay(id: String, delay: Long) {
        val aff = decodeAffiliation(id) ?: ServerAffiliationInfo()
        aff.testDelayMillis = delay
        mainKV.encode(id, JsonUtil.toJson(aff))
    }
    fun clearAllDelays(keys: List<String>?) {
        keys?.forEach {
            val aff = decodeAffiliation(it) ?: return@forEach
            aff.testDelayMillis = 0
            mainKV.encode(it, JsonUtil.toJson(aff))
        }
    }

    //—— بوت و مسیریابی ——————————————————————————
    fun saveBoot(started: Boolean) =
        encodeBoolean(AppConfig.Pref.IS_BOOTED, started)
    fun loadBoot(): Boolean =
        decodeBoolean(AppConfig.Pref.IS_BOOTED, false)

    fun saveRulesets(list: List<RulesetItem>?) =
        encodeString(AppConfig.Pref.ROUTING_RULESET, list?.let { JsonUtil.toJson(it) } ?: "")
    fun loadRulesets(): MutableList<RulesetItem>? {
        val json = decodeString(AppConfig.Pref.ROUTING_RULESET)
        if (json.isEmpty()) return null
        return JsonUtil.fromJson(json, Array<RulesetItem>::class.java).toMutableList()
    }
}
