package com.sample.fitnessvesaglikuygulamasi


data class User(
    val id: String = "",
    val userName: String = "",
    val name: String="",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val height: Double,
    val weight: Double,
    val gender: String
)