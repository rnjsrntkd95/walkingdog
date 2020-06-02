package com.example.walkingdog_kotlin.Challenge

import android.content.Context
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeListModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface ChallengeRetrofit {
    @GET("/challenge/search")
    fun getChallengeList(@Query ("userToken") userToken: String): Call<ChallengeListModel>
    @DELETE("/challenge/drop")
    fun deleteChallenge(@Query("userToken") userToken: String,
                        @Query("_id") _id: String): Call<ChallengeListModel>
    @POST("/challenge/join")
    fun joinChallenge(@Query("userToken") userToken: String,
                      @Query("_id") _id: String): Call<ChallengeListModel>
    @POST("/challenge/new")
    fun createChallenge(): Call<ChallengeListModel>
}