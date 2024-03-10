import company.vk.polis.task1.DataUtils
import company.vk.polis.task1.MessageController
import company.vk.polis.task1.State

fun main() {
//    val utils = DataUtils()
    val messageController = MessageController()

    messageController.getChatItems(1)
    println()
    messageController.getChatItems(2)
    println()
    messageController.getChatItems(3)
    println()


    println(messageController.userMessageCount(1))
    println()

    println(messageController.userMessageCount(2))
    println()

    println(messageController.userMessageCount(3))
    println()
}
