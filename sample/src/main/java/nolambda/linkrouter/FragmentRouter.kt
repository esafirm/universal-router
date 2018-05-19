package nolambda.linkrouter

import android.support.v4.app.Fragment

class FragmentRouter(private val activity: MainActivity) : Router<Fragment>() {

    private val fragmentManager by lazy { activity.supportFragmentManager }

    init {
        addEntry("sample://fragment/{text}") {
            SampleFragment.newInstance(it["text"] ?: throw IllegalStateException("Uri not valid"))
        }
    }

    override fun goTo(uri: String) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, resolve(uri))
                .commit()
    }
}
