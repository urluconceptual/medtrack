package com.unibuc.medtrack.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.SessionManager

@AndroidEntryPoint
class DoctorProfileFragment : Fragment(R.layout.fragment_doctor_profile) {  // make sure to pass layout here

    private val viewModel: DoctorProfileViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager  // <-- Inject SessionManager here

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = sessionManager.getUserEmail()  // Use injected sessionManager instance
        Log.i("DoctorProfileFragment", "Email: " + email)
        if (email != null) {
            viewModel.loadDoctorProfile(email)
        }

        viewModel.doctorProfile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                view.findViewById<TextInputEditText>(R.id.input_doctor_name).setText(profile.user.name)
                view.findViewById<TextInputEditText>(R.id.input_doctor_specialty).setText(profile.doctor.specialty)
                view.findViewById<TextInputEditText>(R.id.input_doctor_email).setText(profile.user.email)
            }
        }

        view.findViewById<View>(R.id.btn_save_profile).setOnClickListener {
            val name = view.findViewById<TextInputEditText>(R.id.input_doctor_name).text.toString()
            val specialty = view.findViewById<TextInputEditText>(R.id.input_doctor_specialty).text.toString()
            val emailField = view.findViewById<TextInputEditText>(R.id.input_doctor_email).text.toString()

            viewModel.doctorProfile.value?.let { currentProfile ->
                val updatedProfile = currentProfile.copy(
                    user = currentProfile.user.copy(name = name, email = emailField),
                    doctor = currentProfile.doctor.copy(specialty = specialty)
                )
                viewModel.saveDoctorProfile(updatedProfile)
            }
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show()
                // Clear focus
                view.clearFocus()

                // Hide keyboard
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        val logoutButton = view.findViewById<Button>(R.id.login_button_form)

        logoutButton.setOnClickListener {
            performLogout()
        }


    }

    private fun performLogout() {
        val sessionManager = SessionManager(requireContext())
        sessionManager.clearSession()

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        findNavController().navigate(
            R.id.action_doctorHomeFragment_to_loginFragment,
            null,
            NavOptions.Builder()
                      .setPopUpTo(R.id.authentication_navigation, true)
                      .build())
    }


}
