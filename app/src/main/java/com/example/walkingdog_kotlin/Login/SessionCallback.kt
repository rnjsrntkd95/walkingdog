package com.example.walkingdog_kotlin.Login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.walkingdog_kotlin.MainActivity
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException


class SessionCallback(val context: Context) : ISessionCallback {
    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("Log", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
    }

    override fun onSessionOpened() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {

            override fun onFailure(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call back :: on failed ${errorResult?.errorMessage}")
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call back :: onSessionClosed ${errorResult?.errorMessage}")

            }

            override fun onSuccess(result: MeV2Response?) {
                Log.i("Log", "아이디 : ${result!!.id}")
                Log.i("Log", "이메일 : ${result.kakaoAccount.email}")
                Log.i("Log", "프로필 이미지 : ${result.profileImagePath}")
                val email = result.kakaoAccount.email
                val password = result!!.id.toString()

                LoginRetrofitCreator(email, password)

                checkNotNull(result) { "session response null" }

                val intent = Intent(context, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                context.startActivity(intent)
                (context as Activity).finish()
            }

        })
    }
}

