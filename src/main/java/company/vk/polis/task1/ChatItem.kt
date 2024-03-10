package company.vk.polis.task1

class ChatItem(val avatarURL : String?, val lastMessage : String, val state: State) {
    init {
        if (state is State.DELETED) {
            val userName : String = MessageController().findUser(state.userId) ?: "unknown user"
            println("сообщение было удалено пользователем ${userName}")
        } else {
            println("ChatItem: avatarURL: $avatarURL , last message: $lastMessage , state: $state")
        }
    }
}