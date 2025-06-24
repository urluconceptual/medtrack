package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.adapters.ChatsAdapterDoctorDtos
import com.unibuc.medtrack.adapters.ChatsAdapterPatientDtos
import com.unibuc.medtrack.data.models.UserType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientChatsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_chats, container, false)

    private lateinit var recyclerView: RecyclerView
    private val viewModel: PatientChatsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewModel.loadDtos()
        observeViewModel()
    }

    private fun setupRecycler() {
        recyclerView = requireView().findViewById(R.id.chatsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.myRole.observe(viewLifecycleOwner) { role ->
            when (role) {
                UserType.PATIENT -> {
                    observePatientViewModel()
                }
                UserType.DOCTOR -> {
                    observeDoctorViewModel()
                }
                else -> {}
            }
        }
    }
    private fun observePatientViewModel() {
        viewModel.doctorDtos.observe(viewLifecycleOwner) { doctorDtos ->
            recyclerView.adapter = ChatsAdapterDoctorDtos(doctorDtos) { doctorDto ->
                goToPatientChatConversationPage(doctorDto.userId, doctorDto.name, doctorDto.specialty)
            }
        }
    }
    private fun observeDoctorViewModel() {
        viewModel.patientDtos.observe(viewLifecycleOwner) { patientDtos ->
            recyclerView.adapter = ChatsAdapterPatientDtos(patientDtos) { patientDto ->
                goToDoctorChatConversationPage(patientDto.userId, patientDto.name, patientDto.dateOfBirth)
            }
        }
    }

    private fun goToPatientChatConversationPage(doctorId: String, doctorName: String, doctorSpecialty: String) {
        val bundle = Bundle().apply {
            putString("doctorId", doctorId)
            putString("doctorName", doctorName)
            putString("doctorSpecialty", doctorSpecialty)
        }
        findNavController().navigate(R.id.patientChatConversationFragment, bundle)
    }
    private fun goToDoctorChatConversationPage(patientId: String, patientName: String, patientDateOfBirth: String) {
        val bundle = Bundle().apply {
            putString("patientId", patientId)
            putString("patientName", patientName)
            putString("patientDateOfBirth", patientDateOfBirth)
        }
        findNavController().navigate(R.id.doctorChatConversationFragment, bundle)
    }

}