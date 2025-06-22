package com.unibuc.medtrack.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
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
    }
}