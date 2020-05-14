package com.example.walkingdog_kotlin.Animal

import android.content.Context
import android.util.Log
import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimalRetrofitCreators(val context: Context) {

    val serverUrl = context.getString(R.string.server_url)

    fun AnimalRetrofitCreator(): AnimalRetrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animalRetrofit = retrofit.create(AnimalRetrofit::class.java)

        return animalRetrofit
    }
}