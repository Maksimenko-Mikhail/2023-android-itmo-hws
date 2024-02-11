package company.vk.polis.task1

data class GroupChat(val id : Int?, val userIds : List<Int>?, val messageIds : List<Int>?) : ChatEntity {
    override fun getId(): Int? {
        return id
    }

    override fun getMessageIds(): List<Int>? {
        return messageIds
    }
}