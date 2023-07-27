package pize.tests.voxelgame.main.modification.loader

import org.json.JSONObject
import java.io.File
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarFile

class ModLoader {
    private val loadedMods: MutableMap<String, Modification>

    init {
        loadedMods = HashMap()
    }

    fun loadMods(modsPath: String?) {
        // Находим моды в папке с модами
        val mods = File(modsPath).list { file: File?, name: String -> name.substring(name.lastIndexOf('.')) == ".jar" }
            ?: return

        // Загружаем
        for (modPath in mods) loadMod(modsPath + modPath)
    }

    fun loadMod(modPath: String) {
        try {
            val jarFile = JarFile(modPath)

            // Try to read file 'mod-properties.json'
            val modPropertiesStream = jarFile.getInputStream(jarFile.getEntry("mod-properties.json"))
                ?: throw RuntimeException("[ModLoader]: Not found file 'mod-properties.json'")
            val modProperties = JSONObject(String(modPropertiesStream.readAllBytes()))

            // Read properties
            val name = modProperties.getString("name")
            val id = modProperties.getString("id")
            val version = modProperties.getString("version")
            val description = modProperties.getString("description")
            val icon = modProperties.getString("icon")

            // Class loader
            val urls = arrayOf(File(modPath).toURI().toURL())
            val classLoader = URLClassLoader(urls, this.javaClass.getClassLoader())

            // Read entry points
            val entryPointMap: MutableMap<ModEntryPointType, Class<*>> = HashMap()
            val entryPoints = modProperties.getJSONObject("entrypoints")

            // Итерируемся по всем типам (main, client, server)
            for (type in ModEntryPointType.entries) {
                // Если содержится один из них
                if (entryPoints.has(type.jsonKey)) {
                    // Загружаем класс
                    val className = entryPoints.getString(type.jsonKey)
                    try {
                        val entryPointClass = classLoader.loadClass(className)
                        // Проверяем, реализует ли он соответствующий интерфейс (ModLoader, ClientModLoader, DedicatedServerModLoader)
                        if (Arrays.stream(entryPointClass.interfaces)
                                .anyMatch { pointClass: Class<*> -> pointClass == type.initializerClass }
                        ) // Добавляем в список точек входа
                            entryPointMap[type] = entryPointClass
                    } catch (e: Exception) {
                        System.err.println("Failed to load Entry point '$className'")
                    }
                }
            }

            // Add mod to list
            val mod = Modification(classLoader, entryPointMap, name, id, version, description, icon)
            loadedMods[id] = mod
            jarFile.close()
        } catch (e: Exception) {
            System.err.println("Failed to load Mod '" + modPath + "' (" + e.message + ")")
        }
    }

    fun initializeMods(entryPointType: ModEntryPointType) {
        try {
            for (modification in loadedMods.values) modification.initialize(entryPointType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun invokeMethod(entryPointType: ModEntryPointType?, name: String?) {
        try {
            for (modification in loadedMods.values) modification.invokeMethod(entryPointType, name)
        } catch (ignored: Exception) {
        }
    }

    fun getLoadedMods(): Collection<Modification> {
        return loadedMods.values
    }
}
