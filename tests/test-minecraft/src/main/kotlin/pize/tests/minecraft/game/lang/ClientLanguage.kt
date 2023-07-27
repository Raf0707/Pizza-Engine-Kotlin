package pize.tests.minecraft.game.lang

import pize.files.MapConfig
import pize.files.Resource

class ClientLanguage {
    private var storage: HashMap<String?, String?>? = null
    fun getOrDefault(key: String?): String? {
        return storage!!.getOrDefault(key, key)
    }

    companion object {
        fun loadFrom(res: Resource?): ClientLanguage {
            val language = ClientLanguage()
            val map = MapConfig(res)
            map.load()
            language.storage = map.map
            return language
        }
    }
}
