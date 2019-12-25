package nolambda.linkrouter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SampleFragment : androidx.fragment.app.Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(text: String): SampleFragment = SampleFragment().apply {
            arguments = Bundle().apply {
                putString("text", text)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false).apply {
            val text = findViewById<TextView>(R.id.text)
            text.text = arguments?.getString("text")
        }
    }
}
