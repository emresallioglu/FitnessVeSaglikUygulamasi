package com.sample.fitnessvesaglikuygulamasi

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ActivityServices {
    suspend fun getActivities(): List<Activity> {

        val activities = mutableListOf<Activity>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("activities").get().await()
        for (document in querySnapshot.documents) {
            val activity = document.toObject(Activity::class.java)
            activity!!.activity_id = document.id
            activity?.let {
                activities.add(it)
            }
        }
        return activities
    }

    suspend fun getActivityByUserId(userId: String): List<ActivityDetail> {
        val activities = this.getActivities()
        val activityDetails = mutableListOf<ActivityDetail>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("user_activities")
            .whereEqualTo("userId", userId).get().await()

        for (document in querySnapshot.documents) {
            for (activity in activities) {
                val userActivity = document.toObject(UserActivity::class.java)
                if ((activity.activity_id == userActivity!!.activityId) &&
                    (userActivity.userId == GlobalVariables.currentUser?.id)) {

                    // caloriesBurned değerini ata
                    var newActivityDetail = ActivityDetail(
                        activity = activity,
                        user = GlobalVariables.currentUser,
                        caloriesBurned = userActivity.caloriesBurned // Yeni özellik
                    )

                    activityDetails.add(newActivityDetail)
                }
            }
        }
        return activityDetails
    }
}