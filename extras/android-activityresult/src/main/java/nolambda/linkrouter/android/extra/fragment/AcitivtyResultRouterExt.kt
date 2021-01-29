package nolambda.linkrouter.android.extra.fragment

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.RouteWithParam
import nolambda.linkrouter.android.RouterProcessor


typealias RouteResultLauncher = (RouterProcessor<*>) -> Unit
typealias RouteResultLauncherWithParam<P> = (RouterProcessor<*>, param: P) -> Unit

fun ComponentActivity.registerRouteForResult(
    route: Route,
    onCallback: (ActivityResult) -> Unit
): RouteResultLauncher {
    val launcher = createLauncher(onCallback)
    return { it.pushToProcessor(launcher, route) }
}

fun <P : Any> ComponentActivity.registerRouteForResult(
    route: RouteWithParam<P>,
    onCallback: (ActivityResult) -> Unit
): RouteResultLauncherWithParam<P> {
    val launcher = createLauncher(onCallback)
    return { router, param -> router.pushToProcessor(launcher, route, param) }
}

fun Fragment.registerRouteForResult(
    route: Route,
    onCallback: (ActivityResult) -> Unit
): RouteResultLauncher {
    val launcher = createLauncher(onCallback)
    return { it.pushToProcessor(launcher, route) }
}

fun <P : Any> Fragment.registerRouteForResult(
    route: RouteWithParam<P>,
    onCallback: (ActivityResult) -> Unit
): RouteResultLauncherWithParam<P> {
    val launcher = createLauncher(onCallback)
    return { router, param -> router.pushToProcessor(launcher, route, param) }
}

/* --------------------------------------------------- */
/* > Launcher Creation */
/* --------------------------------------------------- */

private fun Fragment.createLauncher(onCallback: (ActivityResult) -> Unit) =
    registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        onCallback
    )

private fun ComponentActivity.createLauncher(onCallback: (ActivityResult) -> Unit) =
    registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        onCallback
    )

/* --------------------------------------------------- */
/* > Execution */
/* --------------------------------------------------- */

private fun RouterProcessor<*>.pushToProcessor(
    launcher: ActivityResultLauncher<Intent>,
    route: BaseRoute<*>,
    param: Any? = null,
) {
    push(ActivityResultLauncherProcessor, ActivityResultLauncherProcessor.Param(
        launcher = launcher,
        originalParam = param,
        originalRoute = route
    ))
}

