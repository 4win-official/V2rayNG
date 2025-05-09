package com.v2ray.ang.handler

import com.tencent.mmkv.MMKV
import com.v2ray.ang.AppConfig
import com.v2ray.ang.dto.AssetUrlItem
import com.v2ray.ang.dto.ProfileItem
import com.v2ray.ang.dto.RulesetItem
import com.v2ray.ang.dto.ServerAffiliationInfo
import com.v2ray.ang.dto.SubscriptionItem
import com.v2ray.ang.util.JsonUtil
import com.v2ray.ang.util.Utils

object MmkvManager {

    // شناسه‌ی جعبه‌های MMKV
    private const val ID_MAIN = "MAIN"
    private const val ID_PROFILE_FULL_CONFIG = "PROFILE_FULL_CONFIG"
    private const val ID_SERVER_AFF = "SERVER_AFF"

    // کلیدهای اصلی
    private const val KEY_SELECTED_SERVER = "SELECTED_SERVER"
    private const val KEY_ANG_CONFIGS = "ANG_CONFIGS"

    // دسترسی به جعبه‌ها
    private val mainStorage by lazy { MMKV.mmkvWithID(ID_MAIN, MMKV.MULTI_PROCESS_MODE) }
    private val profileFullStorage by lazy { MMKV.mmkvWithID(ID_PROFILE_FULL_CONFIG, MMKV.MULTI_PROCESS_MODE) }
    private val serverAffStorage by lazy { MMKV.mmkvWithID(ID_SERVER_AFF, MMKV.MULTI_PROCESS_MODE) }

    /** دریافت سرور انتخاب‌شده */
    fun getSelectServer(): String? = mainStorage.decodeString(KEY_SELECTED_SERVER)

    /** تنظیم سرور انتخاب‌شده */
    fun setSelectServer(guid: String) {
        mainStorage.encode(KEY_SELECTED_SERVER, guid)
    }

    /** ذخیرهٔ لیست GUID سرورها */
    fun encodeServerList(serverList: MutableList<String>) {
        mainStorage.encode(KEY_ANG_CONFIGS, JsonUtil.toJson(serverList))
    }

    /** بازیابی لیست GUID سرورها */
    fun decodeServerList(): MutableList<String> {
        val json = mainStorage.decodeString(KEY_ANG_CONFIGS)
        return if (json.isNullOrBlank()) mutableListOf()
        else JsonUtil.fromJson(json, Array<String>::class.java).toMutableList()
    }

    /** بازیابی تنظیمات کامل یک سرور */
    fun decodeServerConfig(guid: String): ProfileItem? {
        val json = profileFullStorage.decodeString(guid)
        return if (json.isNullOrBlank()) null else JsonUtil.fromJson(json, ProfileItem::class.java)
    }

    /** ذخیرهٔ تنظیمات کامل یک سرور */
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

    /** حذف یک سرور */
    fun removeServer(guid: String) {
        if (getSelectServer() == guid) {
            mainStorage.remove(KEY_SELECTED_SERVER)
        }
        val serverList = decodeServerList().apply { remove(guid) }
        encodeServerList(serverList)
        profileFullStorage.remove(guid)
        serverAffStorage.remove(guid)
    }

    /** اطلاعات تأخیر تست TCP */
    fun decodeServerAffiliationInfo(guid: String): ServerAffiliationInfo? {
        val json = serverAffStorage.decodeString(guid)
        return if (json.isNullOrBlank()) null else JsonUtil.fromJson(json, ServerAffiliationInfo::class.java)
    }

    /** ثبت نتیجهٔ تست TCP ping */
    fun encodeServerTestDelayMillis(guid: String, testResult: Long) {
        val aff = decodeServerAffiliationInfo(guid) ?: ServerAffiliationInfo()
        aff.testDelayMillis = testResult
        serverAffStorage.encode(guid, JsonUtil.toJson(aff))
    }

    /** پاک‌کردن همهٔ نتایج تست */
    fun clearAllTestDelayResults(keys: List<String>?) {
        keys?.forEach { key ->
            decodeServerAffiliationInfo(key)?.let {
                it.testDelayMillis = 0
                serverAffStorage.encode(key, JsonUtil.toJson(it))
            }
        }
    }

    /** حذف همهٔ سرورها */
    fun removeAllServer(): Int {
        val count = profileFullStorage.allKeys()?.size ?: 0
        mainStorage.clearAll()
        profileFullStorage.clearAll()
        serverAffStorage.clearAll()
        return count
    }

    /** حذف سرورهای نامعتبر */
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

    //––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––

    /** متدهای جدید برای ذخیره‌سازی تنظیمات از SettingsViewModel */

    /** ذخیرهٔ مقدار رشته‌ای */
    fun encodeSettingsString(key: String, value: String) {
        mainStorage.encode(key, value)
    }

    /** ذخیرهٔ مقدار بولین */
    fun encodeSettingsBool(key: String, value: Boolean) {
        mainStorage.encode(key, value)
    }

    /** ذخیرهٔ مقدار عدد صحیح */
    fun encodeSettingsInt(key: String, value: Int) {
        mainStorage.encode(key, value)
    }

    //––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––

    /** استارت روی بوت */
    fun encodeStartOnBoot(startOnBoot: Boolean) {
        encodeSettingsBool(AppConfig.Pref.IS_BOOTED, startOnBoot)
    }
    fun decodeStartOnBoot(): Boolean = mainStorage.decodeBool(AppConfig.Pref.IS_BOOTED, false)

    /** قوانین مسیریابی */
    fun decodeRoutingRulesets(): MutableList<RulesetItem>? {
        val json = mainStorage.decodeString(AppConfig.Pref.ROUTING_RULESET)
        return if (json.isNullOrEmpty()) null else JsonUtil.fromJson(json, Array<RulesetItem>::class.java).toMutableList()
    }
    fun encodeRoutingRulesets(list: MutableList<RulesetItem>?) {
        if (list.isNullOrEmpty()) encodeSettingsString(AppConfig.Pref.ROUTING_RULESET, "")
        else encodeSettingsString(AppConfig.Pref.ROUTING_RULESET, JsonUtil.toJson(list))
    }

}
