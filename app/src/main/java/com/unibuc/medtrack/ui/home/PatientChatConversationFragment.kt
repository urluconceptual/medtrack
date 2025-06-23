package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.adapters.ChatMessageAdapter
import com.unibuc.medtrack.data.models.ChatMessageDTO
import java.text.SimpleDateFormat

class PatientChatConversationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_chat_conversation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupListeners()
    }
    private fun setupData() {
        generateConversation()
    }

    private fun setupListeners() {
        requireView().findViewById<ImageButton>(R.id.back_button_chat).setOnClickListener {
            goToChatsPage()
        }
    }

    private fun generateConversation() {
        val recyclerView: RecyclerView = requireActivity().findViewById(R.id.conversationRecyclerView)

        val dateFormat = SimpleDateFormat("dd-M-yyyy hh:mm:ss")

        val messages = listOf(
            ChatMessageDTO("Buna ziua! Ati luat tratamentul de astazi?", false, dateFormat.parse("28-05-2015 20:00:00")),

            ChatMessageDTO("Buna ziua!", true, dateFormat.parse("28-05-2015 20:06:00")),

            ChatMessageDTO("Da, l-am luat la ora 12.", true, dateFormat.parse("28-05-2015 20:06:30")),

            ChatMessageDTO("OK. Nu uitati si de cel de la ora 21.", false, dateFormat.parse("28-05-2015 20:08:00")),

            ChatMessageDTO("Nu voi uita, multumesc! O zi buna!", true, dateFormat.parse("28-05-2015 20:09:00"))
        )

        recyclerView.adapter = ChatMessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun goToChatsPage() {
        val action = PatientChatConversationFragmentDirections.actionPatientChatConversationFragmentToPatientChatsFragment()
        findNavController().navigate(action)
    }

}