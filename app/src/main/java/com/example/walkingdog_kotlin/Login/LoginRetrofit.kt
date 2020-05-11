package com.example.walkingdog_kotlin.Login

import com.example.walkingdog_kotlin.Login.Model.LoginModel
import retrofit2.Call
import retrofit2.http.*


interface LoginRetrofit {

    @FormUrlEncoded
    @POST("/login")
    fun getLoginToken(@Field("email") email: String,
                      @Field("password") password: String): Call<LoginModel>
}