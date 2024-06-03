package com.sample.fitnessvesaglikuygulamasi

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar

class SleepFragment : Fragment() {

    private lateinit var textViewSleepTime: TextView
    private lateinit var textViewWakeTime: TextView
    private lateinit var buttonSetSleepTime: Button
    private lateinit var buttonSetWakeTime: Button
    private lateinit var buttonSave: Button

    private var sleepTime: Calendar = Calendar.getInstance()
    private var wakeTime: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sleep, container, false)

        textViewSleepTime = view.findViewById(R.id.textViewSleepTime)
        textViewWakeTime = view.findViewById(R.id.textViewWakeTime)
        buttonSetSleepTime = view.findViewById(R.id.buttonSetSleepTime)
        buttonSetWakeTime = view.findViewById(R.id.buttonSetWakeTime)
        buttonSave = view.findViewById(R.id.buttonSave)

        buttonSetSleepTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    // Check if selected time is equal to wakeTime
                    if (selectedTime == wakeTime) {
                        Toast.makeText(context, "Uyku ve uyanma zamanları aynı olamaz!", Toast.LENGTH_SHORT).show()
                    } else {
                        sleepTime = selectedTime
                        updateTextView(textViewSleepTime, sleepTime)
                    }
                },
                sleepTime.get(Calendar.HOUR_OF_DAY),
                sleepTime.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        buttonSetWakeTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    // Check if selected time is equal to sleepTime
                    if (selectedTime == sleepTime) {
                        Toast.makeText(context, "Uyku ve uyanma zamanları aynı olamaz!", Toast.LENGTH_SHORT).show()
                    } else {
                        wakeTime = selectedTime
                        updateTextView(textViewWakeTime, wakeTime)
                    }
                },
                wakeTime.get(Calendar.HOUR_OF_DAY),
                wakeTime.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        buttonSave.setOnClickListener {
            saveSleepTimesToFirestore()
        }

        return view
    }

    private fun updateTextView(textView: TextView, time: Calendar) {
        val formattedTime = SimpleDateFormat("HH:mm").format(time.time)
        textView.text = formattedTime
    }

    private fun saveSleepTimesToFirestore() {
        val formattedSleepTime = SimpleDateFormat("HH:mm").format(sleepTime.time)
        val formattedWakeTime = SimpleDateFormat("HH:mm").format(wakeTime.time)
        val timestamp = FieldValue.serverTimestamp()

        val sleepData = hashMapOf(
            "userId" to (GlobalVariables.currentUser?.id ?: ""),
            "sleepTime" to formattedSleepTime,
            "wakeTime" to formattedWakeTime,
            "timestamp" to timestamp
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("sleep_times")
            .add(sleepData)
            .addOnSuccessListener {
                Toast.makeText(context, "Uyku saatleri kaydedildi!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Uyku saatleri kaydedilirken hata oluştu.", Toast.LENGTH_SHORT).show()
            }
    }
}
