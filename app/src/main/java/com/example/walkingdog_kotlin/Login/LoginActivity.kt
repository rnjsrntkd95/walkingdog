package com.example.walkingdog_kotlin.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email_signUp_btn.setOnClickListener { view ->
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
