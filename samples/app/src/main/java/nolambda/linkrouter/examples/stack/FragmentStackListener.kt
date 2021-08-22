package nolambda.linkrouter.examples.stack

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import nolambda.linkrouter.android.StackRouterItem
import nolambda.linkrouter.android.StackRouterListener

class FragmentStackListener(
    @IdRes private val containerId: Int,
    private val fragmentManager: FragmentManager
) : StackRouterListener {

    override fun shouldHandle(result: Any?): Boolean {
        return result != null && result is Fragment
    }

    override fun onAdd(item: StackRouterItem, result: Any?) {
        checkNotNull(result) { "Result should reflect to shouldHandle function" }

        result as Fragment

        fragmentManager.beginTransaction()
            .add(containerId, result)
            .addToBackStack(item.route.toString())
            .commit()
    }

    override fun onPop(item: StackRouterItem) {
        fragmentManager.popBackStack(item.route.toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}