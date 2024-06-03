package com.sample.fitnessvesaglikuygulamasi

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SavedProgramsActivity : AppCompatActivity() {

    private lateinit var savedProgramsContainer: LinearLayout
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_programs)

        savedProgramsContainer = findViewById(R.id.saved_programs_container)

        MainScope().launch {
            loadSavedPrograms()
        }
    }

    private fun addProgramSeparator() {
        val separator = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2 // Height of the separator line
            ).apply {
                setMargins(0, 16, 0, 16) // Margins for the separator line
            }
            setBackgroundColor(Color.parseColor("#CCCCCC")) // Color of the separator line
        }
        savedProgramsContainer.addView(separator)
    }

    private fun addTextViewToContainer(text: String) {
        val textView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textSize = 15f
            gravity = Gravity.CENTER
            setText(text)
        }
        savedProgramsContainer.addView(textView)
    }

    private suspend fun loadSavedPrograms() {
        val userId = GlobalVariables.currentUser?.id ?: return
        val querySnapshot = db.collection("diet_lists")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING) // Sıralama işlemi burada yapılıyor
            .get().await()

        if (querySnapshot.isEmpty) {
            addTextViewToContainer("Kaydedilen program bulunamadı.")
            return
        }

        val savedPrograms = querySnapshot.documents.map { it.getString("dietList") ?: "Bilinmeyen Diyet Listesi" }
        savedPrograms.forEachIndexed { index, program ->
            addTextViewToContainer(program)
            // Add separator after each program except the last one
            if (index < savedPrograms.size - 1) {
                addProgramSeparator()
            }
        }
    }
}
