package tools.blocks.messenger.app.rest

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import tools.blocks.messenger.app.domain.message.Message
import tools.blocks.messenger.app.domain.message.MessageService
import tools.blocks.messenger.app.domain.user.User
import tools.blocks.messenger.app.domain.user.UserService


@RestController
@RequestMapping("/message/{user}")
class MessageController(
    val messageService: MessageService,
    val userService: UserService
    ) {
    @GetMapping("/sent")
    fun getAllSentMessages(
        @PathVariable(name = "user") user: String
    ): List<Message> = messageService.findSent(user.toUser())

    @GetMapping("/received")
    fun getAllReceivedMessages(
        @PathVariable(name = "user") user: String
    ): List<Message>  = messageService.findReceived(user.toUser())

    @GetMapping("/received/from/{sender}")
    fun getMessagesFromUser(
        @PathVariable(name = "user") user: String,
        @PathVariable(name = "sender") sender: String
    ): List<Message> = messageService.findSentBy(user = user.toUser(), sender = sender.toUser())

    @PostMapping("/send")
    fun sendMessage(
        @PathVariable("user") user: String,
        @RequestParam("recipient") recipient: String,
        @RequestParam("payload") payload: String
    ): ResponseEntity<Boolean> {
        if (user == recipient) return badRequest().body(false)
        with(userService) {
            if(!exists(user.toUser()) || !exists(recipient.toUser())) return badRequest().body(false)
        }
        return messageService.send(user.toUser(), recipient.toUser(), payload).let { ok().body(true) }
    }

    private fun String.toUser() = User(this)
}
