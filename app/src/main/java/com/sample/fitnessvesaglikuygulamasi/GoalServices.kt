package com.sample.fitnessvesaglikuygulamasi

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GoalServices {


    suspend fun getGoalByUserId(userId: String): List<UserGoal> {
        val goalDetail = mutableListOf<UserGoal>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("user_goals")
            .whereEqualTo("userId", userId).get().await()

        for (document in querySnapshot.documents) {
                val userGoal = document.toObject(UserGoal::class.java)
                if ((userGoal?.userId == GlobalVariables.currentUser?.id)) {

                    var newGoalDetail = UserGoal(
                        userId = GlobalVariables.currentUser?.id,
                        goal = userGoal?.goal ?: "",
                        goal_description = userGoal?.goal_description ?: "",
                        startDate = userGoal!!.startDate,
                        endDate = userGoal!!.endDate,
                    )

                    goalDetail.add(newGoalDetail)
                }
        }
        return goalDetail
    }
}