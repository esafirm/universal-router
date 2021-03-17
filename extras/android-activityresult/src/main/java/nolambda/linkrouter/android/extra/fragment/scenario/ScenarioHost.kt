package nolambda.linkrouter.android.extra.fragment.scenario

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner

sealed class ScenarioHost
class ActivityHost(val activity: ComponentActivity) : ScenarioHost()
class FragmentHost(val fragment: Fragment) : ScenarioHost()

val ScenarioHost.viewModelStore: ViewModelStoreOwner
    get() = when (this) {
        is ActivityHost -> activity
        is FragmentHost -> fragment
    }

val ScenarioHost.lifecycle: Lifecycle
    get() = when (this) {
        is ActivityHost -> activity.lifecycle
        is FragmentHost -> fragment.viewLifecycleOwner.lifecycle
    }
