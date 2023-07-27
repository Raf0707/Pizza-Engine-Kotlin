package pize.tests.minecraft.game.lang

import pize.files.MapConfig
import pize.files.Resource
import pize.tests.minecraft.utils.log.Logger
import java.util.*

class LanguageManager {
    private val languages: HashMap<String?, LanguageInfo>
    var language: ClientLanguage? = null
        private set

    init {
        languages = HashMap()
    }

    fun selectLanguage(languageCode: String?) {
        language = ClientLanguage.Companion.loadFrom(Resource("vanilla/lang/$languageCode.txt"))
    }

    fun updateAvailableLanguages() {
        languages.clear()
        val langDir = Resource("vanilla/lang/", true)
        val langList = langDir.listResources() ?: return
        for (resLang in langList) {
            val langMap = MapConfig(resLang)
            langMap.load()
            val code = langMap["language.code"]
            if (code != null) languages[code] = LanguageInfo(
                code,
                langMap["language.name"],
                langMap["language.region"]
            )
            langMap.clear()
        }
        val joiner = StringJoiner(", ")
        for (languageInfo in languages.values) joiner.add(languageInfo.name)
        Logger.Companion.instance().info("Loaded Languages: $joiner")
    }

    val availableLanguages: List<LanguageInfo>
        get() = languages.values.stream().toList()

    fun getLanguageInfo(code: String?): LanguageInfo? {
        return languages[code]
    }
}
