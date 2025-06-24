package com.unibuc.medtrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.DoctorUserDTO

class ChatsAdapter(private val doctorDtos: List<DoctorUserDTO>,
                   private val onChatButtonClick: (doctorDto: DoctorUserDTO) -> Unit) :
    RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item_chat_patient, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = doctorDtos.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(doctorDtos[position], onChatButtonClick)
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val doctorName: TextView = itemView.findViewById(R.id.name)
        private val doctorSpecialty: TextView = itemView.findViewById(R.id.specialty)
        private val chatButton: View = itemView.findViewWithTag("chat_button")

        fun bind(doctorUserDto: DoctorUserDTO, onChatButtonClick: (doctorDto: DoctorUserDTO) -> Unit) {
            doctorName.text = doctorUserDto.name
            doctorSpecialty.text = doctorUserDto.specialty

            chatButton.setOnClickListener {
                onChatButtonClick(doctorUserDto)
            }
        }
    }
}
