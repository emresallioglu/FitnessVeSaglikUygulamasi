package com.sample.fitnessvesaglikuygulamasi

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
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
import android.widget.TextView

class AddActivityActivity : AppCompatActivity() {

    private lateinit var activitySpinner: Spinner
    private lateinit var hourPicker: NumberPicker
    private lateinit var addButton: Button
    private lateinit var returnButton: Button
    private lateinit var activityDescriptionTextView: TextView
    private lateinit var activityCaloryTextView: TextView
    private lateinit var activityVideoWebView: WebView
    private val db = FirebaseFirestore.getInstance()
    private val activities = mutableListOf<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_activity)

        activitySpinner = findViewById(R.id.activitySpinner)
        hourPicker = findViewById(R.id.hourPicker)
        addButton = findViewById(R.id.addButton)
        returnButton = findViewById(R.id.returnButton)
        activityDescriptionTextView = findViewById(R.id.activityDescriptionTextView)
        activityCaloryTextView = findViewById(R.id.activityCaloryTextView)
        activityVideoWebView = findViewById(R.id.activityVideoWebView)

        hourPicker.minValue = 0
        hourPicker.maxValue = 24

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadActivitiesFromFirestore()

        addButton.setOnClickListener {
            if (hourPicker.value == 0) {
                Toast.makeText(this, "Lütfen saat seçiniz.", Toast.LENGTH_SHORT).show()
            } else {
                saveUserActivityToFirestore()
            }
        }

        returnButton.setOnClickListener {
            finish()
        }

        activitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedActivity = activities[position]
                activityDescriptionTextView.text = selectedActivity.activity_description
                activityCaloryTextView.text = getString(R.string.calory_format, selectedActivity.activity_calory)

                // Embed URL'yi kullanarak videoyu tam ekran olarak yükle
                val videoId = selectedActivity.activity_video_url.split("v=")[1] // Video ID'yi URL'den çıkar
                val embedUrl = "https://www.youtube.com/embed/$videoId"

                activityVideoWebView.settings.javaScriptEnabled = true
                activityVideoWebView.webViewClient = WebViewClient()
                activityVideoWebView.loadUrl(embedUrl)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Bir şey yapılmayacak
            }
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
                // Handle error
            }
    }

    private fun saveUserActivityToFirestore() {
        val selectedActivityName = activitySpinner.selectedItem.toString()
        val selectedHours = hourPicker.value.toFloat()

        val selectedActivity = activities.find { it.activity_name == selectedActivityName }
        val caloriesBurned = selectedActivity?.activity_calory?.times(selectedHours) ?: 0f

        val activityDetail = UserActivity(
            userId = GlobalVariables.currentUser?.id,
            activityId = selectedActivity?.activity_id ?: "",
            caloriesBurned = caloriesBurned
        )

        db.collection("user_activities")
            .add(activityDetail)
            .addOnSuccessListener {
                Toast.makeText(this, "Aktivite kaydedildi!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }
}
