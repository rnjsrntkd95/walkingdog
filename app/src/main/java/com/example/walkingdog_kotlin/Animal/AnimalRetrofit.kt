package com.example.walkingdog_kotlin.Animal

import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import retrofit2.Call
import retrofit2.http.GET

interface AnimalRetrofit {

    @GET("/animal/breed_list")
    fun getAllBreed(): Call<BreedListModel>

}