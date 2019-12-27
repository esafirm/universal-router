package nolambda.linkrouter.examples

import androidx.fragment.app.Fragment

class FragmentRouter(private val activity: MainActivity) : UriRouter<Fragment>() {

    private val fragmentManager by lazy { activity.supportFragmentManager }

    init {
        addEntry("sample://fragment/{text}") {
            SampleFragment.newInstance(it["text"] ?: throw IllegalStateException("Uri not valid"))
        }
    }

    fun goTo(uri: String) {
        fragmentManager.beginTransaction()
            .replace(R.id.container, resolve(uri))
            .commit()
    }
}
