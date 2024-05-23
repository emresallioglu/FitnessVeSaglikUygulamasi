package com.sample.fitnessvesaglikuygulamasi

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.sample.fitnessvesaglikuygulamasi.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Firebase.firestore

        // Kayıt ol butonuna tıklandığında RegisterActivity'ye geçiş
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)


        }

        /*binding.button.setOnClickListener{
            val db = FirebaseFirestore.getInstance()

            val activities = listOf(
                Activity("Yürüyüş", "Hafif tempolu yürüyüş", 300f),
                Activity("Koşu", "Orta tempolu koşu", 600f),
                Activity("Bisiklet Sürme", "Orta tempolu bisiklet sürme", 500f),
                Activity("Yüzme", "Serbest stil yüzme", 700f),
                Activity("Boks", "Antrenman sırasında boks yapma", 800f),
                Activity("Barfiks", "Barfiks çekme", 400f),
                Activity("Yoga", "Rahatlatıcı yoga egzersizleri", 200f),
                Activity("Basketbol", "Orta tempolu basketbol oynamak", 400f),
                Activity("Futbol", "Orta tempolu futbol oynamak", 500f),
                Activity("Voleybol", "Orta tempolu voleybol oynamak", 300f),
                Activity("Plank", "Karın kaslarını güçlendirmek için plank yapma", 250f),
                Activity("Squat", "Diz ve kalça kaslarını çalıştırmak için squat yapma", 350f),
                Activity("Push-up", "Göğüs, kol ve omuz kaslarını çalıştırmak için push-up yapma", 200f),
                Activity("Jumping Jacks", "Kardiyo yapmak için jumping jacks yapma", 400f),
                Activity("Sit-up", "Karın kaslarını güçlendirmek için sit-up yapma", 250f),
                Activity("Burpee", "Tüm vücudu çalıştırmak için burpee yapma", 500f),
                Activity("Leg Raise", "Alt karın kaslarını çalıştırmak için leg raise yapma", 300f),
                Activity("Mountain Climber", "Kardiyo ve karın kaslarını çalıştırmak için mountain climber yapma", 450f),
                Activity("Lunge", "Bacak kaslarını güçlendirmek için lunge yapma", 300f),
                Activity("Deadlift", "Bel, sırt ve bacak kaslarını çalıştırmak için deadlift yapma", 400f)
            )

            for (activity in activities) {
                db.collection("activities")
                    .add(activity)
                    .addOnSuccessListener { documentReference ->
                        println("Activity added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding activity: $e")
                    }
            }
        }*/



        binding.loginButton.setOnClickListener {

            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            var userLoggedIn = false

            if (username.isEmpty() || password.isEmpty()) {
                // Display error message
                return@setOnClickListener
            }

            db.collection("users").whereEqualTo("userName", username)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val storedPassword = document.getString("password") ?: ""
                        if (password == storedPassword) { // Assuming you have a secure password checking function
                            // Successful login
                            userLoggedIn = true

                            GlobalVariables.currentUser = User(
                                id = document.id,
                                userName = document.getString("username") ?: "",
                                name = document.getString("name") ?: "",
                                surname = document.getString("surname") ?: "",
                                email = document.getString("email") ?: "",
                                password = document.getString("password") ?: "",
                                height = document.getDouble("height") ?: 0.0,
                                weight = document.getDouble("weight") ?: 0.0,
                                gender = document.getString("gender") ?: ""
                            )

                            // Store user information, navigate to main app interface
                            Log.d(TAG, "Successful login: ${GlobalVariables.currentUser}")
                            // ...
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            break // Exit loop after successful login
                        }
                    }
                    if (!userLoggedIn) {
                        // Handle failed login
                        Log.d(TAG, "Login failed: Invalid username or password")
                        // ...
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle database error
                    Log.w(TAG, "Error getting documents", exception)
                    // ...
                }
        }

    }

}