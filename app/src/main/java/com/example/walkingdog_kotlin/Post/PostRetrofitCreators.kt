package com.example.walkingdog_kotlin.Post

import android.content.Context
import com.example.walkingdog_kotlin.R
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostRetrofitCreators(val context: Context) {

//    val serverUrl = context.getString(R.string.server_url)
    val serverUrl = "http://10.0.2.2:3000"

    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

    fun PostRetrofitCreator(): PostRetrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val postRetrofit = retrofit.create(PostRetrofit::class.java)

        return postRetrofit
    }
}