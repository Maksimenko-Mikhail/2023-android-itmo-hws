package company.vk.polis.task1


class MessageController {
    fun getChatItems(userId: Int, state: State? = null) : List<ChatItem>? {
        val res : MutableList<ChatItem> = ArrayList()
        val info = Repository.getInfo()
        val users = getValidUsers(info)
        val user = users.firstOrNull { user -> user.id == userId } ?: return null
        val userChats = getValidChats(info, user.id)
        val messages = getValidMessages(info, null, state)
        for (chat in userChats) {
            var message : Message? = null
            var timestamp : Long = 0
            for (messageId in chat.getMessageIDs()!!) {
                val mes = messages.get(messageId) ?: continue
                if (mes.timestamp > timestamp) {
                    message = mes
                    timestamp = mes.timestamp
                }
            }
            message ?: continue
            val item = ChatItem(user.avatarUrl, message.text, message.state)
            res.add(item)
        }
        return res
    }

    fun userMessageCount(userId: Int) : Int {
        return getValidMessages(Repository.getInfo(), userId).size
    }

    fun findUser(userId: Int) : String? {
        val users = getValidUsers(Repository.getInfo())
        val user = users.find { it.id == userId }
        return user?.name
    }

    private fun getValidUsers(records : List<Entity>) : List<User> {
        return records.filter { user -> user is User && user.isValid() } as List<User>
    }

    private fun getValidChats(records : List<Entity>, userId: Int) : List<ChatEntity> {
        return records.filter { chat -> (chat is Chat && chat.isValid() && (chat.userIds.senderId == userId || chat.userIds.receiverId == userId)) ||
                (chat is GroupChat && chat.isValid() && chat.userIds.contains(userId))} as List<ChatEntity>
    }

    private fun getValidMessages(records : List<Entity>, userId: Int? = null, state: State? = null) : Map<Int, Message> {
        val res : MutableMap<Int, Message> = HashMap()
        records.map { message -> if (message is Message && message.isValid() && (userId == null || message.senderId == userId) && (state == null || message.state == state)) res.put(message.id, message) }
        return res
    }

    private fun User.isValid() : Boolean {
        return id != null && name != null
    }

    private fun Chat.isValid() : Boolean {
        return id != null && userIds != null && messageIds != null
    }

    private fun GroupChat.isValid() : Boolean {
        return messageIds != null
    }

    private fun Message.isValid() : Boolean {
        return id != null && text != null && senderId != null && timestamp != null && state != null
    }

}