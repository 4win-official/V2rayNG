package com.your.package.name.ui

object V2RayService {
    fun connect(server: Server) {
        // TODO: این‌جا باید فراخوانی v2ray-core یا روش دلخواهت برای اتصال بیاد.
        // فرضی:
        // Core.start(server.toJsonConfig())
    }
    fun disconnect() {
        // TODO: متوقف کردن سرویس VPN
        // Core.stop()
    }
}
