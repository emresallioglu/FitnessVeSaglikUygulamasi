import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.fitnessvesaglikuygulamasi.ActivityDetail
import com.sample.fitnessvesaglikuygulamasi.R

class ActivityAdapter(var activities: List<ActivityDetail>, private val onDeleteClickListener: OnDeleteClickListener) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val activityNameText: TextView = itemView.findViewById(R.id.activity_name_text)
        val caloriesBurnedText: TextView = itemView.findViewById(R.id.calories_burned_text)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.activityNameText.text = activity.activity?.activity_name
        holder.caloriesBurnedText.text = "${activity.caloriesBurned} Kalori"

        // Delete button click listener
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(position)
        }
    }

    override fun getItemCount() = activities.size

    fun updateActivities(newActivities: List<ActivityDetail>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}
