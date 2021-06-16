package com.example.ledmatrix.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ledmatrix.R

class CommutorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commutor)

        val homeFragment = HomeFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_commutor, homeFragment).commit()

    }
}