package com.sample.fitnessvesaglikuygulamasi


data class User(
    val id: String = "",
    val userName: String = "",
    val name: String="",
    val surname: String = "",
    val age: Int,
    val email: String = "",
    val password: String = "",
    val height: Double,
    val weight: Double,
    val gender: String
){
    // Secondary constructor without arguments
    constructor() : this("", "", "", "", 0, "", "", 0.0, 0.0, "")
}