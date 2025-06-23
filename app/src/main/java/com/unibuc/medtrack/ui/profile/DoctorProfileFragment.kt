package com.unibuc.medtrack.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    }
}
