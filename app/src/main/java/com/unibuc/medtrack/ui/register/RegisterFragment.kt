package com.unibuc.medtrack.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.unibuc.medtrack.R

class RegisterFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.login_text).setOnClickListener {
            goToLoginFragment()
        }

        view.findViewById<TextView>(R.id.signup_button).setOnClickListener {
            doSignUp()
        }

        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            goToOnboardingPage()
        }
    }

    private fun goToLoginFragment() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun doSignUp() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToHomeGraph()
        findNavController().navigate(action)
    }

    private fun goToOnboardingPage() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToOnboardingFragment()
        findNavController().navigate(action)
    }
}