package com.sample.fitnessvesaglikuygulamasi

data class UserActivity(
    val userId: String? = null,
    val activityId: String = "",
    val caloriesBurned: Float = 0f
){
    // No-argument constructor
    constructor() : this(null, "", 0f)
}