package com.unibuc.medtrack.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.LoginResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservables()
    }

    private fun setupListeners() {
        requireView().findViewById<TextView>(R.id.sign_up_text).setOnClickListener {
            goToRegisterFragment()
        }

        requireView().findViewById<MaterialButton>(R.id.login_button_form).setOnClickListener {
            doLogin()
        }

        requireView().findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.back_button_login).setOnClickListener {
            goToOnboardingPage()
        }
    }

    private fun setupObservables() {
        viewModel.isLoginSuccessful.observe(viewLifecycleOwner) {
            when (it) {
                LoginResponse.SUCCESS -> {
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeGraph()
                    findNavController().navigate(action)
                }
                LoginResponse.WRONG_CREDENTIALS -> {
                    Toast.makeText(requireContext(), "Wrong credentials", Toast.LENGTH_SHORT).show()
                }
                LoginResponse.EMPTY_FIELDS -> {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun doLogin() {
        val email = requireView()
            .findViewById<TextInputEditText>(R.id.email_input)
            .text
            .toString()
            .trim()
        val password = requireView()
            .findViewById<TextInputEditText>(R.id.password_input)
            .text
            .toString()

        viewModel.submitLoginForm(email, password)
    }

    private fun goToOnboardingPage() {
        val action = LoginFragmentDirections.actionLoginFragmentToOnboardingFragment()
        findNavController().navigate(action)
    }
}