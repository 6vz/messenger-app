package tools.blocks.messenger.app.testUtils

import tools.blocks.messenger.app.domain.message.Message
import tools.blocks.messenger.app.domain.user.User
import tools.blocks.messenger.app.messaging.RabbitMessage
import java.util.*

fun message(
    sender: String = USER_NAME1,
    recipient: String = USER_NAME2,
    payload: String = PAYLOAD
) =
    Message(
        id = UUID1,
        sender = user(sender),
        recipient = user(recipient),
        payload = payload
    )

fun rabbitMessage(
    sender: String = USER_NAME1,
    recipient: String = USER_NAME2,
    payload: String = PAYLOAD
) =
    RabbitMessage(
        sender = sender,
        recipient = recipient,
        payload = payload
    )

fun user(user: String = USER_NAME1) = User(user)

const val USER_NAME1 = "user1"
const val USER_NAME2 = "user2"
const val PAYLOAD = "Lorem ipsum"
val UUID1 = UUID.randomUUID()!!