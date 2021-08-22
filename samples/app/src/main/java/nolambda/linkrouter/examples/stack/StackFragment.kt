package nolambda.linkrouter.examples.stack

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_stack.*
import nolambda.linkrouter.examples.R

class StackFragment : Fragment(R.layout.fragment_stack) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text.text = arguments?.getInt(ARG_INDEX, -1).toString()
    }

    companion object {

        private const val ARG_INDEX = "Arg.Index"

        fun create(index: Int): Fragment {
            return StackFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INDEX, index)
                }
            }
        }
    }
}