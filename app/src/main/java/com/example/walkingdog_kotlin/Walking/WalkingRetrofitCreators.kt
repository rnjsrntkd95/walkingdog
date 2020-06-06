package com.example.walkingdog_kotlin.Walking

import android.content.Context
import com.example.walkingdog_kotlin.Post.PostRetrofit
import com.example.walkingdog_kotlin.R
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WalkingRetrofitCreators(val context: Context) {

//    val serverUrl = context.getString(R.string.server_url)
    val serverUrl = "http://10.0.2.2:3000"

    fun WalkingRetrofitCreator(): WalkingRetrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val walkingRetrofit = retrofit.create(WalkingRetrofit::class.java)

        return walkingRetrofit
    }
}