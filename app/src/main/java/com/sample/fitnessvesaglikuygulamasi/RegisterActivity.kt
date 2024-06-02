package com.sample.fitnessvesaglikuygulamasi

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
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
            var userName = binding.usernameEditText.text.toString().trim()
            val name = binding.nameEditText.text.toString()
            val surName = binding.surnameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val height = binding.heightEditText.text.toString().toDoubleOrNull() ?: 0.0
            val weight = binding.weightEditText.text.toString().toDoubleOrNull() ?: 0.0
            val gender = binding.genderSpinner.selectedItem.toString()
            val ageText = binding.ageEditText.text.toString()

            // Yaş alanı için kontrol
            val age = if (ageText.isNotEmpty()) {
                ageText.toIntOrNull() ?: 0
            } else {
                0
            }

            // Giriş doğrulamasını yapın (Boş alan kontrolü, email formatı vb.)
            if (userName.isEmpty() || name.isEmpty() || surName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                // Gerekli alanlar boşsa kullanıcıyı uyar
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Email formatı uygun değilse kullanıcıyı uyar
                Toast.makeText(this, "Lütfen geçerli bir email adresi girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8) {
                // Şifre en az 8 karakterli olmalıdır
                Toast.makeText(this, "Şifreniz en az 8 karakterden oluşmalıdır", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Diğer giriş kontrollerini ekleyebilirsiniz.

            // Kullanıcı adının veritabanında mevcut olup olmadığını kontrol et
            db.collection("users")
                .whereEqualTo("userName", userName)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Kullanıcı adı veritabanında mevcutsa kullanıcıyı uyar
                        Toast.makeText(this, "Bu kullanıcı adı zaten kullanımda, lütfen farklı bir kullanıcı adı seçin", Toast.LENGTH_SHORT).show()
                    } else {
                        // Kullanıcı adı veritabanında mevcut değilse kullanıcıyı kaydeder
                        val user = User("", userName, name, surName, age, email, password, height, weight, gender)

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
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                    // Hata durumunda gerekli işlemler yapılabilir
                }
        }



        // TODO: Kayıt ol butonuna tıklandığında Firebase ile kullanıcı oluşturulacak
    }
}
