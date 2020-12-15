package tools.blocks.messenger.app.domain.message

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.amqp.rabbit.core.RabbitTemplate
import tools.blocks.messenger.app.config.RabbitConfig.Companion.MESSAGE_ROUTING_KEY
import tools.blocks.messenger.app.config.RabbitConfig.Companion.MESSAGE_EXCHANGE_NAME
import tools.blocks.messenger.app.testUtils.*

@ExtendWith(MockitoExtension::class)
class MessageServiceTest(
    @Mock private val repository: MessageRepository,
    @Mock private val rabbitTemplate: RabbitTemplate
) {
    private val service = MessageService(repository, rabbitTemplate)

    @Test
    fun `Should find sent messages`() {
        given(repository.findBySender(user())).willReturn(listOf(message()))

        val messages = service.findSent(user())

        assertThat(messages).containsExactly(message())
        then(repository).should().findBySender(user())
    }

    @Test
    fun `Should return empty list when sent messages not found`() {
        given(repository.findBySender(user())).willReturn(emptyList())

        val messages = service.findSent(user())

        assertThat(messages).isEmpty()
        then(repository).should().findBySender(user())
    }

    @Test
    fun `Should find received messages`() {
        given(repository.findByRecipient(user())).willReturn(listOf(message()))

        val messages = service.findReceived(user())

        assertThat(messages).containsExactly(message())
        then(repository).should().findByRecipient(user())
    }

    @Test
    fun `return empty list when received messages not found`() {
        given(repository.findByRecipient(user())).willReturn(emptyList())

        val messages = service.findReceived(user())

        assertThat(messages).isEmpty()
        then(repository).should().findByRecipient(user())
    }

    @Test
    fun `Should find received from messages`() {
        given(repository.findBySenderAndRecipient(user(), user(USER_NAME2))).willReturn(listOf(message()))

        val messages = service.findSentBy(user(USER_NAME2), user())

        assertThat(messages).containsExactly(message())
        then(repository).should().findBySenderAndRecipient(user(), user(USER_NAME2))
    }

    @Test
    fun `return empty list when received from messages not found`() {
        given(repository.findBySenderAndRecipient(user(), user(USER_NAME2))).willReturn(emptyList())

        val messages = service.findSentBy(user(USER_NAME2), user())

        assertThat(messages).isEmpty()
        then(repository).should().findBySenderAndRecipient(user(), user(USER_NAME2))
    }

    @Test
    fun `Should send message`() {
        service.send(user(), user(USER_NAME2), PAYLOAD)

        then(rabbitTemplate).should().convertAndSend(MESSAGE_EXCHANGE_NAME, MESSAGE_ROUTING_KEY, rabbitMessage())
    }
}
