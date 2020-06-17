package com.example.walkingdog_kotlin.Challenge.Model

import android.provider.ContactsContract
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

data class ChallengeUserListModel (
    val records: ArrayList<user> = arrayListOf(),
    val myRecord: user,
    val error: Number
)

// userId -> nickname을 가져오는 방식으로 바꿔야함.
data class user (
    val walkingCount: Number,
    val walkingAvg: Number,
    val score: Int,
    val user: UserNicknameModel,
    val challenge: String,
    val challengeTitle: String
)
data class UserNicknameModel (
    val nickname: String
)
data class nickname(
    val _id: String,
    val nickname: String
)
data class myChallengeId(
    val myChallenge: String
)

data class CreateChallengeResultModel (
    val error: Int,
    val challengeId: String
)

data class JoinChallengeResultModel (
    val error: Int
)

data class DropChallengeResultModel (
    val error: Int
)