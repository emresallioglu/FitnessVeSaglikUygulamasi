package com.sample.fitnessvesaglikuygulamasi

import java.util.Date

data class UserGoal(
    var userId: String? = null,
    val goal: String = "",
    val goal_description: String = "",
    val startDate: Date? = null,
    val endDate: Date? = null,
    val isCompleted: Boolean = false
)
