package com.sample.fitnessvesaglikuygulamasi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class GoalsAdapter(private var goals: List<UserGoal>) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalText: TextView = itemView.findViewById(R.id.goal_text)
        val goalDescriptionText: TextView = itemView.findViewById(R.id.goal_description_text)
        val goalStatusText: TextView = itemView.findViewById(R.id.goal_status_text)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button) // deleteButton eklendi
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]
        holder.goalText.text = goal.goal
        holder.goalDescriptionText.text = goal.goal_description
        holder.goalStatusText.text = if (goal.isCompleted) "Tamamlandı" else "Devam Ediyor"

        if (goal.isCompleted) {
            holder.goalText.paintFlags = holder.goalText.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.goalText.paintFlags = holder.goalText.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.deleteButton.setOnClickListener {
            // Silme düğmesine tıklandığında burada hedefi silme işlemlerini gerçekleştirin
            deleteGoal(goal)
        }
    }

    private fun deleteGoal(goal: UserGoal) {
        val db = FirebaseFirestore.getInstance()
        val userId = goal.userId ?: return

        db.collection("user_goals")
            .document(goal.userId!!)
            .delete()
            .addOnSuccessListener {
                // Veritabanından silme işlemi başarılı olduğunda RecyclerView'yi güncelleyin
                // Öncelikle silinen hedefi gösteren listeyi güncelleyin
                val updatedGoals = goals.toMutableList()
                updatedGoals.remove(goal)
                updateGoals(updatedGoals)
            }
            .addOnFailureListener { exception ->
                // Veritabanından silme işlemi başarısız olduğunda hata durumunu yönetin
                Log.e("GoalsFragment", "Hedefi silme başarısız: ", exception)
                // Hata durumunu kullanıcıya bildirebilirsiniz, örneğin bir Toast mesajı gösterebilirsiniz.
            }
    }



    override fun getItemCount() = goals.size

    fun updateGoals(newGoals: List<UserGoal>) {
        goals = newGoals
        notifyDataSetChanged()
    }
}
