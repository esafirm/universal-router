package nolambda.linkrouter.examples

import androidx.test.ext.junit.rules.ActivityScenarioRule
import io.github.kakaocup.kakao.screen.Screen
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class PerformanceTest {

    @Rule
    @JvmField
    val rule = ActivityScenarioRule(PerformanceTestActivity::class.java)

    @Test
    fun standardRouting() {
        Screen.onScreen<PerformanceTestScreen> {
            btnTest.click()
        }
    }

    @Test
    fun noUrlRouting() {
        Screen.onScreen<PerformanceTestScreen> {
            switchIsNoUrl.click()
            btnTest.click()
        }
    }

    @Test
    fun lazyRouting() {
        Screen.onScreen<PerformanceTestScreen> {
            switchIsLazy.click()
            btnTest.click()
        }
    }

    @Ignore
    @Test
    fun lazyNoUrlRouting() {
        Screen.onScreen<PerformanceTestScreen> {
            switchIsLazy.click()
            switchIsNoUrl.click()
            btnTest.click()
        }
    }

    @Test
    fun keyUriRouter() {
        Screen.onScreen<PerformanceTestScreen> {
            switchKeyUri.click()
            btnTest.click()
        }
    }

    @Test
    fun lazyKeyUriRouter() {
        Screen.onScreen<PerformanceTestScreen> {
            switchIsLazy.click()
            switchKeyUri.click()
            btnTest.click()
        }
    }
}