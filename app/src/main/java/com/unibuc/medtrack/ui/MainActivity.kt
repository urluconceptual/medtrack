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
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.unibuc.medtrack.data.models.UserType
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

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

    private fun setupListeners() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.tabbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController


        bottomNav.setOnItemSelectedListener { item ->
            lifecycleScope.launch {
                val role = viewModel.loadRole()

                when (item.itemId) {
                    R.id.tabbar_home -> {
                        var dest = 0

                        if (role == UserType.DOCTOR)
                            dest = R.id.doctorHomeFragment
                        else
                            dest = R.id.patientHomeFragment

                        if (navController.currentDestination?.id != dest) {
                            navController.navigate(dest)
                        }
                    }
                    R.id.tabbar_messages -> {
                        var dest = 0

                        if (role == UserType.DOCTOR)
                        //  TODO
                            return@launch
                        else
                            dest = R.id.patientChatsFragment

                        if (navController.currentDestination?.id != dest) {
                            navController.navigate(dest)
                        }
                    }
                    R.id.tabbar_calendar -> {
                        var dest = 0

                        if (role == UserType.DOCTOR)
                        //  TODO
                            return@launch
                        else
                            dest = R.id.patientCalendarFragment

                        if (navController.currentDestination?.id != dest) {
                            navController.navigate(dest)
                        }
                    }
                    R.id.tabbar_profile -> {
                        var dest = 0

                        if (role == UserType.DOCTOR)
                            dest = R.id.profileFragment
                        else
                            dest = R.id.patientProfileFragment

                        if (navController.currentDestination?.id != dest) {
                            navController.navigate(dest)
                        }
                    }
                }
            }
            true
        }
    }
}