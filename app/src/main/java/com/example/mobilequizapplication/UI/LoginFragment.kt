package com.example.mobilequizapplication.UI

import com.example.mobilequizapplication.R
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Initialize Firebase
        auth = FirebaseAuth.getInstance()

        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btnLogin)
        val tvRegisterNow = view.findViewById<TextView>(R.id.tvRegisterNow)

        // 2. The Login Logic
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Check if empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Ask Firebase: "Is this user real?"
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // IF SUCCESS: Go to Home
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main, HomeFragment()) // Using 'main' ID we fixed earlier
                        .commit()
                }
                .addOnFailureListener { e ->
                    // IF FAIL: Stay here and show error
                    Toast.makeText(context, "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // 4. The Register Button Logic
        tvRegisterNow.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}