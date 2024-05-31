package com.sample.fitnessvesaglikuygulamasi

import Activity

data class ActivityDetail(
    var activity: Activity? = null,
    var user: User? = null,
    var caloriesBurned: Float = 0f // Yeni özellik
)