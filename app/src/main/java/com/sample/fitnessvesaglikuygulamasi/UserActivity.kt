package com.sample.fitnessvesaglikuygulamasi

data class UserActivity(
    val userId: String? = null,
    val activityId: String = "",
    val caloriesBurned: Float = 0f
)