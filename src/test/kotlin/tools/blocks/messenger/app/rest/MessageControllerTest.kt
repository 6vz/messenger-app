package tools.blocks.messenger.app.rest

import com.nhaarman.mockitokotlin2.given
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.blocks.messenger.app.domain.message.Message
import tools.blocks.messenger.app.domain.message.MessageService
import tools.blocks.messenger.app.domain.user.User
import tools.blocks.messenger.app.domain.user.UserService
import tools.blocks.messenger.app.testUtils.*

@WebMvcTest(MessageController::class)
class MessageControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val converter: MappingJackson2HttpMessageConverter
) {
    @MockBean
    private lateinit var messageService: MessageService

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `Should post message`() {
        givenUserExists(USER_NAME1, true)
        givenUserExists(USER_NAME2, true)

        mockMvc.send(USER_NAME1, USER_NAME2, PAYLOAD).andExpect(status().isOk)
    }

    @Test
    fun `Should return bad request when try to send to yourself`() {
        mockMvc.send(USER_NAME1, USER_NAME1, PAYLOAD).andExpect(status().isBadRequest)
    }

    @Test
    fun `Should return bad request when one of users not exists`() {
        givenUserExists(USER_NAME1, true)
        givenUserExists(USER_NAME2, false)

        mockMvc.send(USER_NAME1, USER_NAME2, PAYLOAD).andExpect(status().isBadRequest)
    }

    @Test
    fun `Should return sent messages`() {
        givenSentMessages(user(), listOf(message()))

        mockMvc.sent(USER_NAME1).
            andExpectJsonContent(
                arrayOf(message()),
                converter
            )
    }

    @Test
    fun `Should return empty list when sent messages not found`() {
        givenSentMessages(user(), emptyList())

        mockMvc.sent(USER_NAME1).
            andExpect(content().json("[]"))
    }

    @Test
    fun `Should return received messages`() {
        givenReceivedMessages(user(), listOf(message()))

        mockMvc.received(USER_NAME1).
        andExpectJsonContent(
            arrayOf(message()),
            converter
        )
    }

    @Test
    fun `Should return empty list when received messages not found`() {
        givenReceivedMessages(user(), emptyList())

        mockMvc.received(USER_NAME1).
        andExpect(content().json("[]"))
    }

    @Test
    fun `Should return received from messages`() {
        givenReceivedFromMessages(user(USER_NAME2), user(), listOf(message()))

        mockMvc.receivedFrom(USER_NAME2, USER_NAME1).
        andExpectJsonContent(
            arrayOf(message()),
            converter
        )
    }

    @Test
    fun `Should return empty list when received from messages not found`() {
        givenReceivedFromMessages(user(USER_NAME2), user(), emptyList())

        mockMvc.receivedFrom(USER_NAME2, USER_NAME1).
        andExpect(content().json("[]"))
    }

    private fun MockMvc.send(user: String, recipient: String, payload: String) =
        perform(
            post("/message/$user/send")
                .param("recipient", recipient)
                .param("payload", payload)
        )

    private fun MockMvc.sent(user: String) =
        perform(get("/message/$user/sent"))

    private fun MockMvc.received(user: String) =
        perform(get("/message/$user/received"))

    private fun MockMvc.receivedFrom(user: String, sender: String) =
        perform(get("/message/$user/received/from/$sender"))

    private fun givenSentMessages(user: User, messages: List<Message>) {
        given(messageService.findSent(user)).willReturn(messages)
    }

    private fun givenReceivedMessages(user: User, messages: List<Message>) {
        given(messageService.findReceived(user)).willReturn(messages)
    }

    private fun givenReceivedFromMessages(user: User, sender: User, messages: List<Message>) {
        given(messageService.findSentBy(user, sender)).willReturn(messages)
    }

    private fun givenUserExists(user: String, exists: Boolean) =
        given(userService.exists(user(user))).willReturn(exists)
}