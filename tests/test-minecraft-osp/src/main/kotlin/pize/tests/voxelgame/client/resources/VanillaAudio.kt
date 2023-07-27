package pize.tests.voxelgame.client.resources

object VanillaAudio {
    private const val SOUND_DIR = "sound/"
    fun register(resources: GameResources) {
        println("[Resources]: Load Audio")

        // Random //
        resources.registerSound(SOUND_DIR, "random/click")
        resources.registerSound(SOUND_DIR, "random/explode", 1, 4)
        resources.registerSound(SOUND_DIR, "random/glass", 1, 3)
        resources.registerSound(SOUND_DIR, "random/levelup")
        // Damage //
        resources.registerSound(SOUND_DIR, "damage/fallbig")
        resources.registerSound(SOUND_DIR, "damage/fallsmall")
        resources.registerSound(SOUND_DIR, "damage/hit", 1, 3)
        // Dig //
        resources.registerSound(SOUND_DIR, "dig/grass", 1, 4)
        resources.registerSound(SOUND_DIR, "dig/stone", 1, 4)
        resources.registerSound(SOUND_DIR, "dig/wood", 1, 4)
        resources.registerSound(SOUND_DIR, "dig/sand", 1, 4)
    }
}
