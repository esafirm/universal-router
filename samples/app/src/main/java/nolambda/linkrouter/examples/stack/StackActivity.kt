package nolambda.linkrouter.examples.stack

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_stack.*
import nolambda.linkrouter.android.StackAppRouter
import nolambda.linkrouter.android.StackRouterItem
import nolambda.linkrouter.android.StackRouterListener
import nolambda.linkrouter.android.addRouterProcessor
import nolambda.linkrouter.android.autoregister.AutoRegister
import nolambda.linkrouter.approuter.AppRouter
import nolambda.linkrouter.examples.R

class StackActivity : AppCompatActivity() {

    @AutoRegister
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack)

        val fragmentStack = FragmentStackListener(R.id.container, supportFragmentManager)
        val stackRouter = StackAppRouter(AppRouter, fragmentStack)

        /* --------------------------------------------------- */
        /* > Control */
        /* --------------------------------------------------- */

        var index = 0

        btn_stack_push.setOnClickListener {
            index += 1
            stackRouter.push(FragmentStackRoute(StackFragment.create(index)))
        }

        btn_stack_pop.setOnClickListener {
            stackRouter.pop()
        }

        btn_stack_replace.setOnClickListener {
            index += 1
            stackRouter.replace(
                StackRouterItem(
                    route = FragmentStackRoute(StackFragment.create(index)),
                    param = null
                )
            )
        }
    }
}