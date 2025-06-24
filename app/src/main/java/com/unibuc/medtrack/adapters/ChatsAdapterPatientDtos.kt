package com.unibuc.medtrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.PatientUserDTO

class ChatsAdapterPatientDtos(private val patientDtos: List<PatientUserDTO>,
                             private val onChatButtonClick: (patientDto: PatientUserDTO) -> Unit) :
    RecyclerView.Adapter<ChatsAdapterPatientDtos.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item_chat_patient, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = patientDtos.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(patientDtos[position], onChatButtonClick)
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val patientName: TextView = itemView.findViewById(R.id.name)
        private val patientDateOfBirth: TextView = itemView.findViewById(R.id.specialty)
        private val chatButton: View = itemView.findViewWithTag("chat_button")

        fun bind(patientUserDto: PatientUserDTO, onChatButtonClick: (patientDto: PatientUserDTO) -> Unit) {
            patientName.text = patientUserDto.name
            patientDateOfBirth.text = patientUserDto.dateOfBirth

            chatButton.setOnClickListener {
                onChatButtonClick(patientUserDto)
            }
        }
    }
}
