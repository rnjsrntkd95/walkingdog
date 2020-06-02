package com.example.walkingdog_kotlin.Post.Model

import java.util.*

data class PostListModel (
    val posts: List<PostModel> = listOf(),
    val error: Number
)

data class PostModel (
    val like: Int,
    val calorie: Number,
    val distance: Number,
    val animal: List<String> = listOf(),
    val id: String,
    val content: String,
    val image: List<String> = listOf(),
    val routeImage: String,
    val walkingTime: Number,
    val addressAdmin: String,
    val addressLocality: String,
    val addressThoroughfare: String,
    val user: String,
    val nickname: String,
    val created_date: Date
)