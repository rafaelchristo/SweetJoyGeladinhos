package com.example.sweetjoygeladinhos

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class SweetJoyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Log.d("FirebaseCheck", "Firebase inicializado: ${FirebaseApp.getApps(this).isNotEmpty()}")
    }
}
