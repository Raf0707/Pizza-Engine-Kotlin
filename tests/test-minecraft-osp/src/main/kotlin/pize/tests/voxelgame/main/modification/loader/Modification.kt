package pize.tests.voxelgame.main.modification.loader

import pize.files.Resource
import java.net.URLClassLoader

class Modification(
    private val classLoader: URLClassLoader,
    entryPointMap: Map<ModEntryPointType, Class<*>>,
    val name: String,
    val iD: String,
    val version: String,
    val description: String,
    iconPath: String?
) {
    private val entryPoints: MutableMap<ModEntryPointType?, Any>
    val icon: Resource

    init {
        icon = Resource(iconPath!!, classLoader.javaClass)
        entryPoints = HashMap()
        for ((key, value) in entryPointMap) entryPoints[key] = value.getConstructor().newInstance()
    }

    @Throws(Exception::class)
    fun initialize(entryPointType: ModEntryPointType) {
        invokeMethod(entryPointType, entryPointType.initMethodName)
    }

    @Throws(Exception::class)
    fun invokeMethod(entryPointType: ModEntryPointType?, name: String?) {
        if (!entryPoints.containsKey(entryPointType)) return
        val entryPoint = entryPoints[entryPointType]
        val method = entryPoint!!.javaClass.getDeclaredMethod(name)
        method.invoke(entryPoint)
    }
}
