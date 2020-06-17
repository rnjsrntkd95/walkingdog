package com.example.walkingdog_kotlin.Login

import com.example.walkingdog_kotlin.Login.Model.LoginModel
import com.example.walkingdog_kotlin.Login.Model.MyProfileModel
import com.example.walkingdog_kotlin.Login.Model.SetNicknameModel
import retrofit2.Call
import retrofit2.http.*


interface LoginRetrofit {

    @FormUrlEncoded
    @POST("/login")
    fun getLoginToken(@Field("email") email: String,
                      @Field("password") password: String): Call<LoginModel>

    @FormUrlEncoded
    @PUT("/login/nickname")
    fun setNickname(@Field("userToken") userToken: String,
                    @Field("nickname") nickname: String): Call<SetNicknameModel>

    @GET("/animal/mypet")
    fun getMyProfile(@Query("userToken") userToken: String): Call<MyProfileModel>
}