package com.example.walkingdog_kotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.walkingdog_kotlin.Login.LoginRetrofit
import com.example.walkingdog_kotlin.Login.Model.LoginModel
import com.kakao.auth.ErrorCode
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
                val kakaoRetrofit = Retrofit.Builder()
                    .baseUrl("http:192.168.0.4:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val getToken = kakaoRetrofit.create(LoginRetrofit::class.java)

                getToken.getLoginToken(email, password).enqueue(object: Callback<LoginModel> {
                    override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                        Log.d("DEBUG", "실패")
                        Log.d("DEBUG", t.message)
                    }
                    override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                        Log.d("TAG", "성공")
                        val token = response.body()?.loginToken
                        Log.d("TAG", token)
                    }

                })

                checkNotNull(result) { "session response null" }

                val intent = Intent(context, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                context.startActivity(intent)
                (context as Activity).finish()
            }

        })
    }
}

