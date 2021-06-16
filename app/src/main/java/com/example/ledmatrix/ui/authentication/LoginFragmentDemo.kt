package com.example.ledmatrix.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityLoginDemoBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.*
import kotlinx.android.synthetic.main.activity_login_demo.*

class LoginFragmentDemo : Fragment() {
    private lateinit var binding: ActivityLoginDemoBinding

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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_demo)
//        setUpTabs()
//        setUpTransforming()
//    }

    private fun setUpTransforming() {
        binding.apply {
            loginFabFb.translationY = 800F
            loginFabGoogle.translationY = 300F
            loginFabZalo.translationY = 800F
            login_tab_layout.translationY = 300F

            loginFabFb.alpha = 0F
            loginFabGoogle.alpha = 0F
            loginFabZalo.alpha = 0F
            login_tab_layout.alpha = 0F

            loginFabFb.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400)
                .start()
            loginFabGoogle.animate().translationY(0F).alpha(1F).setDuration(2000).setStartDelay(600)
                .start()
            loginFabZalo.animate().translationY(0F).alpha(1F).setDuration(2000).setStartDelay(800)
                .start()
            login_tab_layout.animate().translationY(0F).alpha(1F).setDuration(1000)
                .setStartDelay(100).start()
        }
    }

    private fun setUpTabs() {

//            loginTabLayout.addTab(login_tab_layout.newTab().setText("Đăng Nhập"))
//            loginTabLayout.addTab(login_tab_layout.newTab().setText("Đăng Kí"))
        login_tab_layout.tabGravity = GRAVITY_FILL
        val adapter = LoginAdapter(childFragmentManager)
        adapter.addFragment(LoginTabFragment(), "Login")
        adapter.addFragment(SignupTabFragment(), "Sign Up")
        login_view_pager.adapter = adapter
        Log.e(TAG, "adapter")
        login_tab_layout.setupWithViewPager(login_view_pager)
//            loginViewPager.addOnPageChangeListener(
//                TabLayout.TabLayoutOnPageChangeListener(
//                    loginTabLayout
//                )
//            )
//            Log.e(TAG, "change page")
    }

    companion object {
        const val TAG = "LoginActivityDemo"
    }
}