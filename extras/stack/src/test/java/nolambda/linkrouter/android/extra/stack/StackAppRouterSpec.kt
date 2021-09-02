package nolambda.linkrouter.android.extra.stack

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nolambda.linkrouter.android.AbstractAppRouter
import nolambda.linkrouter.android.Route
import nolambda.linkrouter.android.extra.stack.fake.FakeStackListener
import nolambda.linkrouter.android.test.TestRoute

class StackAppRouterSpec : StringSpec({

    val createRouter = { listener: StackRouterListener ->
        val router = object : AbstractAppRouter<Any>() {}
        StackAppRouter(router, listener)
    }

    val fakeListener = FakeStackListener()
    val testRouter = createRouter(fakeListener)

    val testRoute = TestRoute()

    "push should be working as expected" {
        val expectedResult = "123"

        testRouter.register(testRoute) { expectedResult }
        testRouter.push(testRoute)

        fakeListener.stack.size shouldBe 1
        fakeListener.stack.peek() shouldBe expectedResult
    }

    "pop should be working as expected" {
        testRouter.pop()

        fakeListener.stack.size shouldBe 0
    }

    "pop until should be working as expected" {

    }

    "replace should be working as expected" {

    }
})