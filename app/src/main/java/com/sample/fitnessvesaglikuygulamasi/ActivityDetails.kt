package com.sample.fitnessvesaglikuygulamasi

data class ActivityDetail(
    var activity: Activity? = null,
    var user: User? = null,
    var caloriesBurned: Float = 0f // Yeni özellik
)