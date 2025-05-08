package com.your.package.name.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.your.package.name.core.SubscriptionManager
import com.your.package.name.core.V2RayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var btnToggle: Button
    private var isConnected = false
    private val configUrl = "https://raw.githubusercontent.com/4win-official/VPN/refs/heads/main/config.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnToggle = findViewById(R.id.btnToggleVpn)
        btnToggle.setOnClickListener { onToggleClicked(it) }
    }

    fun onToggleClicked(view: View) {
        if (!isConnected) startVpn() else stopVpn()
    }

    private fun startVpn() {
        btnToggle.text = "در حال اتصال..."
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. دریافت JSON کانفیگ
                val jsonText = SubscriptionManager.fetchConfig(configUrl)
                // 2. تبدیل JSON به لیست سرورها
                val servers = SubscriptionManager.parseServers(jsonText)
                // 3. انتخاب سریع‌ترین سرور
                val best = SubscriptionManager.selectFastest(servers)
                // 4. اتصال
                V2RayService.connect(best)

                withContext(Dispatchers.Main) {
                    isConnected = true
                    btnToggle.text = "قطع اتصال"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    btnToggle.text = "خطا"
                }
            }
        }
    }

    private fun stopVpn() {
        V2RayService.disconnect()
        isConnected = false
        btnToggle.text = "اتصال"
    }
}
