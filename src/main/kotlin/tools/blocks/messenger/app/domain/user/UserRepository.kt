package tools.blocks.messenger.app.domain.user

import org.springframework.dao.support.DataAccessUtils.singleResult
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import tools.blocks.messenger.app.domain.message.Message
import java.sql.ResultSet

@Repository
class UserRepository(private val jdbcTemplate: NamedParameterJdbcTemplate){
    fun insert(user: User): Boolean=
        jdbcTemplate.update(
        """
            insert into usr(username)
              values (:username)
            on conflict do nothing
            """,
        with(user) {
            mapOf(
                "username" to userName
            )
        }
    ) == 1

    fun exists(user: User): Boolean =
        singleResult(
            jdbcTemplate.query(
                """
                select *
                from usr
                where username = :username
                """,
                mapOf("username" to user.userName)
            ) { rs, _ -> rs.getUser() != null}
        ) ?: false

    private fun ResultSet.getUser() =
        User(userName = getString("username"))
}