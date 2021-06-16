package com.example.ledmatrix.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.ledmatrix.R
import androidx.fragment.app.replace
import kotlinx.android.synthetic.main.activity_onboarding1.*

class Onboarding1Fragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_onboarding1,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.btn_onboarding1_next)
        setupTransforming()
        button.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                replace<Onboarding2Fragment>(R.id.fragment_container_view)
                addToBackStack(null)
            }
        }
    }

    private fun setupTransforming() {
        iv_onboard1_icon.translationY = 300F
        iv_onboard1_text.translationY = 300F
        iv_onboard1_icon.alpha = 0F
        iv_onboard1_text.alpha = 0F
        iv_onboard1_icon.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400).start()
        iv_onboard1_text.animate().translationY(0F).alpha(1F).setDuration(2000).setStartDelay(600).start()
    }
}