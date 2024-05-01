import kotlinx.serialization.json.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import kotlin.system.exitProcess

const val TWITCH_CHANNEL_ID = "CHANNEL_ID_HERE"
const val TWITCH_OAUTH_TOKEN = "OAUTH_TOKEN_HERE"

fun main() {

    val websocketUrl = "wss://pubsub-edge.twitch.tv"
    val request = okhttp3.Request.Builder().url(websocketUrl).build()
    val client = okhttp3.OkHttpClient()
    val heartbeatInterval = 60 * 1000

    val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            println("WebSocket connection opened!")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val json = text.toJson()
            println("Message received: $json")

            if(json.get("type")?.jsonPrimitive?.content == "PONG") {
                println("PONG received")
            } else if(json.get("type")?.jsonPrimitive?.content == "MESSAGE") {
                val rootObject = JSONObject(text)
                val data = rootObject.getJSONObject("data")
                val topic = data.getString("topic")

                val messageString = data.getString("message")
                val messageObject = JSONObject(messageString)
                val messageType = messageObject.getString("type")

                // check if the message that was received is a whisper
                if(messageType == "whisper_sent") {

                    val dataObject = JSONObject(messageObject.getString("data"))

                    val recipient = dataObject.getJSONObject("recipient")

                    println("Recipient: ${recipient.getString("username")}")

                }

                val dataObjectString = messageObject.getJSONObject("data_object")
                val dataObject = JSONObject(dataObjectString)

                if (dataObject.has("id")) {
                    val id = dataObject.getString("id")
                    println("Message type: $topic with type $messageType, id: $id")
                } else {
                    println("Message type: $topic with type $messageType (no id found)")
                }
            } else {
                println("Unknown message type")
            }

        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            println("WebSocket closing: code=$code, reason=$reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            println("WebSocket connection failed: ${t.message}")

            exitProcess(1)

        }
    }

    val webSocket: WebSocket = client.newWebSocket(request, webSocketListener)

    startListening(webSocket)

    sendHeartbeat(webSocket, heartbeatInterval)

}

// function to send a heartbeat every x seconds
fun sendHeartbeat(webSocket: WebSocket, interval: Int) {
    val heartbeatMessage = "{\"type\":\"PING\"}"
    while (true) {
        Thread.sleep(interval.toLong())
        webSocket.send(heartbeatMessage)
    }
}

fun startListening(webSocket: WebSocket) {

    //val message = "{\"type\":\"LISTEN\",\"data\":{\"topics\":[\"channel-points-channel-v1.${TWITCH_CHANNEL_ID}\"],\"auth_token\":\"${TWITCH_OAUTH_TOKEN}\"}}"

    val message = JsonObject(
        mapOf(
            "type" to JsonPrimitive("LISTEN"),
            "data" to JsonObject(
                mapOf(
                    "topics" to JsonArray(listOf(JsonPrimitive("whispers.${TWITCH_CHANNEL_ID}"))),
                    "auth_token" to JsonPrimitive(TWITCH_OAUTH_TOKEN)
                )
            )
        )
    )

    println(message)

    webSocket.send(message.toString())

}

// translate String to JSON
fun String.toJson(): JsonObject {
    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
    return json.parseToJsonElement(this).jsonObject
}