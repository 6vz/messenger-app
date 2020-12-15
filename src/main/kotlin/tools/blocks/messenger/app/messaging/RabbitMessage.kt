package tools.blocks.messenger.app.messaging

import com.fasterxml.jackson.annotation.JsonProperty

data class RabbitMessage(
    @JsonProperty("sender") val sender: String,
    @JsonProperty("recipient") val recipient: String,
    @JsonProperty("payload") val payload: String
)