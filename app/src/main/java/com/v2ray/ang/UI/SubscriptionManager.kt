package com.v2ray.ang.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object SubscriptionManager {
    suspend fun fetchConfig(url: String): String =
        withContext(Dispatchers.IO) {
            URL(url).readText()
        }

    fun parseServers(jsonText: String): List<Server> {
        // فرض: JSON آرایه‌ای از شیء { "address": "...", "port": ..., ... }
        // از کتابخانه Moshi یا Gson استفاده کن:
        return Gson().fromJson(jsonText, Array<Server>::class.java).toList()
    }

    fun selectFastest(servers: List<Server>): Server {
        var best: Server = servers.first()
        var bestPing = Int.MAX_VALUE

        servers.forEach { srv ->
            val ping = pingServer(srv.address)
            if (ping < bestPing) {
                best = srv
                bestPing = ping
            }
        }
        return best
    }

    private fun pingServer(host: String): Int {
        return try {
            val start = System.currentTimeMillis()
            val conn = (URL("http://$host").openConnection() as HttpURLConnection).apply {
                connectTimeout = 3000
                connect()
            }
            conn.disconnect()
            (System.currentTimeMillis() - start).toInt()
        } catch (_: Exception) {
            Int.MAX_VALUE
        }
    }
}

data class Server(
    val address: String,
    val port: Int,
    val uuid: String?,
    val alterId: Int?,
    val network: String? // vmess, vless, etc
)
