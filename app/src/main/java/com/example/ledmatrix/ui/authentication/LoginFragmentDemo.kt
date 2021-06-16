package com.example.ledmatrix.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ledmatrix.R
import com.google.android.material.tabs.TabLayout.*
import kotlinx.android.synthetic.main.activity_login_demo.*

class LoginFragmentDemo : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_login_demo, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabs()
        setUpTransforming()
    }

    private fun setUpTransforming() {
        login_fab_fb.translationY = 800F
        login_fab_google.translationY = 300F
        login_fab_zalo.translationY = 800F

        login_fab_fb.alpha = 0F
        login_fab_google.alpha = 0F
        login_fab_zalo.alpha = 0F

        login_fab_fb.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400)
            .start()
        login_fab_google.animate().translationY(0F).alpha(1F).setDuration(2000).setStartDelay(600)
            .start()
        login_fab_zalo.animate().translationY(0F).alpha(1F).setDuration(2000).setStartDelay(800)
            .start()
    }

    private fun setUpTabs() {
        login_tab_layout.tabGravity = GRAVITY_FILL
        val adapter = LoginAdapter(childFragmentManager)
        adapter.addFragment(LoginTabFragment(), "Login")
        adapter.addFragment(SignupTabFragment(), "Sign Up")
        login_view_pager.adapter = adapter
        Log.e(TAG, "adapter")
        login_tab_layout.setupWithViewPager(login_view_pager)
    }

    companion object {
        const val TAG = "LoginActivityDemo"
    }
}