package com.example.walkingdog_kotlin.model

import com.example.walkingdog_kotlin.Post.Model.PostModel
import java.util.*

class FeedContent(val post: PostModel) {
//    val profileImg : String = post.image
    val userName : String = post.nickname
    val date : Date = post.created_date
    val location : String = post.location
    val dogName : String = post.animal.toString()
    val uploadImg : String = post.image
    val explain : String = post.content
    val time : Number = post.walkingTime
    val distance : Number = post.distance
    val calory : Number = post.calorie
    val likes : Number = post.like

}