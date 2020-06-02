package com.example.walkingdog_kotlin.Comment.Model

import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.Animal.Model.SetNewAnimalModel
import retrofit2.Call
import retrofit2.http.*

interface CommentRetrofit {

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
    @GET("animal/mypet")
    fun findMyPet(@Query("userToken") userToken: String,
                  @Query("user") _id: String
    ): Call<SetNewAnimalModel>

}