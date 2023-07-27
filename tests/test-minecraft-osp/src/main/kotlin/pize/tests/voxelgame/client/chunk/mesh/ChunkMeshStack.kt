package pize.tests.voxelgame.client.chunk.mesh

import pize.app.Disposable

class ChunkMeshStack : Disposable {
    val packed: ChunkMeshPackedCullingOn
    val custom: ChunkMeshCullingOff
    val translucent: ChunkMeshTranslucentCullingOn

    init {
        packed = ChunkMeshPackedCullingOn()
        custom = ChunkMeshCullingOff()
        translucent = ChunkMeshTranslucentCullingOn()
    }

    override fun dispose() {
        packed.dispose()
        custom.dispose()
        translucent.dispose()
    }
}
