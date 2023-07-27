package pize.tests.voxelgame.main.audio

enum class BlockSoundPack(val placeSounds: SoundGroup, val destroySounds: SoundGroup, val stepSounds: SoundGroup) {
    GRASS(SoundGroup.DIG_GRASS, SoundGroup.DIG_GRASS, SoundGroup.DIG_GRASS),
    STONE(SoundGroup.DIG_STONE, SoundGroup.DIG_STONE, SoundGroup.DIG_STONE),
    WOOD(SoundGroup.DIG_WOOD, SoundGroup.DIG_WOOD, SoundGroup.DIG_WOOD),
    SAND(SoundGroup.DIG_SAND, SoundGroup.DIG_SAND, SoundGroup.DIG_SAND),
    GLASS(SoundGroup.DIG_GLASS, SoundGroup.DIG_GLASS, SoundGroup.DIG_GLASS);

    fun randomPlaceSound(): Sound? {
        return placeSounds.random()
    }

    fun randomDestroySound(): Sound? {
        return destroySounds.random()
    }

    fun randomStepSound(): Sound? {
        return stepSounds.random()
    }
}
