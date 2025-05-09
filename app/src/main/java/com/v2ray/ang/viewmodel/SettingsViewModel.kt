package com.v2ray.ang.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.v2ray.ang.AppConfig

class SettingsViewModel : ViewModel() {

    fun onPreferenceChanged(sp: SharedPreferences, key: String) {
        // اگر نیاز به واکنش خاصی به تغییرات تنظیمات دارید، اینجا اضافه کنید.
        // در غیر این صورت، این متد می‌تونه خالی بمونه یا حذف بشه.
        when (key) {
            AppConfig.Pref.SOCKS_PORT -> {
                val v = sp.getString(key, "10808")
                // استفاده یا لاگ در صورت نیاز
            }
            AppConfig.Pref.HTTP_PORT -> {
                val v = sp.getString(key, "10809")
                // استفاده یا لاگ در صورت نیاز
            }
            AppConfig.Pref.BOOT_DELAY -> {
                val v = sp.getString(key, "0")
                // استفاده یا لاگ در صورت نیاز
            }
            else -> {
                val v = sp.getBoolean(key, false)
                // استفاده یا لاگ در صورت نیاز
            }
        }
    }
}
