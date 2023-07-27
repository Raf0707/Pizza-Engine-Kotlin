package pize.tests.voxelgame.client.resources

object VanillaMusic {
    private const val SOUND_DIR = "music/"
    private const val SOUND_GAME_DIR = SOUND_DIR + "game/"
    private const val SOUND_MENU_DIR = SOUND_DIR + "menu/"
    fun register(resources: GameResources) {
        println("[Resources]: Load Music")

        // Menu //
        resources.registerSound(SOUND_MENU_DIR, "mutation")
        // Game //
        resources.registerSound(SOUND_GAME_DIR, "wet_hands")
    }
}
