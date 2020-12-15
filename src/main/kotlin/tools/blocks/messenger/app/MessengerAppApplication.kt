package tools.blocks.messenger.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessengerAppApplication

fun main(args: Array<String>) {
    runApplication<MessengerAppApplication>(*args)
}
