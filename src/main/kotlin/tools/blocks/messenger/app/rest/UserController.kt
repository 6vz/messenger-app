package tools.blocks.messenger.app.rest

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tools.blocks.messenger.app.domain.user.User
import tools.blocks.messenger.app.domain.user.UserService

@RestController
@RequestMapping("/user/{user}")
class UserController(private val service: UserService) {
    @PostMapping
    fun create(@PathVariable("user") user: String) = service.newUser(User(user))
}