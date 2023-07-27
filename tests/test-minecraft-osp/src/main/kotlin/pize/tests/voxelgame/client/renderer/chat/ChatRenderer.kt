package pize.tests.voxelgame.client.renderer.chat

import pize.Pize.dt
import pize.Pize.height
import pize.Pize.mouse
import pize.Pize.width
import pize.app.Disposable
import pize.graphics.texture.Region
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.math.Maths.abs
import pize.tests.voxelgame.client.renderer.GameRenderer
import pize.tests.voxelgame.client.renderer.text.TextComponentBatch
import pize.tests.voxelgame.main.text.Component
import kotlin.math.max
import kotlin.math.min

class ChatRenderer(private val gameRenderer: GameRenderer) : Disposable {
    private val batch: TextureBatch?
    private val textBatch: TextComponentBatch?
    private val chatX = 10
    private val chatY = 10
    private var scroll = 0f
    private var scrollMotion = 0f
    private val skins: MutableMap<Int, Texture>
    private val headRegion: Region
    private val hatRegion: Region

    init {
        textBatch = gameRenderer.textComponentBatch
        batch = textBatch.getBatch()
        skins = HashMap()
        for (i in 1..21) skins[i] = Texture("texture/skin/skin$i.png")
        headRegion = Region(skins[1]!!, 8.0, 8.0, 8.0, 8.0)
        hatRegion = Region(skins[1]!!, 40.0, 8.0, 8.0, 8.0)
    }

    fun render() {
        batch!!.begin()
        textBatch!!.setBackgroundColor(0.0, 0.0, 0.0, 0.0)
        val chatHeight = height / 2f
        val chatWidth = width / 2f
        val lineAdvance = textBatch.font.lineAdvanceScaled
        val chat = gameRenderer.session.game.chat
        val messages = chat.messages
        val openedChatY: Float = chatY + if (chat!!.isOpened) lineAdvance + 10 else 0
        var chatMessagesHeight = 0f
        for (message in messages!!) chatMessagesHeight += textBatch.font.getBounds(
            message.components.toString(),
            chatWidth.toDouble()
        ).y

        // Enter
        if (chat!!.isOpened) {
            val enteringText = chat.enteringText
            val lineWidth = textBatch.font.getLineWidth(enteringText)
            batch.drawQuad(
                0.4,
                chatX.toFloat(),
                chatY.toFloat(),
                max(lineWidth.toDouble(), chatWidth.toDouble()),
                lineAdvance
            )
            val cursorLineWidth = textBatch.font.getLineWidth(enteringText!!.substring(0, chat.cursorX))
            textBatch.drawComponent(
                Component().text(enteringText),
                chatX.toFloat(),
                chatY.toFloat()
            )
            batch.drawQuad(
                1.0,
                1.0,
                1.0,
                1.0,
                chatX + cursorLineWidth,
                chatY.toFloat(),
                textBatch.font.scale,
                lineAdvance
            )
        }

        // Scroll
        if (chat.isOpened && mouse()!!
                .isInBounds(chatX.toDouble(), openedChatY.toDouble(), chatWidth.toDouble(), chatHeight.toDouble())
        ) scrollMotion -= mouse()!!.scroll * dt * lineAdvance * 10
        scroll += scrollMotion
        scrollMotion *= 0.95.toFloat()
        if (!chat.isOpened) scroll = 0f
        scroll = min(0.0, scroll.toDouble()).toFloat()
        scroll = max(min(0.0, (chatHeight - chatMessagesHeight).toDouble()), scroll.toDouble())
            .toFloat()

        // Chat
        val headAdvance = lineAdvance * 1.5f
        val headDrawX = (headAdvance - lineAdvance) / 2
        val headDrawY = (lineAdvance - lineAdvance) / 2
        batch.scissor.begin(
            0,
            chatX.toDouble(),
            openedChatY.toDouble(),
            (chatWidth + headAdvance).toDouble(),
            chatHeight.toDouble()
        ) // Scissors begin
        var textAdvanceY = 0
        for (i in messages.indices.reversed()) {
            val message = messages[i]
            var alpha = 1f
            if (!chat.isOpened) {
                alpha = if (message.seconds < MSG_LIFE_TIME_SEC) min(
                    1.0,
                    MSG_LIFE_TIME_SEC - message.seconds
                )
                    .toFloat() else continue
            }
            val textHeight =
                textBatch.font.getTextHeight(message.components.toString(), (chatWidth + headAdvance).toDouble())
            val renderChatY = openedChatY + textAdvanceY + scroll
            val lineWrapAdvanceY = textHeight - lineAdvance
            val isPlayer = message.source.isPlayer
            val messageStringBuilder = StringBuilder()
            for (component in message.components) messageStringBuilder.append(component.toString())

            // Render background
            batch.drawQuad(0.4 * alpha, chatX.toFloat(), renderChatY, chatWidth + headAdvance, textHeight)

            // Render head
            if (isPlayer) {
                val skinID = abs(message.source.asPlayer().playerName.hashCode()) % 20 + 1
                val skin = skins[skinID]
                batch.setAlpha(alpha.toDouble())
                batch.draw(
                    skin,
                    chatX + headDrawX,
                    renderChatY + lineWrapAdvanceY + headDrawY,
                    lineAdvance,
                    lineAdvance,
                    headRegion
                )
                batch.setTransformOrigin(0.5, 0.5)
                batch.scale(1.1f)
                batch.draw(
                    skin,
                    chatX + headDrawX,
                    renderChatY + lineWrapAdvanceY + headDrawY,
                    lineAdvance,
                    lineAdvance,
                    hatRegion
                )
                batch.scale(1f)
            }
            // Render text
            textBatch.drawComponents(
                message.components,
                chatX + headAdvance,
                renderChatY + lineWrapAdvanceY,
                chatWidth,
                alpha
            )
            textAdvanceY = (textAdvanceY + textHeight).toInt()
        }
        batch.end()
        batch.scissor.end(0) // Scissors end
    }

    override fun dispose() {
        batch!!.dispose()
    }

    companion object {
        private const val MSG_LIFE_TIME_SEC = 6f
    }
}
