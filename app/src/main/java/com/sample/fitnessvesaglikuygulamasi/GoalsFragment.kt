package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class GoalsFragment : Fragment() {

    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        db = FirebaseFirestore.getInstance()

        recyclerView = view.findViewById(R.id.goals_recycler_view)
        addButton = view.findViewById(R.id.add_button)

        recyclerView.layoutManager = LinearLayoutManager(context)
        goalsAdapter = GoalsAdapter(listOf())
        recyclerView.adapter = goalsAdapter

        addButton.setOnClickListener {
            val intent = Intent(activity, AddGoalActivity::class.java)
            startActivity(intent)
        }

        loadGoals()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadGoals() // Activity tekrar görünür olduğunda hedefleri yeniden yükleyin
    }

    private fun loadGoals() {
        val userId = GlobalVariables.currentUser?.id ?: return
        db.collection("user_goals")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val goals = documents.map { document ->
                    document.toObject(UserGoal::class.java).apply { this.userId = document.id }
                }
                goalsAdapter.updateGoals(goals)
            }
            .addOnFailureListener { exception ->
                // Hata durumunu yönetin
            }
    }
}
