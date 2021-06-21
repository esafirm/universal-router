package nolambda.linkrouter.examples

import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.switch.KSwitch
import io.github.kakaocup.kakao.text.KButton

class PerformanceTestScreen : Screen<PerformanceTestScreen>() {
    val btnTest = KButton { withId(R.id.btn_test) }
    val switchIsLazy = KSwitch { withId(R.id.switch_lazy) }
    val switchIsNoUrl = KSwitch { withId(R.id.switch_no_url) }
}