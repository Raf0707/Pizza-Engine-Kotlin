package pize.tests.voxelgame.client.resources

object VanillaBlocks {
    private const val BLOCK_DIR = "texture/block/faithful/"
    fun register(resources: GameResources) {
        println("[Resources]: Load Block Atlas")
        resources.registerBlockRegion(BLOCK_DIR, "grass_block_side")
        resources.registerBlockRegion(BLOCK_DIR, "grass_block_side_overlay")
        resources.registerBlockRegion(BLOCK_DIR, "grass_block_top")
        resources.registerBlockRegion(BLOCK_DIR, "dirt")
        resources.registerBlockRegion(BLOCK_DIR, "stone")
        resources.registerBlockRegion(BLOCK_DIR, "grass")
        resources.registerBlockRegion(BLOCK_DIR, "glass")
        resources.registerBlockRegion(BLOCK_DIR, "oak_log")
        resources.registerBlockRegion(BLOCK_DIR, "oak_log_top")
        resources.registerBlockRegion(BLOCK_DIR, "redstone_lamp_on")
        resources.registerBlockRegion(BLOCK_DIR, "redstone_lamp")
        resources.registerBlockRegion(BLOCK_DIR, "oak_leaves")
        resources.registerBlockRegion(BLOCK_DIR, "water")
        resources.registerBlockRegion(BLOCK_DIR, "sand")
    }
}
