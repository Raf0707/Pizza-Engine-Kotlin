package pize.tests.voxelgame.server.chunk.gen

import pize.tests.voxelgame.server.chunk.ServerChunk

interface ChunkGenerator {
    fun generate(chunk: ServerChunk)
    fun generateDecorations(chunk: ServerChunk?)
    val iD: String
}
