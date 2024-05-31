package com.sample.fitnessvesaglikuygulamasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NutritionFragment : Fragment() {

    private lateinit var newNutritionProgram: Button
    private lateinit var newDietList: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)

        newNutritionProgram = view.findViewById(R.id.generate_diet_button)
        newDietList = view.findViewById(R.id.dietList)

        newNutritionProgram.setOnClickListener {
            MainScope().launch {
                try {
                    val user = loadUserData()
                    val userActivities = loadUserActivities(user.id)
                    val userGoals = loadUserGoals(user.id)

                    val prompt = createPrompt(user, userActivities, userGoals)
                    generateDietList(prompt)
                } catch (e: Exception) {
                    // Handle exception
                    e.printStackTrace()
                }
            }
        }

        return view
    }

    private suspend fun loadUserData(): User {
        val userId = GlobalVariables.currentUser?.id ?: ""
        val document = db.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)!!
    }

    private suspend fun loadUserActivities(userId: String): List<UserActivity> {
        val querySnapshot = db.collection("user_activities")
            .whereEqualTo("userId", userId)
            .get().await()
        return querySnapshot.documents.map { it.toObject(UserActivity::class.java)!! }
    }

    private suspend fun loadUserGoals(userId: String): List<UserGoal> {
        val querySnapshot = db.collection("user_goals")
            .whereEqualTo("userId", userId)
            .get().await()
        return querySnapshot.documents.map { it.toObject(UserGoal::class.java)!! }
    }



    private fun createPrompt(
        user: User,
        userActivities: List<UserActivity>,
        userGoals: List<UserGoal>,
    ): String {
        val activityDescriptions = userActivities.joinToString { "${it.activityId}: ${it.caloriesBurned} calories burned" }
        val goalDescriptions = userGoals.joinToString { "${it.goal}: ${it.goal_description} (Completed: ${it.isCompleted})" }

        return """
            Kullanıcı Profili:
            İsim, Soyisim: ${user.name} ${user.surname}
            Yaş: ${user.age}
            Boy: ${user.height} cm
            Kilo: ${user.weight} kg
            Cinsiyet: ${user.gender}
            Aktiviteler: $activityDescriptions
            Hedefler: $goalDescriptions

            Bu bilgilerle sadece denemelik bir diyet ve egzersiz programı oluşturabilir misin?
            Sade programları yaz, başka bilgi yazma. Başlığa "Denemelik" yaz, Diyet Programı ve Egzersiz Programı yaz.
        """.trimIndent()
    }

    private fun generateDietList(prompt: String) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyC0lFhRMKt0ZBJXWPIckB7TEKZq8kSORYc"
        )

        MainScope().launch {
            val response = generativeModel.generateContent(prompt)
            newDietList.text = response.text
        }
    }
}
