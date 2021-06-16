package com.example.ledmatrix.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ledmatrix.R
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.android.synthetic.main.login_tab_fragment.*

class LoginTabFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_tab_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupTransforming()
    }

//    private fun setupTransforming() {
//        loginEmail.translationX = 800F
//        loginPass.translationX = 800F
//        loginForget.translationX = 800F
//        loginBtn.translationX = 800F
//
//        loginEmail.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(300)
//            .start()
//        loginPass.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500)
//            .start()
//        loginForget.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500)
//            .start()
//        loginBtn.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(700)
//            .start()
//    }
}