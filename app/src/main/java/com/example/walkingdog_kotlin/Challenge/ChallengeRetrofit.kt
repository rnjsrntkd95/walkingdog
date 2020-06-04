package com.example.walkingdog_kotlin.Challenge

import com.example.walkingdog_kotlin.Challenge.Model.ChallengeListModel
import com.example.walkingdog_kotlin.Challenge.Model.ChallengeModel
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDate

interface ChallengeRetrofit {
    @GET("/challenge/search")
    fun getChallengeList(@Query ("userToken") userToken: String): Call<ChallengeListModel>
    @DELETE("/challenge/drop")
    fun deleteChallenge(@Query("userToken") userToken: String,
                        @Query("_id") _id: String): Call<ChallengeListModel>
    @POST("/challenge/join")
    fun joinChallenge(@Query("userToken") userToken: String,
                      @Query("_id") _id: String): Call<ChallengeListModel>
    @FormUrlEncoded
    @POST("/challenge/new")
    fun createChallenge(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("breed") breed: String,
        @Field("start_date") start_date: LocalDate,
        @Field("end_date") end_date: LocalDate,
        @Field("userToken") userToken: String
    ): Call<ChallengeModel>
}