package com.sample.fitnessvesaglikuygulamasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GoalsAdapter(private var goals: List<UserGoal>) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalText: TextView = itemView.findViewById(R.id.goal_text)
        val goalDescriptionText: TextView = itemView.findViewById(R.id.goal_description_text)
        val goalStatusText: TextView = itemView.findViewById(R.id.goal_status_text)
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
    }

    override fun getItemCount() = goals.size

    fun updateGoals(newGoals: List<UserGoal>) {
        goals = newGoals
        notifyDataSetChanged()
    }
}
