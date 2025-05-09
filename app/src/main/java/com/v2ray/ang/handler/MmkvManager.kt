package com.v2ray.ang.handler

import com.tencent.mmkv.MMKV import com.v2ray.ang.AppConfig.Pref.IS_BOOTED import com.v2ray.ang.AppConfig.Pref.ROUTING_RULESET import com.v2ray.ang.AppConfig.Pref.UI_MODE_NIGHT import com.v2ray.ang.dto.ProfileItem import com.v2ray.ang.dto.RulesetItem import com.v2ray.ang.dto.ServerAffiliationInfo import com.v2ray.ang.util.JsonUtil import com.v2ray.ang.util.Utils

/**

A minimal MMKV-backed settings manager for server configs and app preferences. */ object MmkvManager { private const val ID_MAIN = "MAIN" private const val ID_PROFILE = "PROFILE_FULL_CONFIG" private const val ID_AFF = "SERVER_AFF"

private const val KEY_SELECTED = "SELECTED_SERVER" private const val KEY_LIST = "ANG_CONFIGS"

private val mainStorage by lazy { MMKV.mmkvWithID(ID_MAIN, MMKV.MULTI_PROCESS_MODE) } private val profileStorage by lazy { MMKV.mmkvWithID(ID_PROFILE, MMKV.MULTI_PROCESS_MODE) } private val affStorage by lazy { MMKV.mmkvWithID(ID_AFF, MMKV.MULTI_PROCESS_MODE) }

// region Server List & Selection fun getSelectedServer(): String? = mainStorage.decodeString(KEY_SELECTED) fun setSelectedServer(guid: String) = mainStorage.encode(KEY_SELECTED, guid)

fun encodeServerList(list: List<String>) = mainStorage.encode(KEY_LIST, JsonUtil.toJson(list))

fun decodeServerList(): MutableList<String> { val json = mainStorage.decodeString(KEY_LIST) return if (json.isNullOrBlank()) mutableListOf() else JsonUtil.fromJson(json, Array<String>::class.java).toMutableList() } // endregion

// region Server Configs fun decodeServerConfig(guid: String): ProfileItem? { val json = profileStorage.decodeString(guid) return json?.takeIf { it.isNotBlank() }?.let { JsonUtil.fromJson(it, ProfileItem::class.java) } }

fun encodeServerConfig(guid: String, config: ProfileItem): String { val key = guid.ifBlank { Utils.getUuid() } profileStorage.encode(key, JsonUtil.toJson(config)) val list = decodeServerList() if (!list.contains(key)) { list.add(0, key) encodeServerList(list) if (getSelectedServer().isNullOrBlank()) setSelectedServer(key) } return key }

fun removeServer(guid: String) { if (getSelectedServer() == guid) mainStorage.remove(KEY_SELECTED) val list = decodeServerList().apply { remove(guid) } encodeServerList(list) profileStorage.remove(guid) affStorage.remove(guid) } // endregion

// region Affiliation / Delay fun decodeAffiliation(guid: String): ServerAffiliationInfo? { val json = affStorage.decodeString(guid) return json?.takeIf { it.isNotBlank() }?.let { JsonUtil.fromJson(it, ServerAffiliationInfo::class.java) } }

fun encodeDelay(guid: String, delay: Long) { val info = decodeAffiliation(guid) ?: ServerAffiliationInfo() info.testDelayMillis = delay affStorage.encode(guid, JsonUtil.toJson(info)) }

fun clearDelays(keys: List<String>?) = keys?.forEach { decodeAffiliation(it)?.apply { testDelayMillis = 0 } ?.let { affStorage.encode(it.guid, JsonUtil.toJson(it)) } } // endregion

// region Cleanup fun removeAllServers(): Int { val count = profileStorage.allKeys()?.size ?: 0 mainStorage.clearAll() profileStorage.clearAll() affStorage.clearAll() return count }

fun removeInvalid(guid: String = ""): Int { val keys = if (guid.isNotEmpty()) listOf(guid) else affStorage.allKeys()?.toList() ?: emptyList() var removed = 0 keys.forEach { decodeAffiliation(it)?.takeIf { info -> info.testDelayMillis < 0 }?.also { removeServer(it.guid) removed++ } } return removed } // endregion

// region Preferences fun encodeSettings(key: String, value: String?) = mainStorage.encode(key, value) fun encodeSettings(key: String, value: Boolean) = mainStorage.encode(key, value)

fun decodeStartOnBoot(): Boolean = mainStorage.decodeBool(IS_BOOTED, false)

fun encodeStartOnBoot(onBoot: Boolean) = mainStorage.encode(IS_BOOTED, onBoot)

fun decodeUiModeNight(): Boolean = mainStorage.decodeBool(UI_MODE_NIGHT, false)

fun encodeUiModeNight(night: Boolean) = mainStorage.encode(UI_MODE_NIGHT, night)

fun decodeRoutingRulesets(): MutableList<RulesetItem>? { val json = mainStorage.decodeString(ROUTING_RULESET) return json?.takeIf { it.isNotBlank() } ?.let { JsonUtil.fromJson(it, Array<RulesetItem>::class.java).toMutableList() } }

fun encodeRoutingRulesets(list: MutableList<RulesetItem>?) = mainStorage.encode(ROUTING_RULESET, list?.let(JsonUtil::toJson) ?: "") // endregion }


