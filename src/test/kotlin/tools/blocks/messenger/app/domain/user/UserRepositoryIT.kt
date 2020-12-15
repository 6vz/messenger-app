package tools.blocks.messenger.app.domain.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import tools.blocks.messenger.app.testUtils.RepositoryTest
import tools.blocks.messenger.app.testUtils.USER_NAME1
import tools.blocks.messenger.app.testUtils.USER_NAME2
import tools.blocks.messenger.app.testUtils.user
import java.util.*
import java.util.stream.Stream

@RepositoryTest
class UserRepositoryIT @Autowired constructor(private val repository: UserRepository) {
    @Test
    fun `Should create new user`() {
        val id = UUID.randomUUID().toString().subSequence(0,24).toString()
        val expectedUser = user(id)
        val expected = repository.insert(expectedUser)

        assertTrue(expected)
        assertTrue(repository.exists(expectedUser))
    }

    @Test
    fun `Should not create new user if user name already exists`() {
        val id = UUID.randomUUID().toString().subSequence(0,24).toString()
        val expectedUser = user(id)
        val expectedFirst = repository.insert(expectedUser)
        val expectedSecond = repository.insert(expectedUser)

        assertTrue(expectedFirst)
        assertFalse(expectedSecond)
        assertTrue(repository.exists(expectedUser))
    }

    @ParameterizedTest
    @ArgumentsSource(UsersArgumentsProvider::class)
    fun `Should find existing users`(
        user: User,
        exists: Boolean
    ) {
        repository.insert(user())
        repository.insert(user(USER_NAME2))
        assertThat(repository.exists(user)).isEqualTo(exists)
    }

    private class UsersArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> = Stream.of(
            Arguments.of(user(USER_NAME_TEST), false),
            Arguments.of(user(USER_NAME1), true),
            Arguments.of(user(USER_NAME2), true)
        )
    }

    private companion object {
        const val USER_NAME_TEST = "test"
    }
}