package ru.ok.itmo.hw.chats

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.hw.R
import ru.ok.itmo.hw.data_objects.ChatItem

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val chatName = itemView.findViewById<TextView>(R.id.chat_name)
    private val message = itemView.findViewById<TextView>(R.id.last_message)

    fun bind(item : ChatItem) {
        chatName.text = item.name
        message.text = item.lastMessage?.data?.text ?: item.lastMessage?.data?.imagePath

    }

}