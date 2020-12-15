package tools.blocks.messenger.app.messaging

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.then
import com.nhaarman.mockitokotlin2.timeout
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.boot.test.mock.mockito.MockBean
import tools.blocks.messenger.app.domain.message.MessageRepository
import tools.blocks.messenger.app.domain.message.MessageService
import tools.blocks.messenger.app.testUtils.*

@SpringBootTest(webEnvironment = NONE)
class MessageReceiverIT @Autowired constructor(
    private val service: MessageService
) {
    @MockBean
    private lateinit var messageRepository: MessageRepository

    @Test
    fun `Should receive message`() {
        service.send(user(), user(USER_NAME2), PAYLOAD)

        then(messageRepository).should(timeout(1_000)).insert(any())
    }
}