package nolambda.linkrouter.examples.notsimple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import nolambda.linkrouter.android.Router
import nolambda.linkrouter.approuter.AppRoutes

class HomeScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext()).apply {
            addView(Button(requireContext()).apply {
                text = "Go To Product"
                setOnClickListener {
                    Router.push(AppRoutes.Product)
                }
            })
        }
    }
}