package nolambda.linkrouter.android.extra.caller

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.test.TestRoute

class CallerAppRouterSpec : BehaviorSpec({
    Given("Router with caller middleware") {
        val testRouter = object : AbstractAppRouter<String>(CallerProviderMiddleware { _, caller ->
            caller as String
        }) {}

        When("Call with string caller") {
            val caller = "Caller"
            val router = CallerAppRouter(testRouter, caller)
            val testRoute = TestRoute()

            var extra: String? = null
            testRouter.register(testRoute) {
                extra = it.extra
            }
            router.push(testRoute)

            Then("The extra should be the caller") {
                extra shouldBe caller
            }
        }
    }
})