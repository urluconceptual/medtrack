package com.unibuc.medtrack.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.unibuc.medtrack.R

class LoginFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.sign_up_text).setOnClickListener {
            goToRegisterFragment()
        }

        view.findViewById<TextView>(R.id.login_button).setOnClickListener {
            doLogin()
        }

        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            goToOnboardingPage()
        }
    }

    private fun goToRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun doLogin() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeGraph()
        findNavController().navigate(action)
    }

    private fun goToOnboardingPage() {
        val action = LoginFragmentDirections.actionLoginFragmentToOnboardingFragment()
        findNavController().navigate(action)
    }
}