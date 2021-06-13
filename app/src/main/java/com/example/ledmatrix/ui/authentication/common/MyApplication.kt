package com.example.ledmatrix.ui.authentication.common

import android.app.Application
import com.google.firebase.FirebaseApp.initializeApp

class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        initializeApp(applicationContext)
    }
}