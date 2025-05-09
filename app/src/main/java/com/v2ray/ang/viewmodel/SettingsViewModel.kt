package com.v2ray.ang.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.MmkvManager
import com.v2ray.ang.handler.SettingsManager

class SettingsViewModel(application: Application) : AndroidViewModel(application),
    SharedPreferences.OnSharedPreferenceChangeListener {

    fun startListenPreferenceChange() {
        PreferenceManager.getDefaultSharedPreferences(getApplication())
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCleared() {
        PreferenceManager.getDefaultSharedPreferences(getApplication())
            .unregisterOnSharedPreferenceChangeListener(this)
        Log.i(AppConfig.TAG, "Settings ViewModel is cleared")
        super.onCleared()
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String?) {
        Log.i(AppConfig.TAG, "Observe settings changed: $key")
        when (key) {
            // رشته‌ای
            AppConfig.Pref.MODE,
            AppConfig.Pref.VPN_DNS,
            AppConfig.Pref.VPN_BYPASS_LAN,
            AppConfig.Pref.REMOTE_DNS,
            AppConfig.Pref.DOMESTIC_DNS,
            AppConfig.Pref.DNS_HOSTS,
            AppConfig.Pref.DELAY_TEST_URL,
            AppConfig.Pref.LOCAL_DNS_PORT,
            AppConfig.Pref.SOCKS_PORT,
            AppConfig.Pref.LOG_LEVEL,
            AppConfig.Pref.LANGUAGE,
            AppConfig.Pref.ROUTING_DOMAIN_STRATEGY,
            AppConfig.Pref.UPDATE_INTERVAL,
            AppConfig.Pref.FRAGMENT_PACKETS,
            AppConfig.Pref.FRAGMENT_LENGTH,
            AppConfig.Pref.FRAGMENT_INTERVAL,
            AppConfig.Pref.MUX_XUDP_QUIC
            -> {
                MmkvManager.encodeSettingsString(key!!, sp.getString(key, "").orEmpty())
            }

            // بولی
            AppConfig.Pref.ROUTE_ONLY_ENABLED,
            AppConfig.Pref.IS_BOOTED,
            AppConfig.Pref.SPEED_ENABLED,
            AppConfig.Pref.PROXY_SHARING,
            AppConfig.Pref.LOCAL_DNS_ENABLED,
            AppConfig.Pref.FAKE_DNS_ENABLED,
            AppConfig.Pref.APPEND_HTTP_PROXY,
            AppConfig.Pref.ALLOW_INSECURE,
            AppConfig.Pref.PREFER_IPV6,
            AppConfig.Pref.PER_APP_PROXY,
            AppConfig.Pref.BYPASS_APPS,
            AppConfig.Pref.CONFIRM_REMOVE,
            AppConfig.Pref.START_SCAN_IMMEDIATE,
            AppConfig.Pref.DOUBLE_COLUMN_DISPLAY,
            AppConfig.Pref.AUTO_UPDATE_SUBSCRIPTION,
            AppConfig.Pref.FRAGMENT_ENABLED,
            AppConfig.Pref.MUX_ENABLED,
            AppConfig.Pref.SNIFFING_ENABLED
            -> {
                MmkvManager.encodeSettingsBool(key!!, sp.getBoolean(key, false))
            }

            // مقادیر عددی
            AppConfig.Pref.MUX_CONCURRENCY,
            AppConfig.Pref.MUX_XUDP_CONCURRENCY
            -> {
                MmkvManager.encodeSettingsString(key!!, sp.getString(key, "8").orEmpty())
            }
        }

        if (key == AppConfig.Pref.UI_MODE_NIGHT) {
            // برای تغییر تم
            MmkvManager.encodeSettingsBool(key, sp.getBoolean(key, false))
            SettingsManager.setNightMode()
        }
    }
}
