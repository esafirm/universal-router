package nolambda.linkrouter.android.test

import nolambda.linkrouter.android.Route

open class TestRoute(
    vararg paths: String = emptyArray()
) : Route(*paths)