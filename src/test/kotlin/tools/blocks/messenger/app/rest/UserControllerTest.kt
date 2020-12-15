package tools.blocks.messenger.app.rest

import com.nhaarman.mockitokotlin2.given
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.blocks.messenger.app.domain.user.User
import tools.blocks.messenger.app.domain.user.UserService

@WebMvcTest(UserController::class)
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `Should insert user if not exists`() {
        given(userService.newUser(User("user1"))).willReturn(true)

        mockMvc.create("user1")
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun `Should not insert user if exists`() {
        given(userService.newUser(User("user1"))).willReturn(false)

        mockMvc.create("user1")
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
    }

    private fun MockMvc.create(user: String) =
        perform(post("/user/$user"))
}