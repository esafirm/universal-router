package nolambda.linkrouter.examples.picker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_picker_example.*
import nolambda.linkrouter.android.extra.fragment.registerRouteForResult
import nolambda.linkrouter.approuter.AppRouter
import nolambda.linkrouter.approuter.register
import nolambda.linkrouter.examples.R

class PickerExampleFragmentScreen : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, PickerFragment())
                .commit()
        }
    }
}

class PickerFragment : Fragment(R.layout.activity_picker_example) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ResultPickerRoute.register {
            val intent = Intent(requireContext(), ResultPickerScreen::class.java)
            val launcher = it.extra?.launcher
            launcher?.launch(intent)
        }

        btn_picker.setOnClickListener {
            showPicker(AppRouter)
        }
    }

    private val showPicker = registerRouteForResult(ResultPickerRoute) {
        txt_result.text = "Result data: ${it.data?.getStringExtra("result")}"
    }
}