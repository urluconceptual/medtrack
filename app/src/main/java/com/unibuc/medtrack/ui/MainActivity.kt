package com.unibuc.medtrack.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unibuc.medtrack.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.unibuc.medtrack.data.models.UserType
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermission()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabbar =
            findViewById<BottomNavigationView>(R.id.tabbar)
        ViewCompat.setOnApplyWindowInsetsListener(tabbar) { view, insets ->
            view.setPadding(0, 0, 0, 0)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.onboardingFragment -> {
                    tabbar.visibility = View.GONE
                }
                else -> {
                    tabbar.visibility = View.VISIBLE
                }
            }
        }

        setupListeners()
    }

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
                            dest = R.id.doctorChatsFragment
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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

}