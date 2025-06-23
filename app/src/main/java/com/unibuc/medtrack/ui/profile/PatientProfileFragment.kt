package com.unibuc.medtrack.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PatientProfileFragment : Fragment(R.layout.fragment_patient_profile) {
    private val viewModel: PatientProfileViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = sessionManager.getUserEmail()
        Log.i("PatientProfileFragment", "Email: " + email)
        if (email != null) {
            viewModel.loadPatientProfile(email)
        }

        viewModel.patientProfile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                view.findViewById<TextInputEditText>(R.id.input_patient_name).setText(profile.user.name)
                view.findViewById<TextInputEditText>(R.id.input_patient_birth_date).setText(profile.patient.dateOfBirth)
                view.findViewById<TextInputEditText>(R.id.input_patient_email).setText(profile.user.email)
                view.findViewById<TextInputEditText>(R.id.input_patient_phone).setText(profile.patient.phoneNumber)
            }
        }

        view.findViewById<View>(R.id.btn_save_profile).setOnClickListener {
            val name = view.findViewById<TextInputEditText>(R.id.input_patient_name).text.toString()
            val email = view.findViewById<TextInputEditText>(R.id.input_patient_email).text.toString()
            val birthDate = view.findViewById<TextInputEditText>(R.id.input_patient_birth_date).text.toString()
            val phone = view.findViewById<TextInputEditText>(R.id.input_patient_phone).text.toString()

            viewModel.patientProfile.value?.let { currentProfile ->
                val updatedProfile = currentProfile.copy(
                    user = currentProfile.user.copy(name = name, email = email),
                    patient = currentProfile.patient.copy(dateOfBirth = birthDate, phoneNumber = phone)
                )
                viewModel.savePatientProfile(updatedProfile)
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
    }
}