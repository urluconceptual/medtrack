package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.adapters.ChatMessageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientChatConversationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_chat_conversation, container, false)

    private lateinit var doctorId: String
    private lateinit var doctorName: String
    private lateinit var doctorSpecialty: String

    private lateinit var recyclerView: RecyclerView
    private val viewModel: PatientChatConversationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupListeners()
        setupDoctorInfo()
        viewModel.loadMessageDtos(doctorId)
        observeViewModel()
    }

    private fun setupDoctorInfo() {
        arguments?.let {
            doctorId = it.getString("doctorId") ?: throw IllegalStateException()
            doctorName = it.getString("doctorName") ?: ""
            doctorSpecialty = it.getString("doctorSpecialty") ?: ""
        }

        view?.findViewById<TextView>(R.id.doctor_name)!!.setText(doctorName)
        view?.findViewById<TextView>(R.id.doctor_specialty)!!.setText(doctorSpecialty)
    }

    private fun setupRecycler() {
        recyclerView = requireView().findViewById(R.id.conversationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListeners() {
        requireView().findViewById<ImageButton>(R.id.back_button_chat).setOnClickListener {
            goToChatsPage()
        }
    }

    private fun observeViewModel() {
        viewModel.messageDtos.observe(viewLifecycleOwner) { messages ->
            recyclerView.adapter = ChatMessageAdapter(messages)
        }
    }

    private fun goToChatsPage() {
        val action = PatientChatConversationFragmentDirections.actionPatientChatConversationFragmentToPatientChatsFragment()
        findNavController().navigate(action)
    }

}