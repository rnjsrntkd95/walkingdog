package com.example.walkingdog_kotlin.Walking

import android.content.Context
import com.example.walkingdog_kotlin.Post.PostRetrofit
import com.example.walkingdog_kotlin.R
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WalkingRetrofitCreators(val context: Context) {

    val serverUrl = context.getString(R.string.server_url)
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

    fun WalkingRetrofitCreator(): WalkingRetrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val walkingRetrofit = retrofit.create(WalkingRetrofit::class.java)

        return walkingRetrofit
    }
}