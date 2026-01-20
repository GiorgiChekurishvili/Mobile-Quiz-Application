package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser
        binding.tvUserName.text = currentUser?.displayName ?: currentUser?.email ?: "Explorer"
        binding.tvLevel.text = "Level 12 Quiz Master"


        binding.btnEdit.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

}
