package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class GoalsFragment : Fragment() {

    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        // Button tanımlama
        addButton = view.findViewById(R.id.add_button)

        // Button click listener
        addButton.setOnClickListener {
            // Hedef activity'nin intenti
            val intent = Intent(activity, AddGoalActivity::class.java)
            // Ekstra veri göndermek için
            intent.putExtra("anahtar", "değer")
            // Activity'yi başlat
            startActivity(intent)
        }

        return view
    }

}