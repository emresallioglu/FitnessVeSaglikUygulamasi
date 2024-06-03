package com.sample.fitnessvesaglikuygulamasi

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Giriş yapan kullanıcının adını al
        val username = GlobalVariables.currentUser?.name ?: ""

        // TextView'i bul ve metni güncelle
        val welcomeTextView: TextView = view.findViewById(R.id.tv_welcome)
        val motivationMessage: TextView = view.findViewById(R.id.motivation_message)

        welcomeTextView.text = "Merhaba, $username"

        val videoView: VideoView = view.findViewById(R.id.videoView)
        val videoPath = "android.resource://" + requireActivity().packageName + "/" + R.raw.video // "example" yerine dosya adını koy
        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.start()

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyC0lFhRMKt0ZBJXWPIckB7TEKZq8kSORYc"
        )

        viewLifecycleOwner.lifecycleScope.launch {
            val response = generativeModel.generateContent("Maksimum 10 kelimelik, fitness ve spor, insan sağlığı için Motivasyon sözü yazar mısın?")
            motivationMessage.text = response.text
        }
    }
}
