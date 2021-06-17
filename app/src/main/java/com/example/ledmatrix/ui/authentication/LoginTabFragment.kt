package com.example.ledmatrix.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.ledmatrix.R
import com.example.ledmatrix.ui.home.CommutorActivity
import com.example.ledmatrix.ui.home.HomeFragment
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
        setupTransforming()
        login_btn.setOnClickListener {
            val intent = Intent(activity, CommutorActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupTransforming() {
        login_email.translationX = 800F
        login_pass.translationX = 800F
        login_forget_pass.translationX = 800F
        login_btn.translationX = 800F

        login_email.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(100)
            .start()
        login_pass.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(300)
            .start()
        login_forget_pass.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(500)
            .start()
        login_btn.animate().translationX(0F).alpha(1F).setDuration(600).setStartDelay(700)
            .start()
    }

    companion object{
        const val TAG = "LoginTabFragment"
    }
}