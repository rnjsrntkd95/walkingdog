package com.example.walkingdog_kotlin.Login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.walkingdog_kotlin.Login.Model.LoginModel
import com.example.walkingdog_kotlin.MainActivity
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

                val retrofit = RetrofitCreators(context)
                val getToken = retrofit.LoginRetrofitCreator(email, password)

                getToken.getLoginToken(email, password).enqueue(object : Callback<LoginModel> {
                    override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                        Log.d("DEBUG", " Login Retrofit failed!!")
                        Log.d("DEBUG", t.message)
                    }

                    override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                        Log.d("TAG", "Login Retrofit Success!!")
                        val token = response.body()?.loginToken
                        val nickname = response.body()?.nickname

                        if (token != null) {
                            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
                            val edit = pref.edit()
                            edit.putString("userToken", token)
                            edit.apply()

                            if (nickname != null) {
                                val intent = Intent(context, MainActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                (context as Activity).finish()
                            } else {
                                // 닉네임이 없으면 닉네임 설정 Activity 전송
                                val intent = Intent(context, SignUpActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                (context as Activity).finish()
                            }
                        } else {
                            Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })

                checkNotNull(result) { "session response null" }
            }

        })
    }
}

