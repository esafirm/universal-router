package nolambda.linkrouter.examples

import androidx.fragment.app.Fragment
import nolambda.linkrouter.SimpleUriRouter

class FragmentRouter(private val activity: MainActivity) {

    private val simpleUriRouter = SimpleUriRouter<Fragment>()

    private val fragmentManager by lazy { activity.supportFragmentManager }

    init {
        simpleUriRouter.addEntry("sample://fragment/{text}") { _, it ->
            SampleFragment.newInstance(it["text"] ?: throw IllegalStateException("Uri not valid"))
        }
    }

    fun goTo(uri: String) {
        val fragment = simpleUriRouter.resolve(uri)
            ?: throw IllegalStateException("No path for $uri")
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }
}
