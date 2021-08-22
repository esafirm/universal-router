package nolambda.linkrouter.examples.stack

import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.approuter.register

class FragmentStackRoute(val fragment: Fragment) : Route() {
    init {
        register { fragment }
    }
}