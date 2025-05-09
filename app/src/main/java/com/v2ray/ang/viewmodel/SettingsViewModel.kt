package com.v2ray.ang.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.v2ray.ang.AppConfig
import com.v2ray.ang.AppConfig.Pref.*              // import همه‌ی Pref.*
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == null) return
        Log.i(AppConfig.TAG, "Observe settings changed: $key")

        when (key) {
            // رشته‌ای
            MODE,
            VPN_DNS,
            VPN_BYPASS_LAN,
            REMOTE_DNS,
            DOMESTIC_DNS,
            DNS_HOSTS,
            DELAY_TEST_URL,
            LOCAL_DNS_PORT,
            SOCKS_PORT,
            LOG_LEVEL,
            LANGUAGE,
            UI_MODE_NIGHT,
            ROUTING_DOMAIN_STRATEGY,
            UPDATE_INTERVAL,
            FRAGMENT_PACKETS,
            FRAGMENT_LENGTH,
            FRAGMENT_INTERVAL,
            MUX_XUDP_QUIC -> {
                MmkvManager.encodeSettings(key, sharedPreferences.getString(key, "")!!)
            }

            // بولی
            ROUTE_ONLY_ENABLED,
            IS_BOOTED,
            SPEED_ENABLED,
            PROXY_SHARING,
            LOCAL_DNS_ENABLED,
            FAKE_DNS_ENABLED,
            APPEND_HTTP_PROXY,
            ALLOW_INSECURE,
            PREFER_IPV6,
            PER_APP_PROXY,
            BYPASS_APPS,
            CONFIRM_REMOVE,
            START_SCAN_IMMEDIATE,
            DOUBLE_COLUMN_DISPLAY,
            AUTO_UPDATE_SUBSCRIPTION,
            FRAGMENT_ENABLED,
            MUX_ENABLED -> {
                MmkvManager.encodeSettings(key, sharedPreferences.getBoolean(key, false))
            }

            SNIFFING_ENABLED -> {
                MmkvManager.encodeSettings(key, sharedPreferences.getBoolean(key, true))
            }

            // دوباره رشته‌ای برای این دو
            MUX_CONCURRENCY,
            MUX_XUDP_CONCURRENCY -> {
                MmkvManager.encodeSettings(key, sharedPreferences.getString(key, "")!!)
            }
        }

        if (key == UI_MODE_NIGHT) {
            SettingsManager.setNightMode()
        }
    }
}
