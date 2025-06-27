package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.unibuc.medtrack.R
import com.unibuc.medtrack.adapters.ChatMessageAdapter
import com.unibuc.medtrack.data.models.UserType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientChatConversationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_chat_conversation, container, false)

    private lateinit var otherId: String

    private lateinit var doctorName: String
    private lateinit var doctorSpecialty: String

    private lateinit var patientName: String
    private lateinit var patientDateOfBirth: String

    private lateinit var recyclerView: RecyclerView
    private val viewModel: PatientChatConversationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupListeners()
        viewModel.loadRole()
        setupInfo()
        observeViewModel()
    }

    private fun setupInfo() {
        viewModel.myRole.observe(viewLifecycleOwner) { role ->
            when (role) {
                UserType.PATIENT -> {
                    setupDoctorInfo()
                    //viewModel.loadMessageDtos(otherId)
                    viewModel.getChatMessages(otherId)
                }
                UserType.DOCTOR -> {
                    setupPatientInfo()
                    //viewModel.loadMessageDtos(otherId)
                    viewModel.getChatMessages(otherId)
                }
                else -> {}
            }
        }
    }
    private fun setupDoctorInfo() {
        arguments?.let {
            otherId = it.getString("doctorId") ?: throw IllegalStateException()
            doctorName = it.getString("doctorName") ?: ""
            doctorSpecialty = it.getString("doctorSpecialty") ?: ""
        }

        view?.findViewById<TextView>(R.id.doctor_name)!!.setText(doctorName)
        view?.findViewById<TextView>(R.id.doctor_specialty)!!.setText(doctorSpecialty)
    }
    private fun setupPatientInfo() {
        arguments?.let {
            otherId = it.getString("patientId") ?: throw IllegalStateException()
            patientName = it.getString("patientName") ?: ""
            patientDateOfBirth = it.getString("patientDateOfBirth") ?: ""
        }

        view?.findViewById<TextView>(R.id.doctor_name)!!.setText(patientName)
        view?.findViewById<TextView>(R.id.doctor_specialty)!!.setText(patientDateOfBirth)
    }

    private fun setupRecycler() {
        recyclerView = requireView().findViewById(R.id.conversationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListeners() {
        requireView().findViewById<ImageButton>(R.id.back_button_chat).setOnClickListener {
            goToChatsPage()
        }

        requireView().findViewById<Button>(R.id.send_message_button).setOnClickListener {
            sendChatMessage()
        }
    }

    private fun observeViewModel() {
        viewModel.messageDtos.observe(viewLifecycleOwner) { messages ->
            recyclerView.adapter = ChatMessageAdapter(messages)
        }
    }

    private fun goToChatsPage() {
        findNavController().navigate(R.id.action_doctorChatConversationFragment_to_doctorChatsFragment)
    }

    private fun sendChatMessage() {
        val text = requireView()
            .findViewById<TextInputEditText>(R.id.message_input)
            .text
            .toString()

        viewModel.sendChatMessage(text)

        requireView()
            .findViewById<TextInputEditText>(R.id.message_input)
            .setText("")
    }

}