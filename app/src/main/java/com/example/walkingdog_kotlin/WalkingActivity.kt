package com.example.walkingdog_kotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_walking.*

class WalkingActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking)

        //상태바 색을 그레이로 변경
        window.statusBarColor = (ContextCompat.getColor(applicationContext, R.color.mainGray))
        //상태바 아이콘 색 플레그(흰색->검은색)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        came_btn.setOnClickListener {
            var intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }
    }

}
