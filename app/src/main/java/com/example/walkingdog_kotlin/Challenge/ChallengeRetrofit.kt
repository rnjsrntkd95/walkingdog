package com.example.walkingdog_kotlin.Challenge

import com.example.walkingdog_kotlin.Challenge.Model.ChallengeListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengeRetrofit {
    @GET("/challenge/search")
    fun getTimeline(@Query ("userToken") userToken: String): Call<ChallengeListModel>

}