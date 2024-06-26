package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextHeight: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var saveButton: Button
    private lateinit var backToProfileButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editTextName = findViewById(R.id.editTextName)
        editTextSurname = findViewById(R.id.editTextSurname)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextHeight = findViewById(R.id.editTextHeight)
        editTextWeight = findViewById(R.id.editTextWeight)
        saveButton = findViewById(R.id.saveButton)
        backToProfileButton = findViewById(R.id.backToProfileButton)

        loadUserProfile()

        saveButton.setOnClickListener {
            saveUserProfile()
        }

        backToProfileButton.setOnClickListener {
            backToProfile()
        }
    }

    private fun loadUserProfile() {
        val user = GlobalVariables.currentUser
        if (user != null) {
            editTextName.setText(user.name)
            editTextSurname.setText(user.surname)
            editTextEmail.setText(user.email)
            editTextHeight.setText(user.height.toString())
            editTextWeight.setText(user.weight.toString())
        }
    }

    private fun saveUserProfile() {
        val name = editTextName.text.toString()
        val surname = editTextSurname.text.toString()
        val email = editTextEmail.text.toString()
        val height = editTextHeight.text.toString().toDoubleOrNull()
        val weight = editTextWeight.text.toString().toDoubleOrNull()

        // Boş değer kontrolü
        if (name.isBlank() || surname.isBlank() || height == null || weight == null) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        // E-posta format kontrolü
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Lütfen geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show()
            return
        }

        val user = GlobalVariables.currentUser
        if (user != null) {
            val updatedUser = user.copy(
                name = name,
                surname = surname,
                email = email,
                height = height,
                weight = weight
            )

            db.collection("users").document(user.id).set(updatedUser)
                .addOnSuccessListener {
                    GlobalVariables.currentUser = updatedUser
                    Toast.makeText(this, "Profil güncellendi", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Profil güncellenirken hata oluştu", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun backToProfile() {
        finish()
    }

}
