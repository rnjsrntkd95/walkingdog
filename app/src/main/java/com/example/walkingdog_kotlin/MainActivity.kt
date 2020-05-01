package com.example.walkingdog_kotlin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.main_content_framelayout, FeedFragment())
            .commit()

        //액티비티에서 상태바 아이콘 흰색으로 만드는 코드
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //액티비티에서 상태바 배경 색 변경하는 코드1
        //window.statusBarColor = (ContextCompat.getColor(applicationContext, R.color.mainGray))

        //상태바 색 변경하는 코드2
        //getWindow().setStatusBarColor(Color.parseColor("#edf1f5"))


    }
}
