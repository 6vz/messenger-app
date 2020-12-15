package tools.blocks.messenger.app.domain.user

import org.springframework.stereotype.Component

@Component
class UserService(private val repository: UserRepository) {
    fun exists(user: User) = repository.exists(user)

    fun newUser(user: User) = repository.insert(user)
}