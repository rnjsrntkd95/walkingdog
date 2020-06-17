package com.example.walkingdog_kotlin.Login.Model

data class LoginModel (
    val loginToken: String,
    val nickname: String,
    val error: Number
)

data class MyProfileModel (
    val error: Int,
    val nickname: String,
    val profile: String,
    val myPetList: ArrayList<PetModel> = arrayListOf()
)

data class PetModel (
    val animalName: String

)