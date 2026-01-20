package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.util.Log // Added for debugging
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val termsAgreed = binding.cbTerms.isChecked

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!termsAgreed) {
                Toast.makeText(requireContext(), "Agree to the Terms & Privacy Policy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("AUTH_DEBUG", "Starting registration for: $email")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (isAdded) {
                        if (task.isSuccessful) {
                            Log.d("AUTH_DEBUG", "Success!")
                            Toast.makeText(requireContext(), "Registration Successful!", Toast.LENGTH_SHORT).show()

                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, LoginFragment())
                                .commit()
                        } else {
                            Log.e("AUTH_DEBUG", "Failure: ${task.exception?.message}")
                            Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

        binding.tvLoginNow.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}