package com.example.sweetjoygeladinhos

import android.app.Application
import com.google.firebase.FirebaseApp

class SweetJoyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
