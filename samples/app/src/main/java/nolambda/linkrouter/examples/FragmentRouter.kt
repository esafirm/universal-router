package nolambda.linkrouter.examples

import androidx.fragment.app.Fragment
import nolambda.linkrouter.UriRouter
import nolambda.linkrouter.addEntry

class FragmentRouter(private val activity: MainActivity) : UriRouter<Fragment>() {

    private val fragmentManager by lazy { activity.supportFragmentManager }

    init {
        addEntry("sample://fragment/{text}") { _, it ->
            SampleFragment.newInstance(it["text"] ?: throw IllegalStateException("Uri not valid"))
        }
    }

    fun goTo(uri: String) {
        val fragment = resolve(uri) ?: throw IllegalStateException("No path for $uri")
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }
}
