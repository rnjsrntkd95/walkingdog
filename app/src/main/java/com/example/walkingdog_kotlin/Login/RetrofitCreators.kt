package com.example.walkingdog_kotlin.Login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.walkingdog_kotlin.Login.Model.LoginModel
import com.example.walkingdog_kotlin.Login.Model.SetNicknameModel
import com.example.walkingdog_kotlin.R
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitCreators(val context: Context) {

//    val serverUrl = context.getString(R.string.server_url)

    val serverUrl = context.getString(R.string.server_url)


    fun LoginRetrofitCreator(email: String, password: String): LoginRetrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getToken = retrofit.create(LoginRetrofit::class.java)
        return getToken

    }

    fun SetNicknameRetrofit(nickname: String): LoginRetrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val nicknameRetrofit = retrofit.create(LoginRetrofit::class.java)

        return nicknameRetrofit
    }

    fun ProfileRetrofit(): LoginRetrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val profileRetrofit = retrofit.create(LoginRetrofit::class.java)

        return profileRetrofit
    }
}