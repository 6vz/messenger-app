package tools.blocks.messenger.app.domain.message

import mu.KLogging
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import tools.blocks.messenger.app.config.RabbitConfig.Companion.MESSAGE_EXCHANGE_NAME
import tools.blocks.messenger.app.config.RabbitConfig.Companion.MESSAGE_ROUTING_KEY
import tools.blocks.messenger.app.domain.user.User
import tools.blocks.messenger.app.messaging.RabbitMessage

@Component
class MessageService(
    val repository: MessageRepository,
    val rabbitTemplate: RabbitTemplate
    ) {
    fun findSent(user: User) = repository.findBySender(user)

    fun findReceived(user: User) = repository.findByRecipient(user)

    fun findSentBy(user: User, sender: User) = repository.findBySenderAndRecipient(sender, user)

    fun send(user: User, recipient: User, payload: String) {
        rabbitTemplate.convertAndSend(
            MESSAGE_EXCHANGE_NAME,
            MESSAGE_ROUTING_KEY,
            RabbitMessage(
                sender = user.userName,
                recipient = recipient.userName,
                payload = payload
            )
        )
        logger.info("$payload")
    }

    companion object : KLogging()
}