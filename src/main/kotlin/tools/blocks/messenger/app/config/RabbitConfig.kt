package tools.blocks.messenger.app.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter


@Configuration
class RabbitConfig {
    @Bean
    fun messageQueue() = Queue(MESSAGE_QUEUE_NAME)

    @Bean
    fun messageExchange() = TopicExchange(MESSAGE_EXCHANGE_NAME)

    @Bean
    fun messageBinding(queue: Queue, exchange: TopicExchange): Binding =
        BindingBuilder.bind(queue).to(exchange).with(MESSAGE_ROUTING_KEY)

    @Bean
    fun producerJackson2MessageConverter(): MessageConverter =
        Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory) =
        RabbitTemplate(connectionFactory).also{ it.messageConverter = producerJackson2MessageConverter() }

    companion object {
        const val MESSAGE_QUEUE_NAME = "messages-queue"
        const val MESSAGE_EXCHANGE_NAME = "messages-exchange"
        const val MESSAGE_ROUTING_KEY = "messages"
    }
}