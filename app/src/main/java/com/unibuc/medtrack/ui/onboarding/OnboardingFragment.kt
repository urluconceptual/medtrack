package com.unibuc.medtrack.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.unibuc.medtrack.R

class OnboardingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_onboarding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.signup_button).setOnClickListener {
            goToSignUpFragment()
        }

        view.findViewById<TextView>(R.id.login_button).setOnClickListener {
            goToLoginFragment()
        }
    }

    private fun goToLoginFragment() {
        val action = OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun goToSignUpFragment() {
        val action = OnboardingFragmentDirections.actionOnboardingFragmentToRegisterFragment()
        findNavController().navigate(action)
    }
}