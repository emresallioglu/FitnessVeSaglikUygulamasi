package com.sample.fitnessvesaglikuygulamasi

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar

class AddGoalActivity : AppCompatActivity() {

    private lateinit var editTextGoal: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button

    private var calendar1: Calendar = Calendar.getInstance()
    private var calendar2: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_goal)

        editTextGoal = findViewById(R.id.editTextGoal)
        editTextDescription = findViewById(R.id.editTextDescription)
        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)

        button1.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(Calendar.YEAR, year)
                    selectedCalendar.set(Calendar.MONTH, monthOfYear)
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    if (selectedCalendar.timeInMillis >= System.currentTimeMillis()) {
                        calendar1 = selectedCalendar
                        updateTextView1()
                    } else {
                        Toast.makeText(this, "Başlangıç tarihi bugünden önce olamaz.", Toast.LENGTH_SHORT).show()
                    }
                },
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        button2.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(Calendar.YEAR, year)
                    selectedCalendar.set(Calendar.MONTH, monthOfYear)
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    if (selectedCalendar.timeInMillis >= calendar1.timeInMillis) {
                        calendar2 = selectedCalendar
                        updateTextView2()
                    } else {
                        Toast.makeText(this, "Bitiş tarihi başlangıç tarihinden önce olamaz.", Toast.LENGTH_SHORT).show()
                    }
                },
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            if (calendar1.timeInMillis < System.currentTimeMillis()) {
                Toast.makeText(this, "Başlangıç tarihi bugünden önce olamaz.", Toast.LENGTH_SHORT).show()
            } else if (calendar2.timeInMillis < calendar1.timeInMillis) {
                Toast.makeText(this, "Bitiş tarihi başlangıç tarihinden önce olamaz.", Toast.LENGTH_SHORT).show()
            } else {
                saveUserGoalToFirestore()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateTextView1() {
        val formattedDate1 = SimpleDateFormat("dd/MM/yyyy").format(calendar1.time)
        textView1.text = "Seçilen Tarih 1: $formattedDate1"
    }

    private fun updateTextView2() {
        val formattedDate2 = SimpleDateFormat("dd/MM/yyyy").format(calendar2.time)
        textView2.text = "Seçilen Tarih 2: $formattedDate2"
    }

    private fun saveUserGoalToFirestore() {
        val goal = editTextGoal.text.toString()
        val goalDescription = editTextDescription.text.toString()
        val formattedStartDate = SimpleDateFormat("yyyy-MM-dd").format(calendar1.time)
        val formattedEndDate = SimpleDateFormat("yyyy-MM-dd").format(calendar2.time)

        val userGoal = UserGoal(
            userId = GlobalVariables.currentUser?.id,
            goal = goal,
            goal_description = goalDescription,
            startDate = SimpleDateFormat("yyyy-MM-dd").parse(formattedStartDate),
            endDate = SimpleDateFormat("yyyy-MM-dd").parse(formattedEndDate),
            isCompleted = false
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("user_goals")
            .add(userGoal)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Hedef kaydedildi!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Hedef kaydedilirken hata oluştu.", Toast.LENGTH_SHORT).show()
                Log.e("SaveGoal", "Error saving user goal:", exception)
            }
    }

}


