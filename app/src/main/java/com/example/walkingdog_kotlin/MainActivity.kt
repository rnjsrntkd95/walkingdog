package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        user?.let {
            val username = user.displayName
            val email = user.email
            val emailVerified = user.isEmailVerified
            val uid = user.uid

            Log.i("TAG", "username: " + username)
            Log.i("TAG", "email: " + email)
            Log.i("TAG", "emailVerified: " + emailVerified)
            Log.i("TAG", "uid: " + uid)
        }

        firebaseAuth.signOut()
    }
}
