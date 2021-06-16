package com.example.ledmatrix.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.ledmatrix.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            setContentView(R.layout.activity_main)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                add<Onboarding1Fragment>(R.id.fragment_container_view)
            }
        }, 3000)
    }
}