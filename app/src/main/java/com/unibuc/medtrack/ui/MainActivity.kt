package com.unibuc.medtrack.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unibuc.medtrack.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //  Padding for a BottomNavigationView bar can only be removed in kt..
        val tabbar =
            findViewById<BottomNavigationView>(R.id.tabbar)
        ViewCompat.setOnApplyWindowInsetsListener(tabbar) { view, insets ->
            view.setPadding(0, 0, 0, 0)
            insets
        }

        setupListeners()
    }

    //  TODO - When/if "log out" functionality is added, the tabbar (bottom navigation view)
    //         fragment should have its visibility changed back to "GONE", to not be visible
    //         in the Onboarding / Login / Register fragments.
    fun logIn() {
        //TODO log in
    }

    private fun setupListeners() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.tabbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        //  TODO - Redirect to different fragments based on current user's type
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabbar_home -> {
                    if (navController.currentDestination?.id != R.id.patientHomeFragment)
                        navController.navigate(R.id.patientHomeFragment)
                    true
                }

                R.id.tabbar_messages -> {
                    if (navController.currentDestination?.id != R.id.patientChatsFragment)
                        navController.navigate(R.id.patientChatsFragment)
                    true
                }

                R.id.tabbar_calendar -> {
                    if (navController.currentDestination?.id != R.id.patientCalendarFragment)
                        navController.navigate(R.id.patientCalendarFragment)
                    true
                }

                else -> false
            }
        }
    }
}