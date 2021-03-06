package com.example.walkingdog_kotlin.Challenge

import com.example.walkingdog_kotlin.Challenge.Model.*
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDate

interface ChallengeRetrofit {
    @GET("/challenge/search")
    fun getChallengeList(@Query ("userToken") userToken: String): Call<ChallengeListModel>

    @FormUrlEncoded
    @POST("/challenge/drop")
    fun deleteChallenge(@Field("userToken") userToken: String,
                        @Field("_id") _id: String): Call<DropChallengeResultModel>
    @FormUrlEncoded
    @POST("/challenge/join")
    fun joinChallenge(@Field("userToken") userToken: String,
                      @Field("_id") _id: String): Call<JoinChallengeResultModel>
    @FormUrlEncoded
    @POST("/challenge/new")
    fun createChallenge(
        @Field("title") title: String,
        @Field("content") content: String,
//        @Field("breed") breed: String,
        @Field("start_date") start_date: LocalDate,
        @Field("end_date") end_date: LocalDate,
        @Field("userToken") userToken: String
    ): Call<CreateChallengeResultModel>
    @FormUrlEncoded
    @POST("/challenge/users")
    fun getUserList(
        @Field("userToken") userToken: String,
        @Field("challengeId") challengeId: String
    ): Call<ChallengeUserListModel>
    @GET("/challenge/myChallenge")
    fun getMyChallenge(
        @Query("userToken") userToken: String
    ): Call<myChallengeId>
}