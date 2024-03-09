package ru.ok.itmo.hw.chats

import ru.ok.itmo.hw.data_objects.ChatItem

sealed class ChatsUiState {
    data class Success(val itemList : List<ChatItem>) : ChatsUiState()
    data class Error(val throwable: Throwable) : ChatsUiState()
    data object Loading : ChatsUiState()
    data object NoState : ChatsUiState()

}