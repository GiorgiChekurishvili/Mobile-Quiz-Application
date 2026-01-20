package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }


        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                when (f) {

                    is LoginFragment,
                    is RegisterFragment,
                    is FragmentQuiz,
                    is DifficultyFragment -> {
                        hideBottomNavigation()
                    }

                    else -> {
                        showBottomNavigation()
                    }
                }
            }
        }, true)


        if (savedInstanceState == null) {
            replaceFragment(LoginFragment(), false)
        }



        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)


            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment.newInstance()
                R.id.nav_categories -> FragmentCategories.newInstance()
                R.id.nav_profile -> ProfileFragment.newInstance()
                else -> return@setOnItemSelectedListener false
            }

            if (currentFragment?.javaClass == selectedFragment.javaClass) {
                return@setOnItemSelectedListener true
            }


            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            replaceFragment(selectedFragment, false)
            true
        }
    }


    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }


    fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }


    fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }
}
