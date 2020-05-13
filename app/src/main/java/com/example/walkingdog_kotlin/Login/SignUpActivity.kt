/*가입 시, 정보를 입력하는 화면*/

package com.example.walkingdog_kotlin.Login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.walkingdog_kotlin.Login.Information.*
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.R

class SignUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //액티비티 호출 시 FramLayout의 defalt 값을 emailFragment로 지정
        val nicknameFragment = NicknameFragment(this)
        supportFragmentManager.beginTransaction().replace(R.id.signUp_content, nicknameFragment)
            .commit()

    }

    fun finishActivity() {  //액티비티 종료 함수
        finish()
    }

    fun moveEmail() {
        val emailFragment = EmailFragment()
        supportFragmentManager.beginTransaction().replace(R.id.signUp_content, emailFragment)
            .commit()
    }

    fun movePassword() {    //FrameLayout에 PasswordFragment를 지정
        val passwordFragment = PasswordFragment()
        supportFragmentManager.beginTransaction().replace(R.id.signUp_content, passwordFragment)
            .commit()
    }


    fun moveUserName() {        //FrameLayout에 name fragment 지정
        val userNameFragment = UserNameFragment()
        supportFragmentManager.beginTransaction().replace(R.id.signUp_content, userNameFragment)
            .commit()
    }

    fun moveNickname() {    //FrameLayout에 nickname fragment 지정
        val nicknameFragment = NicknameFragment(this)
        supportFragmentManager.beginTransaction().replace(R.id.signUp_content, nicknameFragment)
            .commit()
    }

    fun movePhoneNum() {
        val phoneNumFragment = PhoneFragment()
        supportFragmentManager.beginTransaction().replace(R.id.signUp_content, phoneNumFragment)
            .commit()
    }

    fun startMain() {       //현재 액티비티를 종료하고 mainActivity를 실행
         finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}
