package com.v2ray.ang.extension

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.v2ray.ang.AngApplication
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.io.Serializable
import java.net.URLConnection

//==============================================================================//
// Context Extensions
//==============================================================================//

/** Casts applicationContext to our custom AngApplication, or null if mismatch */
val Context.v2RayApp: AngApplication?
    get() = applicationContext as? AngApplication

/** Generic toast by resource ID */
fun Context.toast(@androidx.annotation.StringRes msgRes: Int) {
    Toasty.normal(this, msgRes).show()
}

/** Generic toast by text */
fun Context.toast(msg: CharSequence) {
    Toasty.normal(this, msg).show()
}

/** Success-style toast */
fun Context.toastSuccess(msg: CharSequence) {
    Toasty.success(this, msg, Toast.LENGTH_SHORT, true).show()
}
fun Context.toastSuccess(@androidx.annotation.StringRes msgRes: Int) {
    Toasty.success(this, msgRes, Toast.LENGTH_SHORT, true).show()
}

/** Error-style toast */
fun Context.toastError(msg: CharSequence) {
    Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show()
}
fun Context.toastError(@androidx.annotation.StringRes msgRes: Int) {
    Toasty.error(this, msgRes, Toast.LENGTH_SHORT, true).show()
}

//==============================================================================//
// JSON Extensions
//==============================================================================//

/** Puts a single key–value pair into a JSONObject */
fun JSONObject.putOpt(pair: Pair<String, Any?>) {
    put(pair.first, pair.second)
}

/** Puts multiple key–value pairs into a JSONObject */
fun JSONObject.putOpt(map: Map<String, Any?>) {
    map.forEach { (k, v) -> put(k, v) }
}

//==============================================================================//
// URI & URLConnection Extensions
//==============================================================================//

/** Returns content length in a backward-compatible way */
val URLConnection.contentLengthCompat: Long
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) contentLengthLong
            else contentLength.toLong()

/** Returns host without surrounding brackets (for IPv6 URIs) */
val Uri.idnHost: String
    get() = host?.removePrefix("[").removeSuffix("]") ?: ""

//==============================================================================//
// Numeric Formatting
//==============================================================================//

private const val THRESHOLD = 1000L
private const val DIVISOR = 1024.0

/** Formats bytes/sec */
fun Long.toSpeedString(): String = "${toTrafficString()}/s"

/** Formats bytes */
fun Long.toTrafficString(): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
    var size = toDouble()
    var idx = 0
    while (size >= THRESHOLD && idx < units.lastIndex) {
        size /= DIVISOR
        idx++
    }
    return String.format("%.1f %s", size, units[idx])
}

//==============================================================================//
// String & Bundle/Intent Serialization
//==============================================================================//

/** Safely removes all spaces */
fun String?.removeWhitespace(): String? = this?.replace("\\s+".toRegex(), "")

/** Parses long or returns 0 if invalid */
fun String.toLongSafe(): Long = toLongOrNull() ?: 0L

/** Concatenates URL segments ensuring single slashes */
fun String.concatUrl(vararg paths: String): String {
    val base = this.trimEnd('/')
    return paths.fold(base) { acc, p ->
        acc + "/" + p.trim('/')
    }
}

/** Extracts a Serializable extra in a type-safe way */
inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getSerializable(key, T::class.java)
        else ->
            @Suppress("DEPRECATION") getSerializable(key) as? T
    }

inline fun <reified T : Serializable> Intent.getSerializableCompat(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getSerializableExtra(key, T::class.java)
        else ->
            @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

//==============================================================================//
// BroadcastReceiver Helpers
//==============================================================================//

/**
 * Registers a one-shot or persistent receiver for package install/uninstall.
 * @param oneShot if true, unregister after first event
 * @param onChange callback when package changes
 */
fun Context.listenForPackageChanges(
    oneShot: Boolean = true,
    onChange: () -> Unit
): BroadcastReceiver {
    val filter = IntentFilter().apply {
        addAction(Intent.ACTION_PACKAGE_ADDED)
        addAction(Intent.ACTION_PACKAGE_REMOVED)
        addDataScheme("package")
    }
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            onChange()
            if (oneShot) unregisterReceiver(this)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
    } else {
        registerReceiver(receiver, filter)
    }
    return receiver
}
