package com.example.walkingdog_kotlin.Login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.walkingdog_kotlin.Login.Model.LoginModel
import com.example.walkingdog_kotlin.MainActivity
import com.kakao.usermgmt.UserManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreators(val context: Context) {

    val serverUrl = "http://192.168.0.51:3000"

    fun LoginRetrofitCreator(email: String, password: String) {
        val kakaoRetrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getToken = kakaoRetrofit.create(LoginRetrofit::class.java)

        getToken.getLoginToken(email, password).enqueue(object : Callback<LoginModel> {
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Log.d("DEBUG", " Login Retrofit failed!!")
                Log.d("DEBUG", t.message)
            }

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                Log.d("TAG", "Login Retrofit Success!!")
                val token = response.body()?.loginToken

                if (token != null) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    context.startActivity(intent)
                    (context as Activity).finish()
                } else {
                    Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}