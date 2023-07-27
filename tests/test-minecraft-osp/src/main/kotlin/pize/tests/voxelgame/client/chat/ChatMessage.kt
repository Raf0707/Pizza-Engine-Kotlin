package pize.tests.voxelgame.client.chat

import pize.tests.voxelgame.main.chat.MessageSource
import pize.tests.voxelgame.main.text.ComponentText
import pize.util.time.Stopwatch

class ChatMessage(val source: MessageSource?, val components: List<ComponentText?>?) {
    private val stopwatch: Stopwatch

    init {
        stopwatch = Stopwatch().start()
    }

    val seconds: Double
        get() = stopwatch.seconds
}
