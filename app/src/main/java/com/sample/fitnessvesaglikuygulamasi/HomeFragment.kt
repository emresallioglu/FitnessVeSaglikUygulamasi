package com.sample.fitnessvesaglikuygulamasi

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // TextView'leri bul ve metni güncelle
        val welcomeTextView: TextView = view.findViewById(R.id.tv_welcome)
        val motivationMessage: TextView = view.findViewById(R.id.motivation_message)
        val videoView: VideoView = view.findViewById(R.id.video_view)

        welcomeTextView.text = "Merhaba, $username"

        // Generative model için içerik oluşturma
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyC0lFhRMKt0ZBJXWPIckB7TEKZq8kSORYc"
        )

        viewLifecycleOwner.lifecycleScope.launch {
            val response = generativeModel.generateContent("Maksimum 10 cümlelik, fitness ve spor, insan sağlığı için Motivasyon sözü yazar mısın?" +
                    "Başlık verme sadece paragrafı yaz.")
            motivationMessage.text = response.text
        }

        // VideoView için video yolu ayarla ve oynat
        val videoUri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.video)
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer -> mediaPlayer.isLooping = true }
        videoView.start()
    }
}
