package com.example.walkingdog_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var checkFragment = CheckFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_content_layout, checkFragment)
            .commit()
    }
}
