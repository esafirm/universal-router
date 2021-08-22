package nolambda.linkrouter.examples.stack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stack.*
import nolambda.linkrouter.android.autoregister.AutoRegister
import nolambda.linkrouter.android.extra.stack.StackAppRouter
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
            stackRouter.replace(FragmentStackRoute(StackFragment.create(index)))
        }
    }
}