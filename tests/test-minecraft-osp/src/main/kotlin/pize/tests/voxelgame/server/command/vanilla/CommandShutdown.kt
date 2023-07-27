package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.command.CommandDispatcher
import pize.util.time.PizeRunnable
import java.util.concurrent.atomic.AtomicInteger

object CommandShutdown {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(Commands.literal("shutdown")
            .requiresPlayer()
            .requires { source: CommandSource -> source.asPlayer().name == "GeneralPashon" }
            .executes { obj: CommandContext? -> shutdown() }
            .then(
                Commands.literal("now")!!
                    .executes { obj: CommandContext? -> shutdownNow() })
        )
    }

    private fun shutdown(context: CommandContext) {
        context.server.broadcast(Component().color(TextColor.DARK_RED).text("Shutting down the server..."))
        val counterInt = AtomicInteger(3)
        PizeRunnable {
            if (counterInt.get() == 0) context.server.stop() else {
                context.server.broadcast(Component().color(TextColor.DARK_RED).text(counterInt))
                counterInt.set(counterInt.get() - 1)
            }
        }.runTimerAsync(1000, 1000)
    }

    private fun shutdownNow(context: CommandContext) {
        context.server.stop()
    }
}
