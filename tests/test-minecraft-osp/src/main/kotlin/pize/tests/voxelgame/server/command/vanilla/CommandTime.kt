package pize.tests.voxelgame.server.command.vanilla

import pize.math.Maths.floor
import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.time.GameTime
import pize.tests.voxelgame.server.command.CommandDispatcher

object CommandTime {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(Commands.literal("time")
            .then(Commands.literal("set")
                .then(
                    Commands.argument("time", CommandArg.Companion.time())!!
                        .executes { obj: CommandContext? -> setTime() })
                .then(Commands.literal("day")
                    .executes { obj: CommandContext? -> setDay() }
                    .then(
                        Commands.argument("time", CommandArg.Companion.time())!!
                            .executes { obj: CommandContext? -> setDayTime() })
                )
                .then(
                    Commands.literal("midnight")!!
                        .executes { obj: CommandContext? -> setMidnight() })
                .then(
                    Commands.literal("night")!!
                        .executes { obj: CommandContext? -> setNight() })
                .then(
                    Commands.literal("noon")!!
                        .executes { obj: CommandContext? -> setNoon() })
            )
            .then(Commands.literal("get")
                .executes { obj: CommandContext? -> getTicks() }
                .then(
                    Commands.literal("seconds")!!
                        .executes { obj: CommandContext? -> getSeconds() })
                .then(
                    Commands.literal("minutes")!!
                        .executes { obj: CommandContext? -> getMinutes() })
                .then(Commands.literal("day")
                    .executes { obj: CommandContext? -> getDays() }
                    .then(
                        Commands.literal("ticks")!!
                            .executes { obj: CommandContext? -> getDayTicks() })
                    .then(
                        Commands.literal("seconds")!!
                            .executes { obj: CommandContext? -> getDaySeconds() })
                    .then(
                        Commands.literal("minutes")!!
                            .executes { obj: CommandContext? -> getDayMinutes() })
                )
            )
        )
    }

    private fun setTime(context: CommandContext) {
        val time = context.getArg(0)!!.asTime()
        // Set time
        context.server.gameTime
            .setTime(time.time, time.unit)
        // Send msg
        context.source.sendMessage(Component().text("Set the gametime to " + time.time + " " + time.unit.literal))
    }

    private fun setDayTime(context: CommandContext) {
        val time = context.getArg(0)!!.asTime()
        // Set day time
        context.server.gameTime
            .setDayTime(time.time, time.unit)
        // Send msg
        context.source.sendMessage(Component().text("Set the daytime to " + time.time + " " + time.unit.literal))
    }

    private fun setDay(context: CommandContext) {
        context.server.gameTime.setDay()
        // Send msg
        context.source.sendMessage(Component().text("Set the time to Day (" + GameTime.Companion.TIME_DAY + ")"))
    }

    private fun setMidnight(context: CommandContext) {
        context.server.gameTime.setMidnight()
        // Send msg
        context.source.sendMessage(Component().text("Set the time to Midnight (" + GameTime.Companion.TIME_MIDNIGHT + ")"))
    }

    private fun setNight(context: CommandContext) {
        context.server.gameTime.setNight()
        // Send msg
        context.source.sendMessage(Component().text("Set the time to Night (" + GameTime.Companion.TIME_NIGHT + ")"))
    }

    private fun setNoon(context: CommandContext) {
        context.server.gameTime.setNoon()
        // Send msg
        context.source.sendMessage(Component().text("Set the time to Noon (" + GameTime.Companion.TIME_NOON + ")"))
    }

    private fun getTicks(context: CommandContext) {
        context.source.sendMessage(
            Component().text(
                "Gametime is " +
                        context.server.gameTime.ticks
            )
        )
    }

    private fun getSeconds(context: CommandContext) {
        context.source.sendMessage(
            Component().text(
                "Gametime is " +
                        context.server.gameTime.seconds + " seconds"
            )
        )
    }

    private fun getMinutes(context: CommandContext) {
        context.source.sendMessage(
            Component().text(
                "Gametime is " +
                        context.server.gameTime.minutes + " minutes"
            )
        )
    }

    private fun getDays(context: CommandContext) {
        context.source.sendMessage(
            Component().text(
                "Day is " +
                        floor((context.server.gameTime.days + 1).toDouble())
            )
        )
    }

    private fun getDayTicks(context: CommandContext) {
        context.source.sendMessage(
            Component().text(
                "Daytime is " +
                        context.server.gameTime.dayTicks
            )
        )
    }

    private fun getDaySeconds(context: CommandContext) {
        context.source.sendMessage(
            Component().text(
                "Daytime is " +
                        context.server.gameTime.daySeconds + " seconds"
            )
        )
    }

    private fun getDayMinutes(context: CommandContext) {
        context.source.sendMessage(
            Component().text(
                "Daytime is " +
                        context.server.gameTime.dayMinutes + " minutes"
            )
        )
    }
}
