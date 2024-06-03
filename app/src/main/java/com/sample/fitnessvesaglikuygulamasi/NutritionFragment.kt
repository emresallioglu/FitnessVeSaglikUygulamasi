package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.findNavController

class NutritionFragment : Fragment() {

    private lateinit var newNutritionProgram: Button
    private lateinit var viewSavedPrograms: Button
    private lateinit var newDietList: TextView
    private lateinit var titleTextNutrition: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)

        newNutritionProgram = view.findViewById(R.id.generate_diet_button)
        viewSavedPrograms = view.findViewById(R.id.view_saved_programs_button)
        newDietList = view.findViewById(R.id.dietList)
        titleTextNutrition = view.findViewById(R.id.title_text_nutrition)

        newNutritionProgram.setOnClickListener {
            MainScope().launch {
                try {
                    Log.d("NutritionFragment", "Button clicked, starting data load")
                    val user = loadUserData()
                    if (user == null) {
                        Log.e("NutritionFragment", "User data is null")
                        return@launch
                    }
                    val userActivitiesWithNames = loadUserActivitiesWithNames(user.id)
                    if (userActivitiesWithNames.isEmpty()) {
                        Log.e("NutritionFragment", "User activities are empty")
                        return@launch
                    }
                    val userGoals = loadUserGoals(user.id)
                    if (userGoals.isEmpty()) {
                        Log.e("NutritionFragment", "User goals are empty")
                        return@launch
                    }
                    val userSleepTimes = loadUserSleepTimes(user.id)

                    val prompt = createPrompt(user, userActivitiesWithNames, userGoals, userSleepTimes)
                    generateDietList(prompt, user.id)
                } catch (e: Exception) {
                    Log.e("NutritionFragment", "Error: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        viewSavedPrograms.setOnClickListener {
            val intent = Intent(activity, SavedProgramsActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private suspend fun loadUserData(): User? {
        val userId = GlobalVariables.currentUser?.id ?: ""
        if (userId.isEmpty()) {
            Log.e("NutritionFragment", "Current user ID is null or empty")
            return null
        }
        val document = db.collection("users").document(userId).get().await()
        if (!document.exists()) {
            Log.e("NutritionFragment", "User document does not exist for userId: $userId")
            return null
        }
        return document.toObject(User::class.java)
    }

    private suspend fun loadUserActivitiesWithNames(userId: String): List<Pair<UserActivity, String>> {
        val userActivitiesQuerySnapshot = db.collection("user_activities")
            .whereEqualTo("userId", userId)
            .get().await()

        if (userActivitiesQuerySnapshot.isEmpty) {
            Log.e("NutritionFragment", "No user activities found for userId: $userId")
            return emptyList()
        }

        val userActivities = userActivitiesQuerySnapshot.documents.mapNotNull {
            it.toObject(UserActivity::class.java)
        }

        if (userActivities.isEmpty()) {
            Log.e("NutritionFragment", "Failed to convert user activities for userId: $userId")
            return emptyList()
        }

        val activitiesMap = mutableMapOf<String, String>()
        for (activityId in userActivities.map { it.activityId }.distinct()) {
            val activityDocument = db.collection("activities").document(activityId).get().await()
            if (activityDocument.exists()) {
                activitiesMap[activityId] = activityDocument.getString("activity_name") ?: "Unknown Activity"
            }
        }

        return userActivities.mapNotNull {
            val activityName = activitiesMap[it.activityId]
            if (activityName != null) {
                it to activityName
            } else {
                Log.e("NutritionFragment", "Activity name not found for activityId: ${it.activityId}")
                null
            }
        }
    }

    private suspend fun loadUserGoals(userId: String): List<UserGoal> {
        val querySnapshot = db.collection("user_goals")
            .whereEqualTo("userId", userId)
            .get().await()

        if (querySnapshot.isEmpty) {
            Log.e("NutritionFragment", "No user goals found for userId: $userId")
            return emptyList()
        }

        return querySnapshot.documents.mapNotNull { it.toObject(UserGoal::class.java) }
    }

    private suspend fun loadUserSleepTimes(userId: String): Pair<String, String>? {
        val querySnapshot = db.collection("sleep_times")
            .whereEqualTo("userId", userId)
            .get().await()

        if (querySnapshot.isEmpty) {
            Log.e("NutritionFragment", "No sleep times found for userId: $userId")
            return null
        }

        val sleepDocument = querySnapshot.documents.first()
        val sleepTime = sleepDocument.getString("sleepTime") ?: return null
        val wakeTime = sleepDocument.getString("wakeTime") ?: return null
        return Pair(sleepTime, wakeTime)
    }

    private fun createPrompt(
        user: User,
        userActivitiesWithNames: List<Pair<UserActivity, String>>,
        userGoals: List<UserGoal>,
        userSleepTimes: Pair<String, String>?
    ): String {
        val activityDescriptions = userActivitiesWithNames.joinToString { "${it.second}: ${it.first.caloriesBurned} kalori değerinde aktivite." }
        val goalDescriptions = userGoals.joinToString { "${it.goal}: ${it.goal_description} (Tamamlandı mı: ${it.isCompleted}" }
        val sleepDescriptions = if (userSleepTimes != null) {
            "Uyanma Zamanı: ${userSleepTimes.second}, Uyku Zamanı: ${userSleepTimes.first} "
        } else {
            "Uyanma Zamanı: Bilinmiyor, Uyku Zamanı: Bilinmiyor"
        }

        return """
            Kullanıcı Profili:
            İsim, Soyisim: ${user.name} ${user.surname}
            Yaş: ${user.age}
            Boy: ${user.height} cm
            Kilo: ${user.weight} kg
            Cinsiyet: ${user.gender}
            Aktiviteler: $activityDescriptions
            Hedefler: $goalDescriptions
            $sleepDescriptions

            Programları kullanıcının Hedefler'ine göre yaz. Yaş, Boy, Kilo, Cinsiyet bilgilerini de göz önünde bulundur.
            Bu bilgilerle sadece denemelik bir diyet ve egzersiz programı oluşturabilir misin? Egzersiz programını aktivitelerle oluştur. 
            Diyet programını uyku saatleriyle oluştur. Öğün sayısını abartma, diyet programı yaz.
            Sadece programları yaz, başka bilgi yazma. Başlığa "Denemelik" yazma, Diyet Programı ve Egzersiz Programı yaz.
            Hiçbir şekilde not yazma sadece programları göster.
        """.trimIndent()
    }

    private fun generateDietList(prompt: String, userId: String) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyC0lFhRMKt0ZBJXWPIckB7TEKZq8kSORYc"
        )

        MainScope().launch {
            try {
                Log.d("NutritionFragment", "Generating diet list")
                val response = generativeModel.generateContent(prompt)
                newDietList.text = response.text
                titleTextNutrition.visibility = View.VISIBLE // Başlığı görünür yap
                response.text?.let { saveDietListToFirestore(userId, it) }
                Log.d("NutritionFragment", "Diet list generated and saved successfully")
            } catch (e: Exception) {
                Log.e("NutritionFragment", "Error generating diet list: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun saveDietListToFirestore(userId: String, dietList: String) {
        val dietData = hashMapOf(
            "userId" to userId,
            "dietList" to dietList,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("diet_lists")
            .add(dietData)
            .addOnSuccessListener {
                Log.d("NutritionFragment", "Diet list saved to Firestore successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("NutritionFragment", "Error saving diet list to Firestore: ${exception.message}")
            }
    }

    private suspend fun displaySavedPrograms(userId: String) {
        val querySnapshot = db.collection("diet_lists")
            .whereEqualTo("userId", userId)
            .get().await()

        if (querySnapshot.isEmpty) {
            newDietList.text = "No saved programs found."
            return
        }

        val savedPrograms = querySnapshot.documents.map { it.getString("dietList") ?: "Unknown Diet List" }
        newDietList.text = savedPrograms.joinToString(separator = "\n\n") { it }
    }
}
