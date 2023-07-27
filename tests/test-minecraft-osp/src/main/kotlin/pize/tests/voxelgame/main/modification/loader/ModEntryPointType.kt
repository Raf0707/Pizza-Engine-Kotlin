package pize.tests.voxelgame.main.modification.loader

import pize.tests.voxelgame.main.modification.api.ClientModInitializer
import pize.tests.voxelgame.main.modification.api.DedicatedServerModInitializer
import pize.tests.voxelgame.main.modification.api.ModInitializer

enum class ModEntryPointType(val jsonKey: String, val initializerClass: Class<*>) {
    MAIN("main", ModInitializer::class.java),
    CLIENT("client", ClientModInitializer::class.java),
    DEDICATED_SERVER("server", DedicatedServerModInitializer::class.java);

    val initMethodName: String

    init {
        initMethodName = initializerClass.getDeclaredMethods()[0].name
    }
}
