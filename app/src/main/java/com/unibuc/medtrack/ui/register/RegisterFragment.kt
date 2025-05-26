package com.unibuc.medtrack.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.SignUpResponse
import com.unibuc.medtrack.data.models.UserType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val viewModel by viewModels<RegisterViewModel>()

    private var selectedUserType: UserType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservables()
    }

    private fun setupListeners() {
        requireView().findViewById<TextView>(R.id.login_text).setOnClickListener {
            goToLoginFragment()
        }

        requireView().findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.back_button_register)
            .setOnClickListener {
                goToOnboardingPage()
            }

        requireView().findViewById<Button>(R.id.sing_up_button).setOnClickListener {
            doSignUp()
        }
    }

    private fun setupObservables() {
        viewModel.isSignUpSuccessful.observe(viewLifecycleOwner) {
            when (it) {
                SignUpResponse.SUCCESS -> {
                    Toast.makeText(requireContext(), "Sign-up successful!", Toast.LENGTH_SHORT).show()

                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.authentication_navigation, inclusive = true)
                        .setLaunchSingleTop(true)
                        .build()

                    when (selectedUserType) {
                        UserType.DOCTOR -> findNavController().navigate(R.id.doctor_navigation, null, navOptions)
                        UserType.PATIENT -> findNavController().navigate(R.id.patient_navigation, null, navOptions)
                        else -> Toast.makeText(requireContext(), "Unknown user type", Toast.LENGTH_SHORT).show()
                    }
                }
                SignUpResponse.EMAIL_TAKEN -> {
                    Toast.makeText(requireContext(), "Email already taken", Toast.LENGTH_SHORT).show()
                }
                SignUpResponse.EMPTY_FIELDS -> {
                    Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun goToLoginFragment() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun doSignUp() {
        val name = requireView()
            .findViewById<TextInputEditText>(R.id.name_input)
            .text.toString()
            .trim()
        val email = requireView()
            .findViewById<TextInputEditText>(R.id.email_input)
            .text
            .toString()
            .trim()
        val password = requireView()
            .findViewById<TextInputEditText>(R.id.password_input)
            .text
            .toString()
        val selectedRadioId = requireView()
            .findViewById<RadioGroup>(R.id.radioGroup)
            .checkedRadioButtonId

        val accountType = when (selectedRadioId) {
            R.id.radioOptionDoctor -> UserType.DOCTOR
            R.id.radioOptionPatient -> UserType.PATIENT
            else -> null
        }
        selectedUserType = accountType

        viewModel.submitSignUpForm(name, email, password, accountType)
    }

    private fun goToOnboardingPage() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToOnboardingFragment()
        findNavController().navigate(action)
    }
}