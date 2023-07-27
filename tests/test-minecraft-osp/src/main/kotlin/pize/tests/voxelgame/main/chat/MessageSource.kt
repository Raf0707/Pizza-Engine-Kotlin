package pize.tests.voxelgame.main.chat

open class MessageSource(val source: MessageSources) {

    val isPlayer: Boolean
        get() = source == MessageSources.PLAYER
    val isServer: Boolean
        get() = source == MessageSources.SERVER

    fun asPlayer(): MessageSourcePlayer {
        return this as MessageSourcePlayer
    }
}
