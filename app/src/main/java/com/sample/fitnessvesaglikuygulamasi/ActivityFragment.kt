package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.runBlocking

class ActivityFragment : Fragment() {
    private lateinit var addButton: Button
    val activityServices = ActivityServices()
    private lateinit var tableLayout: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        // Button tanımlama
        addButton = view.findViewById(R.id.add_button)
        tableLayout = view.findViewById(R.id.activityTable) // Tabloyu tanımla

        // Button click listener
        addButton.setOnClickListener {
            // Hedef activity'nin intenti
            val intent = Intent(activity, AddActivityActivity::class.java)
            // Ekstra veri göndermek için
            intent.putExtra("anahtar", "değer")
            // Activity'yi başlat
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        updateTable() // onResume'da tabloyu güncelle
    }

    private fun updateTable() {
        tableLayout.removeAllViews() // Önceki verileri temizle
        runBlocking {
            val activityDetails = activityServices.getActivityByUserId(GlobalVariables.currentUser?.id.toString())
            for (activityDetail in activityDetails) {
                val tableRow = TableRow(context)
                val nameTextView = TextView(context)
                nameTextView.text = activityDetail.activity?.activity_name
                val caloriesTextView = TextView(context)
                caloriesTextView.text = activityDetail.caloriesBurned.toString()
                tableRow.addView(nameTextView)
                tableRow.addView(caloriesTextView)
                tableLayout.addView(tableRow)
            }
        }
    }
}