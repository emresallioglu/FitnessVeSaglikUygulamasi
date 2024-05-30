package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        uptadeActivities() // onResume'da tabloyu güncelle
    }

    private fun uptadeActivities() {
        runBlocking {
            val activityDetails = activityServices.getActivityByUserId(GlobalVariables.currentUser?.id.toString())
            val activityListLayout = view?.findViewById<LinearLayout>(R.id.activityList)

            // Temizleme
            activityListLayout?.removeAllViews()

            for (activityDetail in activityDetails) {
                val activityTextView = TextView(context)
                activityTextView.text = "${activityDetail.activity?.activity_name}: ${activityDetail.caloriesBurned} Kalori"
                activityTextView.setPadding(0, 16, 0, 16) // Padding ekle
                activityListLayout?.addView(activityTextView)

                // Ayırıcı ekle
                val divider = View(context)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2 // Ayırıcı yüksekliği
                )
                divider.layoutParams = params
                divider.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
                activityListLayout?.addView(divider)
            }
        }
    }

}