package tools.blocks.messenger.app.domain.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import tools.blocks.messenger.app.testUtils.RepositoryTest
import tools.blocks.messenger.app.testUtils.USER_NAME2
import tools.blocks.messenger.app.testUtils.message
import tools.blocks.messenger.app.testUtils.user

@RepositoryTest
class MessageRepositoryIT @Autowired constructor(private val repository: MessageRepository) {
    @Nested
    @DisplayName("When finding sent")
    inner class WhenFindingSent {
        @Test
        fun `should find sent`() {
            givenMessage()

            val messages = repository.findBySender(user())

            assertThat(messages).containsExactly(message())
        }

        @Test
        fun `should return empty list when not find`() {
            givenMessage()

            val messages = repository.findBySender(user(USER_NAME2))

            assertThat(messages).isEmpty()
        }
    }

    @Nested
    @DisplayName("When finding received")
    inner class WhenFindingReceived {
        @Test
        fun `should find received`() {
            givenMessage()

            val messages = repository.findByRecipient(user(USER_NAME2))

            assertThat(messages).containsExactly(message())
        }

        @Test
        fun `should return empty list when not find`() {
            givenMessage()

            val messages = repository.findByRecipient(user())

            assertThat(messages).isEmpty()
        }
    }

    @Nested
    @DisplayName("When finding received from")
    inner class WhenFindingReceivedFrom{
        @Test
        fun `should find received from`() {
            givenMessage()

            val messages = repository.findBySenderAndRecipient(user(), user(USER_NAME2))

            assertThat(messages).containsExactly(message())
        }

        @Test
        fun `should return empty list when not find`() {
            givenMessage()

            val messages = repository.findBySenderAndRecipient(user(USER_NAME2), user())

            assertThat(messages).isEmpty()
        }
    }

    @Nested
    @DisplayName("When inserting")
    inner class WhenInserting {
        @Test
        fun `should insert new message`() {
            val inserted = repository.insert(message())

            assertTrue(inserted)
        }

        @Test
        fun `should not insert when message exists`() {
            val message = message()
            givenMessage(message)

            val inserted = repository.insert(message())

            assertFalse(inserted)
        }
    }

    private fun givenMessage(message: Message = message()) = repository.insert(message)
}