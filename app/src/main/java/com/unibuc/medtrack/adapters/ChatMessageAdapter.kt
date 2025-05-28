package com.unibuc.medtrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.ChatMessageDTO

class ChatMessageAdapter(private val messages: List<ChatMessageDTO>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == 1) {
            val view = inflater.inflate(R.layout.item_message_sent, parent, false)
            return SentMessageViewHolder(view)
        }
        val view = inflater.inflate(R.layout.item_message_received, parent, false)
        return ReceivedMessageViewHolder(view)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].sentByMe)
            return 1
        return 2
    }

    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.message_sent)

        fun bind(message: ChatMessageDTO) {
            textMessage.text = message.message
        }
    }

    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.message_received)

        fun bind(message: ChatMessageDTO) {
            textMessage.text = message.message
        }
    }
}
