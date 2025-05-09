package com.v2ray.ang.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.v2ray.ang.AppConfig.Pref.*           // import همه کلیدهای Pref
import com.v2ray.ang.AppConfig              // برای TAG
import com.v2ray.ang.handler.MmkvManager    // برای encodeSettings
import com.v2ray.ang.handler.SettingsManager

class SettingsViewModel(application: Application) : AndroidViewModel(application),
    SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Starts listening for preference changes.
     */
    fun startListenPreferenceChange() {
        PreferenceManager.getDefaultSharedPreferences(getApplication())
            .registerOnSharedPreferenceChangeListener(this)
    }

    /**
     * Called when the ViewModel is cleared.
     */
    override fun onCleared() {
        PreferenceManager.getDefaultSharedPreferences(getApplication())
            .unregisterOnSharedPreferenceChangeListener(this)
        Log.i(AppConfig.TAG, "Settings ViewModel is cleared")
        super.onCleared()
    }

    /**
     * Called when a shared preference is changed.
     * @param sharedPreferences The shared preferences.
     * @param key The key of the changed preference.
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        Log.i(AppConfig.TAG, "Observe settings changed: $key")
        when (key) {
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
            MUX_XUDP_QUIC,
                -> {
                MmkvManager.encodeSettings(key!!, sharedPreferences.getString(key, "")!!)
            }

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
            MUX_ENABLED,
                -> {
                MmkvManager.encodeSettings(key!!, sharedPreferences.getBoolean(key, false))
            }

            SNIFFING_ENABLED -> {
                MmkvManager.encodeSettings(key, sharedPreferences.getBoolean(key, true))
            }

            MUX_CONCURRENCY,
            MUX_XUDP_CONCURRENCY -> {
                MmkvManager.encodeSettings(key!!, sharedPreferences.getString(key, "")!!)
            }
        }
        if (key == UI_MODE_NIGHT) {
            SettingsManager.setNightMode()
        }
    }
}
