package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import com.sample.fitnessvesaglikuygulamasi.ActivityServices



class ActivityFragment : Fragment() {

    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        // Butonu bulun
        val button = view.findViewById<Button>(R.id.btn_add_activity)

        // Butona OnClickListener ekleyin
        button.setOnClickListener {
            // Navigation Controller'ı alın
            val navController = findNavController()

            // Hedef fragment'a gidin (action ID'sini kullanarak)
            navController.navigate(R.id.action_activityFragment_to_addActivityFragment)
        }

        return view
    }



}