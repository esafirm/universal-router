package nolambda.linkrouter.android.extra.fragment

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.BaseRoute
import nolambda.linkrouter.android.RouterProcessor

/* --------------------------------------------------- */
/* > Launcher Creation */
/* --------------------------------------------------- */

internal fun Fragment.createLauncher(onCallback: (ActivityResult) -> Unit) =
    registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        onCallback
    )

internal fun ComponentActivity.createLauncher(onCallback: (ActivityResult) -> Unit) =
    registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        onCallback
    )

/* --------------------------------------------------- */
/* > Execution */
/* --------------------------------------------------- */

internal fun RouterProcessor<*>.pushToProcessor(
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