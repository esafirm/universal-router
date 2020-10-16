package nolambda.linkrouter.android

import android.content.Context

object RouterPlugin {
    lateinit var appContext: Context
    var isUseAnnotationProcessor = false
    var logger: ((String) -> Unit)? = null
    var errorHandler: (Throwable) -> Unit = { throw it }
}