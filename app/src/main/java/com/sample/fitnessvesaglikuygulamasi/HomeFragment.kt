package com.sample.fitnessvesaglikuygulamasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


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
        welcomeTextView.text = "Merhaba, $username"
    }

}