package nolambda.linkrouter.examples.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

fun Context.isDebuggable(): Boolean {
    val pm: PackageManager = packageManager
    return try {
        val appInfo: ApplicationInfo = pm.getApplicationInfo(packageName, 0)
        0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}