package tools.blocks.messenger.app.domain.message

import tools.blocks.messenger.app.domain.user.User
import java.util.*

data class Message(
    val id: UUID,
    val sender: User,
    val recipient: User,
    val payload: String
)