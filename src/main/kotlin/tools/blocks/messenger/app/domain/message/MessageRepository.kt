package tools.blocks.messenger.app.domain.message

import org.springframework.dao.support.DataAccessUtils.singleResult
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import tools.blocks.messenger.app.domain.user.User
import tools.blocks.messenger.app.util.getId
import tools.blocks.messenger.app.util.getUser
import java.sql.ResultSet
import java.util.*

@Repository
class MessageRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun insert(message: Message): Boolean
        = jdbcTemplate.update(
            """
            insert into message (id, sender, recipient, payload)
              values (:id, :sender, :recipient, :payload)
            on conflict do nothing
            """,
            with(message) {
                mapOf(
                    "id" to id,
                    "sender" to sender.userName,
                    "recipient" to recipient.userName,
                    "payload" to payload
                )
            }
        ) == 1

    fun findBySender(sender: User): List<Message> =
        jdbcTemplate.query(
            """
                select *
                from message
                where sender = :sender
                """,
            mapOf("sender" to sender.userName)
        ) { rs, _ -> rs.getMessage() }

    fun findByRecipient(recipient: User): List<Message> =
        jdbcTemplate.query(
            """
                select *
                from message
                where recipient = :recipient
                """,
            mapOf("recipient" to recipient.userName)
        ) { rs, _ -> rs.getMessage() }

    fun findBySenderAndRecipient(sender: User, recipient: User): List<Message> =
        jdbcTemplate.query(
            """
                select *
                from message
                where recipient = :recipient
                    and sender = :sender
                """,
            mapOf(
                "sender" to sender.userName,
                "recipient" to recipient.userName
            )
        ) { rs, _ -> rs.getMessage() }

    private fun ResultSet.getMessage() =
        Message(
            id = getId(),
            sender = getUser("sender"),
            recipient = getUser("recipient"),
            payload = getString("payload")
        )
}