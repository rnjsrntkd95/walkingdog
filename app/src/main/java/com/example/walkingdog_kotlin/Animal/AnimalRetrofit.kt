package com.example.walkingdog_kotlin.Animal

import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.Animal.Model.SetNewAnimalModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalRetrofit {

    @GET("/animal/breed-list")
    fun getAllBreed(): Call<BreedListModel>

    @FormUrlEncoded
    @POST("/animal/register")
    fun setNewAnimal(@Field ("userToken") userToken: String,
                     @Field("breed") breed: String,
                     @Field("animalName") animalName: String,
                     @Field("age") age: Int,
                     @Field("weight") weight: Number,
                     @Field("gender") gender: String): Call<SetNewAnimalModel>

}