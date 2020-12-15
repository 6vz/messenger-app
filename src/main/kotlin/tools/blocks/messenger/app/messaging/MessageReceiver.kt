package tools.blocks.messenger.app.messaging

import mu.KLogging
import org.springframework.stereotype.Component
import tools.blocks.messenger.app.config.GenerateId
import tools.blocks.messenger.app.domain.message.Message
import tools.blocks.messenger.app.domain.message.MessageRepository
import tools.blocks.messenger.app.domain.user.User
import org.springframework.amqp.rabbit.annotation.RabbitListener
import tools.blocks.messenger.app.config.RabbitConfig.Companion.MESSAGE_QUEUE_NAME


@Component
class MessageReceiver(
    val messageRepository: MessageRepository,
    val generateId: GenerateId
) {
    @RabbitListener(id = RECEIVE_MESSAGE_METHOD, queues = [MESSAGE_QUEUE_NAME])
    fun receiveMessage(rabbitMessage: RabbitMessage) {
        logger.info("Received message: $rabbitMessage")
        messageRepository.insert(rabbitMessage.toMessage())
    }

    fun RabbitMessage.toMessage() =
        Message(
            id = generateId.invoke(),
            sender = User(userName = sender),
            recipient = User(userName = recipient),
            payload = payload
        )

    companion object : KLogging() {
        const val RECEIVE_MESSAGE_METHOD = "receiveMessage"
    }
}