package nolambda.linkrouter.examples.picker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_picker_example.*
import nolambda.linkrouter.android.extra.fragment.registerRouteForResult
import nolambda.linkrouter.android.extra.fragment.scenario.registerScenarioForResult
import nolambda.linkrouter.approuter.AppRouter
import nolambda.linkrouter.approuter.register
import nolambda.linkrouter.examples.R

class PickerExampleScreen : AppCompatActivity(R.layout.activity_picker_example) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ResultPickerRoute.register {
            val launcher = it.extra?.launcher
            launcher?.launch(Intent(this, ResultPickerScreen::class.java))
        }

        btn_picker.setOnClickListener {
            showPicker(AppRouter)
        }

        btn_picker_scenario.setOnClickListener {
            showPickerScenario.launch(AppRouter)
        }
    }

    private val showPicker = registerRouteForResult(ResultPickerRoute) {
        txt_result.text = "Result data: ${it.data?.getStringExtra("result")}"
    }

    private val showPickerScenario = registerScenarioForResult(ResultPickerScenario()) {
        txt_result.text = it
    }

}
