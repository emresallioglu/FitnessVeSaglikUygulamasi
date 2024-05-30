package com.sample.fitnessvesaglikuygulamasi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.NumberPicker
import android.widget.Button
import android.widget.Toast

class AddActivityActivity : AppCompatActivity() {

    private lateinit var activitySpinner: Spinner
    private lateinit var hourPicker: NumberPicker
    private lateinit var addButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val activities = mutableListOf<Activity>()
    private lateinit var returnButton: Button // Aktivitelere dön butonu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_activity)

        activitySpinner = findViewById(R.id.activitySpinner)
        hourPicker = findViewById(R.id.hourPicker)
        addButton = findViewById(R.id.addButton)
        returnButton = findViewById(R.id.returnButton)
        hourPicker.minValue = 0
        hourPicker.maxValue = 24

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadActivitiesFromFirestore()

        addButton.setOnClickListener {
            // Saat sıfır olarak seçilmişse, kullanıcıya uyarı göster
            if (hourPicker.value == 0) {
                Toast.makeText(this, "Lütfen saat seçiniz.", Toast.LENGTH_SHORT).show()
            } else {
                saveUserActivityToFirestore()
            }

        }

        returnButton.setOnClickListener {
            finish()
        }

    }

    private fun loadActivitiesFromFirestore() {
        val activitiesCollection = db.collection("activities")

        activitiesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val activity = document.toObject(Activity::class.java)
                    activity.activity_id = document.id
                    activities.add(activity)
                }

                val activityNames = activities.map { it.activity_name }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, activityNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activitySpinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Hata işlemer
            }
    }

    private fun saveUserActivityToFirestore() {
        val selectedActivityName = activitySpinner.selectedItem.toString()
        val selectedHours = hourPicker.value.toFloat()

        val selectedActivity = activities.find { it.activity_name == selectedActivityName }
        val caloriesBurned = selectedActivity?.activity_calory?.times(selectedHours) ?: 0f

        val activityDetail = UserActivity(
            userId = GlobalVariables.currentUser?.id, // Kullanıcının ID'si
            activityId = selectedActivity?.activity_id ?: "",
            caloriesBurned = caloriesBurned
        )

        db.collection("user_activities")
            .add(activityDetail)
            .addOnSuccessListener {
                Toast.makeText(this, "Aktivite kaydedildi!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Hata işleme
            }
    }
}