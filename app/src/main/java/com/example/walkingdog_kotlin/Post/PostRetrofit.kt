package com.example.walkingdog_kotlin.Post

import com.example.walkingdog_kotlin.Post.Model.DeletePostModel
import com.example.walkingdog_kotlin.Post.Model.PostListModel
import com.example.walkingdog_kotlin.Post.Model.PostModel
import com.example.walkingdog_kotlin.Post.Model.RouteModel
import retrofit2.Call
import retrofit2.http.*

interface PostRetrofit {
    @GET("/post/timeline")
    fun getTimeline(@Query("addressAdmin") addressAdmin: String,
                    @Query("addressLocality") addressLocality: String,
                    @Query("addressThoroughfare") addressThoroughfare: String): Call<PostListModel>

    @GET("/walking/route")
    fun getRoute(@Query("walkingId") walkingId: String): Call<RouteModel>

    @FormUrlEncoded
    @POST("/post/create")
    fun createPost(@Field("comment") comment: String,
                   @Field("userToken") userToken: String,
                   @Field("image") image: Unit): Call<PostModel>

    @DELETE("post/delete")
    fun deletePost(@Field("userToken") userToken: String,
                   @Field("postId") postId: String): Call<DeletePostModel>




}