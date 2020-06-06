package com.example.walkingdog_kotlin.Challenge

import android.content.Context
import com.example.walkingdog_kotlin.R
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChallengeRetrofitCreators(val context: Context) {
//    val serverUrl = context.getString(R.string.server_url)
    val serverUrl = "http://10.0.2.2:3000"

    fun ChallengeRetrofitCreator(): ChallengeRetrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val challengeRetrofit = retrofit.create(ChallengeRetrofit::class.java)

        return challengeRetrofit
    }
}