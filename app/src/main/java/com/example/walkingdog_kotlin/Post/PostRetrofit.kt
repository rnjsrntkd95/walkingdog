package com.example.walkingdog_kotlin.Post

import com.example.walkingdog_kotlin.Post.Model.PostListModel
import com.example.walkingdog_kotlin.Post.Model.PostModel
import retrofit2.Call
import retrofit2.http.*

interface PostRetrofit {
    @GET("/post/timeline")
    fun getTimeline(@Query("addressAdmin") addressAdmin: String,
                    @Query("addressLocality") addressLocality: String,
                    @Query("addressThoroughfare") addressThoroughfare: String,
                    @Query ("userToken") userToken: String): Call<PostListModel>
    @FormUrlEncoded
    @POST("/post/create")
    fun createPost(
        @Field("comment") comment: String,
        @Field("userToken") userToken: String,
        @Field("image") image: Unit
    ): Call<PostModel>

}