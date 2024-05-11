package com.sample.fitnessvesaglikuygulamasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.sample.fitnessvesaglikuygulamasi.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        // Kullanıcı bilgilerini al ve Ana Sayfayı güncelle
        getUserInfoAndSetupUI()
    }

    private fun getUserInfoAndSetupUI() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return // Kullanıcı oturum açmamışsa çıkış

        db.collection("users").document(currentUserId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject<User>()
                    // Kullanıcı adıyla karşılama mesajını güncelle
                    
                    binding.welcomeTextView.text = if (user?.name != null) {
                        "Merhaba, ${user.email}"
                    } else {
                        "Merhaba" // Veya hata mesajı
                    }

                    // TODO: Hedefleri Firestore'dan al ve RecyclerView'da göster
                } else {
                    // Kullanıcı verisi bulunamadıysa hata mesajı gösterin
                    // ...
                }
            }
            .addOnFailureListener { exception ->
                // Hata oluştuysa hata mesajı gösterin
                // ...
            }
    }
}