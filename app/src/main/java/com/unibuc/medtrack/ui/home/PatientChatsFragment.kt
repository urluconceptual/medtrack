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
import com.unibuc.medtrack.adapters.ChatsAdapter
import com.unibuc.medtrack.data.models.DoctorModel
import com.unibuc.medtrack.data.models.DoctorUserDTO
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
        viewModel.loadDoctorDtos()
        observeViewModel()
    }

    private fun setupRecycler() {
        recyclerView = requireView().findViewById(R.id.chatsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.doctorDtos.observe(viewLifecycleOwner) { doctorDtos ->
            recyclerView.adapter = ChatsAdapter(doctorDtos) { doctorDto ->
                goToChatConversationPage(doctorDto.userId, doctorDto.name, doctorDto.specialty)
            }
        }
    }

    private fun goToChatConversationPage(doctorId: String, doctorName: String, doctorSpecialty: String) {
        val bundle = Bundle().apply {
            putString("doctorId", doctorId)
            putString("doctorName", doctorName)
            putString("doctorSpecialty", doctorSpecialty)
        }
        findNavController().navigate(R.id.patientChatConversationFragment, bundle)
    }

}