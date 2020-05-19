package com.example.walkingdog_kotlin.Animal.Model

data class BreedListModel (
    val breedList: List<BreedObjectModel> = listOf()
)

data class BreedObjectModel (
    val _id: String,
    val breed : String
)
