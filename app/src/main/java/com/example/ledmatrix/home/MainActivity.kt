package com.example.ledmatrix.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.ledmatrix.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.frag_container)
        }
    }
}