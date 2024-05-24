package com.sample.fitnessvesaglikuygulamasi

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.fitnessvesaglikuygulamasi.databinding.ActivityRegisterBinding
import java.util.UUID

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        // Gender Spinner setup
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = genderAdapter

        binding.registerButton.setOnClickListener {
            val userName = binding.usernameEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val surName = binding.surnameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val height = binding.heightEditText.text.toString().toDoubleOrNull() ?: 0.0
            val weight = binding.weightEditText.text.toString().toDoubleOrNull() ?: 0.0
            val gender = binding.genderSpinner.selectedItem.toString()
            //val age = binding.ageEditText.text.toString().toIntOrNull() ?: 0 // Yaş alanı eklendi

            // Giriş doğrulamasını yapın (Boş alan kontrolü, email formatı vb.)
            // ...

            val user = User("", userName, name, surName,20, email, password, height, weight, gender) // Yaş alanı eklendi

            val myUuid = UUID.randomUUID()
            val myUuidAsString = myUuid.toString()

            db.collection("users").document(myUuidAsString)
                .set(user)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // RegisterActivity'yi kapatmak için
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error writing document", e)
                    // Başarısızlık durumunda gerekli işlemler yapılabilir
                }

        }
        // TODO: Kayıt ol butonuna tıklandığında Firebase ile kullanıcı oluşturulacak
    }
}
