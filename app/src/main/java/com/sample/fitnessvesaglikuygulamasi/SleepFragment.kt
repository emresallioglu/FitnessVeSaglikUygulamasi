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
                    sleepTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    sleepTime.set(Calendar.MINUTE, minute)
                    updateTextView(textViewSleepTime, sleepTime)
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
                    wakeTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    wakeTime.set(Calendar.MINUTE, minute)
                    updateTextView(textViewWakeTime, wakeTime)
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

        val sleepData = hashMapOf(
            "userId" to (GlobalVariables.currentUser?.id ?: ""),
            "sleepTime" to formattedSleepTime,
            "wakeTime" to formattedWakeTime
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
