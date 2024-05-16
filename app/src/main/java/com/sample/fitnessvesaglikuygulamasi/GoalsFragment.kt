package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            /*GlobalScope.launch(Dispatchers.Main) {
                val goalList = GoalServices().getGoalByUserId(GlobalVariables.currentUser?.id ?: "")

                // Test amaçlı, goalList'in içeriğini logcat'e yazdırabiliriz
                goalList.forEach { goal ->
                    println("Goal: ${goal.goal}, Description: ${goal.goal_description}")
                }
            }*/
        }

        return view
    }

}