package com.example.walkingdog_kotlin.Challenge.Model

import java.util.*

data class ChallengeListModel (
    val challenges: List<ChallengeModel> = listOf(),
    val popularChallenge: ChallengeModel,
    val error: Number,
    val msg: String
)

data class ChallengeModel (
    val date: Int,
    val population: Int,
    val populationLimit: Int,
    val _id: String,
    val title: String,
    val content: String,
    val start_date: Date,
    val end_date: Date,
    val producer: String,
    val create_date: Date,
    val breed: String
)