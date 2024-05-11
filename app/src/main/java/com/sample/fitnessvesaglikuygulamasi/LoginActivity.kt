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



        binding.loginButton.setOnClickListener {

            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            var userLoggedIn = false

            if (username.isEmpty() || password.isEmpty()) {
                // Display error message
                return@setOnClickListener
            }

            db.collection("users").whereEqualTo("name", username)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val storedPassword = document.getString("password") ?: ""
                        if (password == storedPassword) { // Assuming you have a secure password checking function
                            // Successful login
                            userLoggedIn = true

                            val user = User(
                                id = document.id,
                                name = document.getString("name") ?: "",
                                password = document.getString("password") ?: "",
                                email = document.getString("email") ?: ""
                            )

                            // Store user information, navigate to main app interface
                            Log.d(TAG, "Successful login: $user")
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