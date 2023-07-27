package pize.tests.voxelgame.client.chunk.mesh

class ChunkMeshTranslucentCullingOn : ChunkMeshPackedCullingOn() {
    override val type: ChunkMeshType?
        get() = ChunkMeshType.TRANSLUCENT
}
