package com.example.ecommerceapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerceapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.my_nav)
        val user = FirebaseAuth.getInstance().currentUser
        val startDestination = if (user == null) {
            R.id.loginFragment
        } else {
            R.id.homeFragment
        }
        navGraph.setStartDestination(startDestination)
        navController.graph = navGraph
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val showBottomNav = setOf(R.id.homeFragment, R.id.cartFragment, R.id.accountFragment, R.id.saveFragment)

            binding.bottomNav.visibility =
                if(destination.id in showBottomNav ) View.VISIBLE else View.GONE
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val currentDestination = navController.currentDestination?.id

        when(currentDestination) {
            R.id.cartFragment, R.id.accountFragment, R.id.saveFragment -> {
                navController.navigate(R.id.homeFragment)
                binding.bottomNav.selectedItemId = R.id.homeFragment
            }

            R.id.homeFragment -> finish()
            else -> {
                super.onBackPressedDispatcher.onBackPressed()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}