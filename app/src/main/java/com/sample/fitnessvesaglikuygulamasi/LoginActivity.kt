package com.sample.fitnessvesaglikuygulamasi

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sample.fitnessvesaglikuygulamasi.databinding.ActivityLoginBinding

data class Activity(
    var activity_id: String = "",
    val activity_name: String = "",
    val activity_description: String = "",
    val activity_calory: Float = 0f,
    val activity_video_url: String = ""  // Yeni alan eklendi
)

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Firebase.firestore

        // Kayıt ol TextView'ine tıklandığında RegisterActivity'ye geçiş
        binding.registerTextView.setOnClickListener {
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

            db.collection("users").whereEqualTo("userName", username)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val storedPassword = document.getString("password") ?: ""
                        if (password == storedPassword) {
                            // Successful login
                            userLoggedIn = true

                            GlobalVariables.currentUser = User(
                                id = document.id,
                                userName = document.getString("userName") ?: "",
                                name = document.getString("name") ?: "",
                                surname = document.getString("surname") ?: "",
                                email = document.getString("email") ?: "",
                                password = document.getString("password") ?: "",
                                height = document.getDouble("height") ?: 0.0,
                                weight = document.getDouble("weight") ?: 0.0,
                                gender = document.getString("gender") ?: "",
                                age = document.getLong("age")?.toInt() ?: 0
                            )

                            // Store user information, navigate to main app interface
                            Log.d(TAG, "Successful login: ${GlobalVariables.currentUser}")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            break
                        }
                    }
                    if (!userLoggedIn) {
                        // Handle failed login
                        Log.d(TAG, "Login failed: Invalid username or password")
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle database error
                    Log.w(TAG, "Error getting documents", exception)
                }
        }
    }
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

/*binding.button.setOnClickListener {
            val db = FirebaseFirestore.getInstance()

            val activities = listOf(
                Activity("", "Yürüyüş", "Hafif tempolu yürüyüş, genel sağlığı ve zindeliği artırır.", 300f, "https://www.youtube.com/watch?v=QxaKQG1i0t4"),
                Activity("", "Koşu", "Orta tempolu koşu, kardiyovasküler sağlığı güçlendirir.", 600f, "https://www.youtube.com/watch?v=fis26HvvDII"),
                Activity("", "Bisiklet Sürme", "Orta tempolu bisiklet sürme, bacak kaslarını güçlendirir.", 500f, "https://www.youtube.com/watch?v=1VtMzty4igY"),
                Activity("", "Yüzme", "Serbest stil yüzme, tüm vücut kaslarını çalıştırır ve kalori yakar.", 700f, "https://www.youtube.com/watch?v=a_G9Naeclk4"),
                Activity("", "Boks", "Antrenman sırasında boks yapma, dayanıklılığı ve kuvveti artırır.", 800f, "https://www.youtube.com/watch?v=sFbx2FJ4YYM"),
                Activity("", "Barfiks", "Barfiks çekme, üst vücut kaslarını güçlendirir.", 400f, "https://www.youtube.com/watch?v=eGo4IYlbE5g"),
                Activity("", "Yoga", "Rahatlatıcı yoga egzersizleri, esnekliği ve zihinsel sağlığı artırır.", 200f, "https://www.youtube.com/watch?v=v7AYKMP6rOE"),
                Activity("", "Basketbol", "Orta tempolu basketbol oynamak, kardiyo ve koordinasyon yeteneklerini geliştirir.", 400f, "https://www.youtube.com/watch?v=bUUM73xzbH4"),
                Activity("", "Futbol", "Orta tempolu futbol oynamak, dayanıklılığı ve takım çalışmasını artırır.", 500f, "https://www.youtube.com/watch?v=vvLCXgCkxb0"),
                Activity("", "Voleybol", "Orta tempolu voleybol oynamak, refleksleri ve koordinasyonu artırır.", 300f, "https://www.youtube.com/watch?v=MOqnvRt7CSU"),
                Activity("", "Plank", "Karın kaslarını güçlendirmek için plank yapma, çekirdek kasları hedefler.", 250f, "https://www.youtube.com/watch?v=pSHjTRCQxIw"),
                Activity("", "Squat", "Diz ve kalça kaslarını çalıştırmak için squat yapma, bacak gücünü artırır.", 350f, "https://www.youtube.com/watch?v=aclHkVaku9U"),
                Activity("", "Push-up", "Göğüs, kol ve omuz kaslarını çalıştırmak için push-up yapma.", 200f, "https://www.youtube.com/watch?v=_l3ySVKYVJ8"),
                Activity("", "Jumping Jacks", "Kardiyo yapmak için jumping jacks yapma, hızlı kalori yakar.", 400f, "https://www.youtube.com/watch?v=UpH7rm0cYbM"),
                Activity("", "Sit-up", "Karın kaslarını güçlendirmek için sit-up yapma, çekirdek kasları hedefler.", 250f, "https://www.youtube.com/watch?v=1fbU_MkV7NE"),
                Activity("", "Burpee", "Tüm vücudu çalıştırmak için burpee yapma, yüksek yoğunluklu kardiyo sağlar.", 500f, "https://www.youtube.com/watch?v=TU8QYVW0gDU"),
                Activity("", "Leg Raise", "Alt karın kaslarını çalıştırmak için leg raise yapma.", 300f, "https://www.youtube.com/watch?v=JB2oyawG9KI"),
                Activity("", "Mountain Climber", "Kardiyo ve karın kaslarını çalıştırmak için mountain climber yapma.", 450f, "https://www.youtube.com/watch?v=nmwgirgXLYM"),
                Activity("", "Lunge", "Bacak kaslarını güçlendirmek için lunge yapma.", 300f, "https://www.youtube.com/watch?v=QOVaHwm-Q6U"),
                Activity("", "Deadlift", "Bel, sırt ve bacak kaslarını çalıştırmak için deadlift yapma.", 400f, "https://www.youtube.com/watch?v=r4MzxtBKyNE")
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