package pize.tests.minecraft.game.options

enum class SoundCategory private constructor(// Погода
    val defaultVolume: Float, val translateKey: String
) {
    MASTER(1f, "audioSettings.master"),

    // Общая Громкость
    MUSIC(0.1f, "audioSettings.music"),

    // Музыка
    AMBIENT(1f, "audioSettings.ambient"),

    // Окружение
    PLAYERS(1f, "audioSettings.players"),

    // Игроки
    BLOCKS(1f, "audioSettings.blocks"),

    // Блоки
    WEATHER(0.1f, "audioSettings.weather")

}
