package com.example.walkingdog_kotlin.Post.Model

import android.util.Log
import com.example.walkingdog_kotlin.Post.Model.PostModel
import com.example.walkingdog_kotlin.ReformatDate
import java.util.*

class FeedContent() {
    val profileImg : String = ""
    var userName : String = ""
    var date : String = ""
    var location : String = ""
    var dogName : String = ""
    var uploadImg : List<String> = listOf()
    var explain : String = ""
    var time : Number = 0
    var distance : Number = 0
    var calory : Number = 0
    var likes : Number = 0

    constructor(post: PostModel): this() {
        var animalList: String = ""
        for (animalIndex in 0 until post.animal.size) {
            if (animalIndex == post.animal.size - 1) {
                animalList += post.animal[animalIndex]
                continue
            }
            animalList = animalList + post.animal[animalIndex] + ", "
        }

        this.userName = post.nickname
        this.date = ReformatDate("yyyy년 MM월 dd일", post.created_date)
        this.location = post.addressAdmin + " " + post.addressLocality
        this.dogName = animalList
//        this.uploadImg = post.image
        this.explain = post.content
        this.time = post.walkingTime
        this.distance = post.distance
        this.calory = post.calorie
        this.likes = post.like
    }


}