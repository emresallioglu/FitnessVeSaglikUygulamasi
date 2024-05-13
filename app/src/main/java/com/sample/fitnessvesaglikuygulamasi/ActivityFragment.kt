package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.runBlocking

class ActivityFragment : Fragment() {

    private lateinit var addButton: Button
    val activityServices = ActivityServices()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        // Button tanımlama
        val button = view.findViewById<Button>(R.id.add_button)

// Button click listener
        button.setOnClickListener {
            // Hedef activity'nin intenti
            /*val intent = Intent(activity, AddActivityActivity::class.java)

            // Ekstra veri göndermek için
            intent.putExtra("anahtar", "değer")

            // Activity'yi başlat
            startActivity(intent)*/

            runBlocking {
                val activityDetails = activityServices.getActivityByUserId(GlobalVariables.currentUser?.id
                    ?: "")
            }
        }

        return view
    }



}