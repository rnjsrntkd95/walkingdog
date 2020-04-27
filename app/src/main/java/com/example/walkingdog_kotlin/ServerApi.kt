package com.example.walkingdog_kotlin

import com.example.walkingdog_kotlin.model.ServerApiModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ServerApiRouter {

    @GET("/message")
    fun getAppName(): Call<ServerApiModel>

//    @FormUrlEncoded
    @POST("/message")
    fun getPostAppName(): Call<ServerApiModel>

}