package com.v2ray.ang.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.v2ray.ang.AppConfig
import com.v2ray.ang.util.MmkvManager

class SettingsViewModel : ViewModel() {

    fun onPreferenceChanged(sp: SharedPreferences, key: String) {
        when (key) {
            AppConfig.Pref.ENABLE_PROXY -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.ALLOW_INSECURE -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.SOCKS_PORT -> {
                val v = sp.getString(key, "10808")
                MmkvManager.encodeSettingsStr(key, v)
            }

            AppConfig.Pref.HTTP_PORT -> {
                val v = sp.getString(key, "10809")
                MmkvManager.encodeSettingsStr(key, v)
            }

            AppConfig.Pref.ENABLE_NOTIFY -> {
                val v = sp.getBoolean(key, true)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.SPEED_ENABLED -> {
                val v = sp.getBoolean(key, true)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.TETHERING_SUPPORT -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.ENABLE_DIRECT_BOOT -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.UDP_FORWARDING -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.ALLOW_OTHER_APP -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.RESTORE_ON_BOOT -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.BOOT_DELAY -> {
                val v = sp.getString(key, "0")
                MmkvManager.encodeSettingsStr(key, v)
            }

            AppConfig.Pref.PER_APP_PROXY -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.REPLACE_START -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.TUN_MODE -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.DNS_FORWARD -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.REPLACE_SYSTEM_DNS -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }

            AppConfig.Pref.REPLACE_ROUTING -> {
                val v = sp.getBoolean(key, false)
                MmkvManager.encodeSettingsBool(key, v)
            }
        }
    }
}
