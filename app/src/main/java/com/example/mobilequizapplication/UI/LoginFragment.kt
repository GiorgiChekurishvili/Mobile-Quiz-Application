package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobilequizapplication.R
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        val btnEdit = view.findViewById<ImageButton>(R.id.btnEdit)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvLevel = view.findViewById<TextView>(R.id.tvLevel)


        if (currentUser != null) {

            tvUserName.text = currentUser.displayName ?: currentUser.email
        } else {
            tvUserName.text = "Guest User"
        }


        tvLevel.text = "Level 12 Quiz Master"


        btnEdit.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}