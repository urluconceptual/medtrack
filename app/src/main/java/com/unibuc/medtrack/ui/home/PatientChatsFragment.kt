package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.unibuc.medtrack.R

class PatientChatsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_chats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupListeners()
    }

    private fun setupListeners() {
        val chatButtons = findAllViewsWithTag(requireView(), "chat_button")
        for (chatButton in chatButtons)
            chatButton.setOnClickListener {
                goToChatConversationPage()
            }
    }

    fun findAllViewsWithTag(root: View, tag: Any): List<View> {
        val result = mutableListOf<View>()
        if (root.tag == tag) result.add(root)
        if (root is ViewGroup) {
            for (i in 0 until root.childCount) {
                result.addAll(findAllViewsWithTag(root.getChildAt(i), tag))
            }
        }
        return result
    }

    private fun setupData() {

    }

    private fun goToChatConversationPage() {
        val action = PatientChatsFragmentDirections.actionPatientChatsFragmentToPatientChatConversationFragment()
        findNavController().navigate(action)
    }

}