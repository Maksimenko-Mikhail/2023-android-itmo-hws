package company.vk.polis.task1

import org.jetbrains.annotations.Nullable


data class GroupChat(val id : Int, val userIds : List<Int>, val messageIds : List<Int>?) : ChatEntity {

    override fun getId(): Int {
        return id
    }

    override fun getMessageIDs(): List<Int>? {
        return messageIds
    }

}