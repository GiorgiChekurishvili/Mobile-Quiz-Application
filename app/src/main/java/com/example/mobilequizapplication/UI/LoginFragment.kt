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


        auth = FirebaseAuth.getInstance()

        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btnLogin)
        val tvRegisterNow = view.findViewById<TextView>(R.id.tvRegisterNow)


        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // IF SUCCESS:
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()


                    if (activity is MainActivity) {
                        (activity as MainActivity).showBottomNavigation()
                    }

                   -
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }
                .addOnFailureListener { e ->
                    // IF FAIL: Stay here and show error
                    Toast.makeText(context, "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }


        tvRegisterNow.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}