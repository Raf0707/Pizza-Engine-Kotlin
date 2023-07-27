package pize.tests.voxelgame.main.command.node

import pize.tests.voxelgame.main.command.Command
import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.main.command.source.CommandSourcePlayer
import java.util.function.Predicate

open class CommandNodeLiteral(val literal: String) {
    protected val children: MutableMap<String, CommandNodeLiteral?>
    var command: Command? = null
        protected set
    protected var requires: Predicate<CommandSource>? = null

    init {
        children = HashMap()
    }

    fun getChildren(): Collection<CommandNodeLiteral?> {
        return children.values
    }

    fun getChild(literal: String): CommandNodeLiteral? {
        return children[literal]
    }

    fun addChild(node: CommandNodeLiteral?) {
        children[node!!.literal] = node
    }

    open fun then(node: CommandNodeLiteral?): CommandNodeLiteral {
        addChild(node)
        return this
    }

    open fun executes(command: Command?): CommandNodeLiteral {
        this.command = command
        return this
    }

    fun requires(requires: Predicate<CommandSource>?): CommandNodeLiteral {
        this.requires = requires
        return this
    }

    fun requiresPlayer(): CommandNodeLiteral {
        requires = Predicate { source: CommandSource? -> source is CommandSourcePlayer }
        return this
    }

    fun requirementsTest(source: CommandSource): Boolean {
        // Если требований нет - тест пройден
        return if (requires == null) true else requires!!.test(source)
    }
}
