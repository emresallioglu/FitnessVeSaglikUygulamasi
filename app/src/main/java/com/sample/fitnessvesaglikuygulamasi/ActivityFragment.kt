import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.fitnessvesaglikuygulamasi.ActivityServices
import com.sample.fitnessvesaglikuygulamasi.AddActivityActivity
import com.sample.fitnessvesaglikuygulamasi.GlobalVariables
import com.sample.fitnessvesaglikuygulamasi.R
import kotlinx.coroutines.runBlocking

class ActivityFragment : Fragment() {
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private val activityServices = ActivityServices()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        addButton = view.findViewById(R.id.add_button)
        recyclerView = view.findViewById(R.id.activity_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        activityAdapter = ActivityAdapter(listOf())
        recyclerView.adapter = activityAdapter

        addButton.setOnClickListener {
            val intent = Intent(activity, AddActivityActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        updateActivities()
    }

    private fun updateActivities() {
        runBlocking {
            val activityDetails = activityServices.getActivityByUserId(GlobalVariables.currentUser?.id.toString())
            activityAdapter.updateActivities(activityDetails)
        }
    }
}