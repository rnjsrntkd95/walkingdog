package com.example.walkingdog_kotlin


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.walkingdog_kotlin.Login.LoginActivity


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)
    }
}
