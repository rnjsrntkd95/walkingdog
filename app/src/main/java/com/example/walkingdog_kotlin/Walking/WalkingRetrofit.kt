package com.example.walkingdog_kotlin.Walking

import com.example.walkingdog_kotlin.Walking.Model.CreateWalkingResultModel
import com.example.walkingdog_kotlin.Walking.Model.MyPetListModel
import com.example.walkingdog_kotlin.Walking.Model.MyPetModel
import com.example.walkingdog_kotlin.Walking.Model.MyWalkingStaticModel
import retrofit2.Call
import retrofit2.http.*

interface WalkingRetrofit {
    @FormUrlEncoded
    @POST("/walking/create")
    fun createWalking(@Field("userToken") userToken: String,
                      @Field("calorie") calorie: Double,
                      @Field("distance") distance: Double,
                      @Field("walkingTime") walkingTime: Int,
                      @Field("walkingAmounts") walkingAmounts: ArrayList<Double>,
                      @Field("addressAdmin") addressAdmin: String,
                      @Field("addressLocality") addressLocality: String,
                      @Field("addressThoroughfare") addressThoroughfare: String,
                      @Field("animal") animal: ArrayList<String>,
                      @Field("route") route: ArrayList<ArrayList<Double>>,
                      @Field("toiletLoc") toiletLoc: ArrayList<ArrayList<Double>>
    ): Call<CreateWalkingResultModel>

    @GET("/animal/mypet")
    fun getMyPet(@Query("userToken") userToken: String): Call<MyPetListModel>

    @GET("/walking/static")
    fun getMyWalkingStatic(@Query("userToken") userToken: String): Call<MyWalkingStaticModel>
}