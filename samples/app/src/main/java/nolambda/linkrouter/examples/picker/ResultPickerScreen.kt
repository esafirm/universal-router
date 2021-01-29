package nolambda.linkrouter.examples.picker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result_picker.*
import nolambda.linkrouter.examples.R

class ResultPickerScreen : AppCompatActivity(R.layout.activity_result_picker) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnResult.setOnClickListener {
            val data = Intent().apply {
                putExtra("result", "abc")
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}