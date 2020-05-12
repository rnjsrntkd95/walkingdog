package com.example.walkingdog_kotlin.Login

import android.util.Log
import com.example.walkingdog_kotlin.Login.Model.LoginModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val serverUrl = "http://192.168.0.51:3000"

fun LoginRetrofitCreator (email: String, password: String) {
    val kakaoRetrofit = Retrofit.Builder()
        .baseUrl(serverUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getToken = kakaoRetrofit.create(LoginRetrofit::class.java)

    getToken.getLoginToken(email, password).enqueue(object: Callback<LoginModel> {
        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
            Log.d("DEBUG", " Login Retrofit failed!!")
            Log.d("DEBUG", t.message)
        }
        override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
            Log.d("TAG", "Login Retrofit Success!!")
            val token = response.body()?.loginToken
        }
    })
}