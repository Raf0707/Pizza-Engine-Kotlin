package pize.tests.voxelgame.client.chat

import pize.tests.voxelgame.client.ClientGame
import pize.tests.voxelgame.main.chat.MessageSource
import pize.tests.voxelgame.main.chat.MessageSourceOther
import pize.tests.voxelgame.main.net.packet.CBPacketChatMessage
import pize.tests.voxelgame.main.net.packet.SBPacketChatMessage
import pize.tests.voxelgame.main.text.Component
import pize.util.io.TextProcessor
import java.util.concurrent.CopyOnWriteArrayList

class Chat(private val game: ClientGame) {
    private val messageList: CopyOnWriteArrayList<ChatMessage>
    val textProcessor: TextProcessor
    var isOpened = false
        private set(opened) {
            field = opened
            textProcessor.isActive = opened
            game.session.controller.playerController.rotationController.showMouse(opened)
        }
    private val history: MutableList<String>
    private var historyPointer = 0

    init {
        messageList = CopyOnWriteArrayList()
        history = ArrayList()
        textProcessor = TextProcessor(false)
        textProcessor.isActive = false
        putMessage(MessageSourceOther(), Component().text("Enter /help for command list"))
    }

    val messages: List<ChatMessage>
        get() = messageList

    fun putMessage(messagePacket: CBPacketChatMessage) {
        messageList.add(ChatMessage(messagePacket.source, messagePacket.components))
    }

    fun putMessage(source: MessageSource?, component: Component?) {
        messageList.add(ChatMessage(source, component!!.toFlatList()))
    }

    fun clear() {
        messageList.clear()
    }

    val enteringText: String
        get() = textProcessor.toString()

    fun enter() {
        val message = textProcessor.toString()
        game.sendPacket(SBPacketChatMessage(message))
        textProcessor.clear()
        if (!history.isEmpty() && history[history.size - 1] == message) return
        history.add(history.size, message)
        historyPointer = history.size - 1
    }

    fun historyUp() {
        if (historyPointer == history.size - 1 && history[history.size - 1] != textProcessor.toString()) {
            historyPointer++
            if (!textProcessor.toString().isBlank()) history.add(textProcessor.toString())
        }
        if (historyPointer - 1 < 0) return
        historyPointer--
        textProcessor.removeLine()
        textProcessor.insertLine(history[historyPointer])
    }

    fun historyDown() {
        if (historyPointer + 2 > history.size) return
        historyPointer++
        textProcessor.removeLine()
        textProcessor.insertLine(history[historyPointer])
    }

    val cursorX: Int
        get() = textProcessor.cursorX

    fun close() {
        game.session.controller.playerController.rotationController.lockNextFrame()
        historyPointer = history.size - 1
        textProcessor.removeLine()
        isOpened = false
    }

    fun open() {
        isOpened = true
    }

    fun openAsCommandLine() {
        open()
        textProcessor.insertChar('/')
    }
}
