package tools.blocks.messenger.app.domain.user

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.then
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import tools.blocks.messenger.app.testUtils.user

@ExtendWith(MockitoExtension::class)
class UserServiceTest(
    @Mock private val repository: UserRepository
) {
        private val service = UserService(repository)

    @Test
    fun`Should create new user if not exists`() {
        given(repository.insert(user())).willReturn(true)

        val result = service.newUser(user())

        assertTrue(result)
        then(repository).should().insert(user())
    }

    @Test
    fun`Should return false if user exists already`() {
        given(repository.insert(user())).willReturn(false)

        val result = service.newUser(user())

        assertFalse(result)
        then(repository).should().insert(user())
    }

    @Test
    fun `Should return true if user exists`() {
        given(repository.exists(user())).willReturn(true)

        val result = service.exists(user())

        assertTrue(result)
        then(repository).should().exists(user())
    }

    @Test
    fun `Should return false if user not exists`() {
        given(repository.exists(user())).willReturn(false)

        val result = service.exists(user())

        assertFalse(result)
        then(repository).should().exists(user())
    }
}