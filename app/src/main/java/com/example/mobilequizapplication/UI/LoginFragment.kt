package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()



            parentFragmentManager.beginTransaction()
                .replace(R.id.main, HomeFragment.newInstance())
                .commit()
        }


        binding.tvRegisterNow.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.main, RegisterFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
