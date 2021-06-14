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

        val loginEmail: EditText = view.findViewById(R.id.login_email)
        val loginPass: EditText = view.findViewById(R.id.login_pass)
        val loginForget: TextView = view.findViewById(R.id.login_forget_pass)
        val loginBtn: Button = view.findViewById(R.id.login_btn)


        loginEmail.translationX = 800F
        loginPass.translationX = 800F
        loginForget.translationX = 800F
        loginBtn.translationX = 800F

        loginEmail.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(300)
            .start()
        loginPass.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500)
            .start()
        loginForget.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(500)
            .start()
        loginBtn.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(700)
            .start()
        return view
    }
}