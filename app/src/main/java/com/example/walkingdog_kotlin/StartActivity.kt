package com.example.walkingdog_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.walkingdog_kotlin.Login.LoginActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        start_btn.setOnClickListener { view ->
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
