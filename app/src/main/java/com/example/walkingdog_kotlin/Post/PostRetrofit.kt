package com.example.walkingdog_kotlin.Post

import com.example.walkingdog_kotlin.Animal.Model.BreedListModel
import com.example.walkingdog_kotlin.Post.Model.PostListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PostRetrofit {
    @GET("/post/timeline")
    fun getTimeline(@Query("addressAdmin") addressAdmin: String,
                    @Query("addressLocality") addressLocality: String,
                    @Query("addressThoroughfare") addressThoroughfare: String,
                    @Query ("userToken") userToken: String): Call<PostListModel>

}