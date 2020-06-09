package com.example.walkingdog_kotlin.Post

import com.example.walkingdog_kotlin.Post.Model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PostRetrofit {
    @GET("/post/timeline")
    fun getTimeline(@Query("addressAdmin") addressAdmin: String,
                    @Query("addressLocality") addressLocality: String,
                    @Query("addressThoroughfare") addressThoroughfare: String): Call<PostListModel>

    @GET("/walking/route")
    fun getRoute(@Query("walkingId") walkingId: String): Call<RouteModel>

    @Multipart
    @POST("/post/create")
    fun createPost(@Header("userToken") userToken: String,
                   @Part("comment") comment: RequestBody,
                   @Part images: ArrayList<MultipartBody.Part>): Call<CreatePostModel>

    @DELETE("post/delete")
    fun deletePost(@Field("userToken") userToken: String,
                   @Field("postId") postId: String): Call<DeletePostModel>




}