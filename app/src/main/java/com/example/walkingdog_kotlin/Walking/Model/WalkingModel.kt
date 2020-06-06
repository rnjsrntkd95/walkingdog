package com.example.walkingdog_kotlin.Walking.Model

data class CreateWalkingResultModel (
    val error: Int,
    val walkingId: String
)

data class MyPetListModel (
    val error: Int,
    val myPetList: ArrayList<MyPetModel>
)

data class MyPetModel (
    val animalName: String,
    val age: Int,
    val weight: Double,
    val breed: String
)