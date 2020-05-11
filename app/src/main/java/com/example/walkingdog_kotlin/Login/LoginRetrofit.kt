package com.example.walkingdog_kotlin.Login

import com.example.walkingdog_kotlin.Login.Model.LoginModel
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface LoginRetrofit {

    @Headers("accept: application/json",
        "content-type: application/json")
    @POST("/login")
    fun getLoginToken(@Header("email") email: String,
                      @Header("password") password: String): Call<LoginModel>
}