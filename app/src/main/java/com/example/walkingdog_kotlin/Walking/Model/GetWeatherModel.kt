package com.example.walkingdog_kotlin.Walking.Model

data class WeatherAPIModel(
    val weather: List<WeatherModel> = listOf()
)

data class WeatherModel(
    val main: String
)