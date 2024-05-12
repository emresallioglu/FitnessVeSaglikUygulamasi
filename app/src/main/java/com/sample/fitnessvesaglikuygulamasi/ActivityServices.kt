package com.sample.fitnessvesaglikuygulamasi

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ActivityServices {
    suspend fun getActivities(): List<Activity> {

        val activities = mutableListOf<Activity>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("activities").get().await()
        for (document in querySnapshot.documents) {
            val activity = document.toObject(Activity::class.java)
            activity?.let {
                activities.add(it)
            }
        }
        return activities
    }
}