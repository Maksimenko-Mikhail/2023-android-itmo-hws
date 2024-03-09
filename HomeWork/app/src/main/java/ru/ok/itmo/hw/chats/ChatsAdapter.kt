package ru.ok.itmo.hw.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.hw.R
import ru.ok.itmo.hw.data_objects.ChatItem

class ChatsAdapter(private val items : List<ChatItem>) :
    RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.message_card_item,
            parent,
            false
        )
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(items[position])
    }
}