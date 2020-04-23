package com.example.walkingdog_kotlin

import com.example.walkingdog_kotlin.model.ServerApiModel
import retrofit2.Call
import retrofit2.http.GET

interface ServerApiRouter {

    @GET("/message")
    fun getAppName(): Call<ServerApiModel>
}