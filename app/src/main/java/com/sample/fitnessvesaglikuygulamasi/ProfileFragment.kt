package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var textViewName: TextView
    private lateinit var textViewSurname: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewHeight: TextView
    private lateinit var textViewWeight: TextView
    private lateinit var editButton: Button
    private lateinit var logoutButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        textViewName = view.findViewById(R.id.textViewName)
        textViewSurname = view.findViewById(R.id.textViewSurname)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        textViewHeight = view.findViewById(R.id.textViewHeight)
        textViewWeight = view.findViewById(R.id.textViewWeight)
        editButton = view.findViewById(R.id.editButton)
        logoutButton = view.findViewById(R.id.logoutButton)

        editButton.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val user = GlobalVariables.currentUser
        if (user != null) {
            textViewName.text = user.name
            textViewSurname.text = user.surname
            textViewEmail.text = user.email
            textViewHeight.text = user.height.toString()
            textViewWeight.text = user.weight.toString()
        }
    }
}
