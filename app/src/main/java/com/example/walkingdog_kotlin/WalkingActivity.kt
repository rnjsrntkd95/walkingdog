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
import java.util.*
import kotlin.concurrent.timer

class WalkingActivity : AppCompatActivity() {

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null
    private var lap = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking)

        //상태바 색을 그레이로 변경
        window.statusBarColor = (ContextCompat.getColor(applicationContext, R.color.mainGray))
        //상태바 아이콘 색 플레그(흰색->검은색)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        camera_btn.setOnClickListener {
            var intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }

        playFab.setOnClickListener {
            //플레이 버튼
            isRunning = !isRunning

            if (isRunning) { // true
                start()
            } else { // false
                pause()
            }
        }

        resetFab.setOnClickListener {
            //리셋 버튼
            reset()
        }

    }

    private fun pause() { // 타이머 일시정지
        playFab.setBackgroundResource(R.drawable.play_icon)
        timerTask?.cancel() // 실행중인 타이머가 있다면 취소한다.
    }

    private fun start() { //타이머 스타트
        playFab.setBackgroundResource(R.drawable.pause_icon) //일시정지 이미지로 바꿔줌.

        timerTask = timer(period = 10) {
            // period = 10 0.01초 , period = 1000 면 1초
            time++
            // 0.01초마다 변수를 증가시킴

            val hour = (time / 144000) % 24 // 1시간
            val min = (time / 6000) % 60 // 1분
            val sec = (time / 100) % 60 //1초
            val milli = time % 100 // 0.01 초
            runOnUiThread {
                // Ui 를 갱신 시킴.

                if (min < 10) { // 분
                    minTextView.text = "0$min"
                } else {
                    minTextView.text = "$min"
                }

                if (sec < 10) { // 초
                    secTextView.text = "0$sec"
                } else {
                    secTextView.text = "$sec"
                }

                if (milli < 10) {
                    milliTextView.text = "0$milli"
                } else {
                    milliTextView.text = "$milli"
                }

                //$ 를 붙여주면 변하는 값을 계속 덮어준다
                //ex) $를 붙여주면 기존에 1이라는 값이 잇을때 값이 2로변하면 2로 바꿔준다.

            }
        }

    }

    private fun reset() {
        timerTask?.cancel()       // 실행중인 타이머 취소

        // 모든 변수 초기화
        time = 0
        isRunning = false
        playFab.setBackgroundResource(R.drawable.play_icon)
        minTextView.text = "00"
        secTextView.text = "00"
        milliTextView.text = "00"

    }
}
