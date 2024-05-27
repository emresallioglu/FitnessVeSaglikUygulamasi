package com.sample.fitnessvesaglikuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NutritionFragment : Fragment() {

    private lateinit var newNutritionProgram: Button
    private lateinit var newDietList: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)

        // Button tanımlama
        newNutritionProgram = view.findViewById(R.id.generate_diet_button)
        newDietList = view.findViewById(R.id.dietList)

        // Button click listener
        newNutritionProgram.setOnClickListener {

            val generativeModel = GenerativeModel(
                // The Gemini 1.5 models are versatile and work with most use cases
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyC0lFhRMKt0ZBJXWPIckB7TEKZq8kSORYc"

            )

            val prompt = "Motivasyon cümlesi yazar mısın? Anasayfam için. 10 kelime olsun. Fitness ve Diyet motivasyonu için. Sadece motivasyon cümlesini yaz, başka hiçbir şey yazma."

            MainScope().launch {
                val response = generativeModel.generateContent(prompt)
                newDietList.setText(response.text)
            }

        }

        return view
    }




}

