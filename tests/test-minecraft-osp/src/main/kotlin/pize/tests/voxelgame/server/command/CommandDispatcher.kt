package pize.tests.voxelgame.server.command

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.node.CommandNodeArg
import pize.tests.voxelgame.main.command.node.CommandNodeLiteral
import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.main.command.source.CommandSourcePlayer
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.command.vanilla.*

class CommandDispatcher(val server: Server) {
    private val commands: MutableMap<String?, CommandNodeLiteral?>

    init {
        commands = HashMap()

        // Register commands
        CommandHelp.registerTo(this)
        CommandSpawn.registerTo(this)
        CommandTeleport.registerTo(this)
        CommandFly.registerTo(this)
        CommandList.registerTo(this)
        CommandSeed.registerTo(this)
        CommandSetWorldSpawn.registerTo(this)
        CommandTell.registerTo(this)
        CommandKick.registerTo(this)
        CommandLevel.registerTo(this)
        CommandTime.registerTo(this)
        CommandShutdown.registerTo(this)
    }

    fun getCommands(): Collection<CommandNodeLiteral?> {
        return commands.values
    }

    fun newCommand(node: CommandNodeLiteral?) {
        commands[node.getLiteral()] = node
    }

    fun executeCommand(commandMessage: String, source: CommandSource) {
        // Уведомление об отправке команды игроком
        if (source is CommandSourcePlayer) println("[Server]: Player " + source.player.name + " execute command: " + commandMessage)

        // Разделяем комманду на аргументы
        val splitCommand = commandMessage.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Проверяем существует ли такая команда в корневом списке
        var targetCommandNode = commands[splitCommand[0]]
        if (targetCommandNode == null) {
            source.sendMessage(Component().color(TextColor.RED).text("Command /" + splitCommand[0] + " is not exists"))
            return
        } else if (!targetCommandNode.requirementsTest(source)) {
            source.sendMessage(Component().color(TextColor.RED).text("You don't have permission"))
            return
        }


        // Аргументы
        val joinedArguments = commandMessage.substring(splitCommand[0].length)
        val argumentList: MutableList<CommandArg?> = ArrayList()

        // Ищем ноду по дереву
        // Проходимся по всей строке с аргументами
        var index = if (joinedArguments.startsWith(" ")) 1 else 0
        while (index < joinedArguments.length) {
            // Оставшиеся символы
            val remainingChars = joinedArguments.substring(index)
            val nextSpace = remainingChars.indexOf(" ")
            val children: List<CommandNodeLiteral?> = ArrayList(
                targetCommandNode!!.children
            )
            // Проходимся по каждому child-node
            val oldIndex = index
            for (j in children.indices) {
                val child = children[j]

                // Если он является агрументом - пытаемся парсить его
                if (child is CommandNodeArg) {
                    val arg: CommandArg = child.argument
                    val parsedChars = arg.parse(remainingChars, source, server)
                    // [DEBUG]: System.out.println("Parsed " + remainingChars + " by "+ arg.getClass().getSimpleName() + ": " + parsedChars);

                    // Если не удалось разобрать аргумент
                    if (parsedChars == 0) {
                        // И если этот аргумент оставался последним вариантом
                        if (j + 1 == children.size) {
                            // Указать на ошибку
                            val wrongArgEndPointer = if (nextSpace > 0) nextSpace + index else joinedArguments.length
                            source.sendMessage(
                                Component().color(TextColor.RED).text("Wrong argument: ").reset()
                                    .text("/" + splitCommand[0] + joinedArguments.substring(0, wrongArgEndPointer))
                                    .color(TextColor.RED).text("<-HERE")
                            )
                            return
                        } else continue
                    }
                    index += parsedChars + 1 // 1 - пробел
                    argumentList.add(arg)
                    targetCommandNode = child
                    break

                    // Если это литерал - просто проверяем на равенство
                } else {
                    // Находим текущий аргумент
                    val currentArgument =
                        if (nextSpace == -1) remainingChars else remainingChars.substring(0, nextSpace)

                    // [DEBUG]: System.out.println("Check literal: " + currentArgument + " by " + child.getClass().getSimpleName() + "(" + child.getLiteral() + ")");

                    // Проверяем
                    if (currentArgument.equals(child.getLiteral(), ignoreCase = true)) {
                        targetCommandNode = child
                        index += currentArgument.length + 1 // 1 - пробел
                        break
                        // Если не удалось проверить
                    } else {
                        // И если этот аргумент оставался последним вариантом
                        if (j + 1 == children.size) {
                            val wrongArgEndPointer = if (nextSpace > 0) nextSpace + index else joinedArguments.length
                            source.sendMessage(
                                Component().color(TextColor.RED).text("Wrong argument: ").reset()
                                    .text("/" + splitCommand[0] + joinedArguments.substring(0, wrongArgEndPointer))
                                    .color(TextColor.RED).text("<-HERE")
                            )
                            return
                        }
                    }
                }
            }

            // Если не один нод в списке children не смог парсить joinedArguments.substring(index) (индекс остался тем же)
            if (oldIndex == index) {
                // Выбрасываем ошибку
                val wrongArgEndPointer = if (nextSpace > 0) nextSpace + index else joinedArguments.length
                source.sendMessage(
                    Component().color(TextColor.RED).text("Wrong argument: ").reset()
                        .text("/" + splitCommand[0] + joinedArguments.substring(0, wrongArgEndPointer))
                        .color(TextColor.RED).text("<-HERE")
                )
                return
            }
        }

        // Check permissions
        if (!targetCommandNode!!.requirementsTest(source)) {
            source.sendMessage(Component().color(TextColor.RED).text("You don't have permission"))
            return
        }

        // Последняя нода должна содержать интерфейс 'Command' для исполнения команды
        val command = targetCommandNode.command
        if (command == null) {
            source.sendMessage(Component().color(TextColor.RED).text("Wrong arguments"))
            return
        }

        // Исполняем команду
        val commandContext = CommandContext(server, source, commandMessage, argumentList)
        command.run(commandContext)
    }
}
