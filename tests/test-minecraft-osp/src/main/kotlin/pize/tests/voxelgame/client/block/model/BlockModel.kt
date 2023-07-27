package pize.tests.voxelgame.client.block.model

import pize.graphics.texture.Region
import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.BlockRotation
import pize.tests.voxelgame.client.chunk.mesh.ChunkMesh
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshType
import pize.tests.voxelgame.client.chunk.mesh.builder.ChunkBuilder
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.chunk.ChunkUtils
import java.util.*

class BlockModel(val meshType: ChunkMeshType) {
    private val faces: MutableList<Face?>
    private val nxFaces: MutableList<Face?>
    private val pxFaces: MutableList<Face?>
    private val nyFaces: MutableList<Face?>
    private val pyFaces: MutableList<Face?>
    private val nzFaces: MutableList<Face?>
    private val pzFaces: MutableList<Face?>
    private val facesFromDirection: MutableMap<Direction, List<Face?>>
    private val transparentForNeighbors: BooleanArray

    init {
        faces = ArrayList()
        nxFaces = ArrayList()
        pxFaces = ArrayList()
        nyFaces = ArrayList()
        pyFaces = ArrayList()
        nzFaces = ArrayList()
        pzFaces = ArrayList()
        facesFromDirection = HashMap()
        facesFromDirection[Direction.NEGATIVE_X] = nxFaces
        facesFromDirection[Direction.POSITIVE_X] = pxFaces
        facesFromDirection[Direction.NEGATIVE_Y] = nyFaces
        facesFromDirection[Direction.POSITIVE_Y] = pyFaces
        facesFromDirection[Direction.NEGATIVE_Z] = nzFaces
        facesFromDirection[Direction.POSITIVE_Z] = pzFaces
        facesFromDirection[Direction.NONE] = faces
        transparentForNeighbors = BooleanArray(6)
    }

    private fun getChunkBuilderMesh(chunkBuilder: ChunkBuilder): ChunkMesh {
        return when (meshType) {
            ChunkMeshType.SOLID -> chunkBuilder.solidMesh!!
            ChunkMeshType.CUSTOM -> chunkBuilder.customMesh!!
            ChunkMeshType.TRANSLUCENT -> chunkBuilder.translucentMesh!!
        }
    }

    fun getDirectionFaces(direction: Direction): List<Face?> {
        return facesFromDirection[direction]!!
    }

    fun getFacesFromNormal(x: Int, y: Int, z: Int): List<Face?> {
        return getDirectionFaces(Direction.Companion.fromNormal(x, y, z))
    }

    fun isFaceTransparentForNeighbors(x: Int, y: Int, z: Int): Boolean {
        val index: Int = Direction.Companion.fromNormal(x, y, z).ordinal
        return if (index > 5) false else transparentForNeighbors[index]
    }

    fun setFacesTransparentForNeighbors(vararg transparentForNeighbors: Boolean): BlockModel {
        System.arraycopy(transparentForNeighbors, 0, this.transparentForNeighbors, 0, transparentForNeighbors.size)
        return this
    }

    fun setFacesTransparentForNeighbors(transparentForNeighbors: Boolean): BlockModel {
        Arrays.fill(this.transparentForNeighbors, transparentForNeighbors)
        return this
    }

    fun face(face: Face?): BlockModel {
        faces.add(face)
        return this
    }

    fun face(direction: Direction?, face: Face?): BlockModel {
        when (direction) {
            Direction.NEGATIVE_X -> nxFaces.add(face)
            Direction.POSITIVE_X -> pxFaces.add(face)
            Direction.NEGATIVE_Y -> nyFaces.add(face)
            Direction.POSITIVE_Y -> pyFaces.add(face)
            Direction.NEGATIVE_Z -> nzFaces.add(face)
            Direction.POSITIVE_Z -> pzFaces.add(face)
            Direction.NONE -> faces.add(face)
        }
        return this
    }

    @JvmOverloads
    fun nxFace(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nxFaces.add(Face(Quad.Companion.getNxQuad(), region, color))
        return this
    }

    @JvmOverloads
    fun pxFace(region: Region?, color: IColor = Color.WHITE): BlockModel {
        pxFaces.add(Face(Quad.Companion.getPxQuad(), region, color))
        return this
    }

    @JvmOverloads
    fun nyFace(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nyFaces.add(Face(Quad.Companion.getNyQuad(), region, color))
        return this
    }

    @JvmOverloads
    fun pyFace(region: Region?, color: IColor = Color.WHITE): BlockModel {
        pyFaces.add(Face(Quad.Companion.getPyQuad(), region, color))
        return this
    }

    @JvmOverloads
    fun nzFace(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nzFaces.add(Face(Quad.Companion.getNzQuad(), region, color))
        return this
    }

    @JvmOverloads
    fun pzFace(region: Region?, color: IColor = Color.WHITE): BlockModel {
        pzFaces.add(Face(Quad.Companion.getPzQuad(), region, color))
        return this
    }

    @JvmOverloads
    fun allFaces(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nxFace(region, color)
        pxFace(region, color)
        nyFace(region, color)
        pyFace(region, color)
        nzFace(region, color)
        pzFace(region, color)
        return this
    }

    @JvmOverloads
    fun sideXZFaces(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nxFace(region, color)
        pxFace(region, color)
        nzFace(region, color)
        pzFace(region, color)
        return this
    }

    @JvmOverloads
    fun sideXYFaces(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nxFace(region, color)
        pxFace(region, color)
        nyFace(region, color)
        pyFace(region, color)
        return this
    }

    @JvmOverloads
    fun sideZYFaces(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nzFace(region, color)
        pzFace(region, color)
        nyFace(region, color)
        pyFace(region, color)
        return this
    }

    @JvmOverloads
    fun xFaces(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nxFace(region, color)
        pxFace(region, color)
        return this
    }

    @JvmOverloads
    fun yFaces(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nyFace(region, color)
        pyFace(region, color)
        return this
    }

    @JvmOverloads
    fun zFaces(region: Region?, color: IColor = Color.WHITE): BlockModel {
        nzFace(region, color)
        pzFace(region, color)
        return this
    }

    fun build(builder: ChunkBuilder, block: BlockProperties?, lx: Int, y: Int, lz: Int) {
        if (meshType == ChunkMeshType.CUSTOM) {
            // Custom faces
            val light = builder.chunk!!.getLight(lx, y, lz) / 15f
            for (face in faces) face!!.putFloats(
                builder.customMesh.verticesList,
                lx.toFloat(),
                y.toFloat(),
                lz.toFloat(),
                light,
                light,
                light,
                1f
            )
        } else {
            // Solid faces
            buildSolidFaces(builder, block, lx, y, lz)
        }
    }

    private fun buildSolidFaces(builder: ChunkBuilder, block: BlockProperties?, lx: Int, y: Int, lz: Int) {
        if (builder.isGenSolidFace(lx, y, lz, -1, 0, 0, block)) for (face in nxFaces) buildNxFace(
            builder,
            face,
            lx,
            y,
            lz
        )
        if (builder.isGenSolidFace(lx, y, lz, +1, 0, 0, block)) for (face in pxFaces) buildPxFace(
            builder,
            face,
            lx,
            y,
            lz
        )
        if (builder.isGenSolidFace(lx, y, lz, 0, -1, 0, block)) for (face in nyFaces) buildNyFace(
            builder,
            face,
            lx,
            y,
            lz
        )
        if (builder.isGenSolidFace(lx, y, lz, 0, +1, 0, block)) for (face in pyFaces) buildPyFace(
            builder,
            face,
            lx,
            y,
            lz
        )
        if (builder.isGenSolidFace(lx, y, lz, 0, 0, -1, block)) for (face in nzFaces) buildNzFace(
            builder,
            face,
            lx,
            y,
            lz
        )
        if (builder.isGenSolidFace(lx, y, lz, 0, 0, +1, block)) for (face in pzFaces) buildPzFace(
            builder,
            face,
            lx,
            y,
            lz
        )
    }

    private fun buildNxFace(builder: ChunkBuilder, face: Face?, x: Int, y: Int, z: Int) {
        val light1 = builder.getLight(
            x - 1,
            y,
            z,
            x - 1,
            y,
            z + 1,
            x - 1,
            y + 1,
            z + 1,
            x - 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light2 = builder.getLight(
            x - 1,
            y,
            z,
            x - 1,
            y,
            z + 1,
            x - 1,
            y - 1,
            z + 1,
            x - 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light3 = builder.getLight(
            x - 1,
            y,
            z,
            x - 1,
            y,
            z - 1,
            x - 1,
            y - 1,
            z - 1,
            x - 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light4 = builder.getLight(
            x - 1,
            y,
            z,
            x - 1,
            y,
            z - 1,
            x - 1,
            y + 1,
            z - 1,
            x - 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val ao1 = builder.getAO(x - 1, y + 1, z, x - 1, y, z + 1, x - 1, y + 1, z + 1, x - 1, y, z)
        val ao2 = builder.getAO(x - 1, y - 1, z, x - 1, y, z + 1, x - 1, y - 1, z + 1, x - 1, y, z)
        val ao3 = builder.getAO(x - 1, y - 1, z, x - 1, y, z - 1, x - 1, y - 1, z - 1, x - 1, y, z)
        val ao4 = builder.getAO(x - 1, y + 1, z, x - 1, y, z - 1, x - 1, y + 1, z - 1, x - 1, y, z)
        val shadow = 0.8f
        val brightness1 = shadow * ao1 * light1
        val brightness2 = shadow * ao2 * light2
        val brightness3 = shadow * ao3 * light3
        val brightness4 = shadow * ao4 * light4
        val mesh = if (meshType == ChunkMeshType.TRANSLUCENT) builder.translucentMesh else builder.solidMesh
        if (brightness2 + brightness4 > brightness1 + brightness3) face!!.putIntsPackedFlipped(
            mesh.verticesList,
            x,
            y,
            z,
            brightness1,
            brightness2,
            brightness3,
            brightness4
        ) else face!!.putIntsPacked(mesh.verticesList, x, y, z, brightness1, brightness2, brightness3, brightness4)
    }

    private fun buildPxFace(builder: ChunkBuilder, face: Face?, x: Int, y: Int, z: Int) {
        val light1 = builder.getLight(
            x + 1,
            y,
            z,
            x + 1,
            y,
            z - 1,
            x + 1,
            y + 1,
            z - 1,
            x + 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light2 = builder.getLight(
            x + 1,
            y,
            z,
            x + 1,
            y,
            z - 1,
            x + 1,
            y - 1,
            z - 1,
            x + 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light3 = builder.getLight(
            x + 1,
            y,
            z,
            x + 1,
            y,
            z + 1,
            x + 1,
            y - 1,
            z + 1,
            x + 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light4 = builder.getLight(
            x + 1,
            y,
            z,
            x + 1,
            y,
            z + 1,
            x + 1,
            y + 1,
            z + 1,
            x + 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val ao1 = builder.getAO(x + 1, y + 1, z, x + 1, y, z - 1, x + 1, y + 1, z - 1, x + 1, y, z)
        val ao2 = builder.getAO(x + 1, y - 1, z, x + 1, y, z - 1, x + 1, y - 1, z - 1, x + 1, y, z)
        val ao3 = builder.getAO(x + 1, y - 1, z, x + 1, y, z + 1, x + 1, y - 1, z + 1, x + 1, y, z)
        val ao4 = builder.getAO(x + 1, y + 1, z, x + 1, y, z + 1, x + 1, y + 1, z + 1, x + 1, y, z)
        val shadow = 0.8f
        val brightness1 = shadow * ao1 * light1
        val brightness2 = shadow * ao2 * light2
        val brightness3 = shadow * ao3 * light3
        val brightness4 = shadow * ao4 * light4
        val mesh = if (meshType == ChunkMeshType.TRANSLUCENT) builder.translucentMesh else builder.solidMesh
        if (brightness2 + brightness4 > brightness1 + brightness3) face!!.putIntsPackedFlipped(
            mesh.verticesList,
            x,
            y,
            z,
            brightness1,
            brightness2,
            brightness3,
            brightness4
        ) else face!!.putIntsPacked(mesh.verticesList, x, y, z, brightness1, brightness2, brightness3, brightness4)
    }

    private fun buildNyFace(builder: ChunkBuilder, face: Face?, x: Int, y: Int, z: Int) {
        val light1 = builder.getLight(
            x,
            y - 1,
            z,
            x,
            y - 1,
            z + 1,
            x + 1,
            y - 1,
            z + 1,
            x + 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light2 = builder.getLight(
            x,
            y - 1,
            z,
            x,
            y - 1,
            z - 1,
            x + 1,
            y - 1,
            z - 1,
            x + 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light3 = builder.getLight(
            x,
            y - 1,
            z,
            x,
            y - 1,
            z - 1,
            x - 1,
            y - 1,
            z - 1,
            x - 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light4 = builder.getLight(
            x,
            y - 1,
            z,
            x,
            y - 1,
            z + 1,
            x - 1,
            y - 1,
            z + 1,
            x - 1,
            y - 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val ao1 = builder.getAO(x + 1, y - 1, z, x, y - 1, z + 1, x + 1, y - 1, z + 1, x, y - 1, z)
        val ao2 = builder.getAO(x + 1, y - 1, z, x, y - 1, z - 1, x + 1, y - 1, z - 1, x, y - 1, z)
        val ao3 = builder.getAO(x - 1, y - 1, z, x, y - 1, z - 1, x - 1, y - 1, z - 1, x, y - 1, z)
        val ao4 = builder.getAO(x - 1, y - 1, z, x, y - 1, z + 1, x - 1, y - 1, z + 1, x, y - 1, z)
        val shadow = 0.6f
        val brightness1 = shadow * ao1 * light1
        val brightness2 = shadow * ao2 * light2
        val brightness3 = shadow * ao3 * light3
        val brightness4 = shadow * ao4 * light4
        val mesh = if (meshType == ChunkMeshType.TRANSLUCENT) builder.translucentMesh else builder.solidMesh
        if (brightness2 + brightness4 > brightness1 + brightness3) face!!.putIntsPackedFlipped(
            mesh.verticesList,
            x,
            y,
            z,
            brightness1,
            brightness2,
            brightness3,
            brightness4
        ) else face!!.putIntsPacked(mesh.verticesList, x, y, z, brightness1, brightness2, brightness3, brightness4)
    }

    private fun buildPyFace(builder: ChunkBuilder, face: Face?, x: Int, y: Int, z: Int) {
        val light1 = builder.getLight(
            x,
            y + 1,
            z,
            x,
            y + 1,
            z - 1,
            x + 1,
            y + 1,
            z - 1,
            x + 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light2 = builder.getLight(
            x,
            y + 1,
            z,
            x,
            y + 1,
            z + 1,
            x + 1,
            y + 1,
            z + 1,
            x + 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light3 = builder.getLight(
            x,
            y + 1,
            z,
            x,
            y + 1,
            z + 1,
            x - 1,
            y + 1,
            z + 1,
            x - 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light4 = builder.getLight(
            x,
            y + 1,
            z,
            x,
            y + 1,
            z - 1,
            x - 1,
            y + 1,
            z - 1,
            x - 1,
            y + 1,
            z
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val ao1 = builder.getAO(x + 1, y + 1, z, x, y + 1, z - 1, x + 1, y + 1, z - 1, x, y + 1, z)
        val ao2 = builder.getAO(x + 1, y + 1, z, x, y + 1, z + 1, x + 1, y + 1, z + 1, x, y + 1, z)
        val ao3 = builder.getAO(x - 1, y + 1, z, x, y + 1, z + 1, x - 1, y + 1, z + 1, x, y + 1, z)
        val ao4 = builder.getAO(x - 1, y + 1, z, x, y + 1, z - 1, x - 1, y + 1, z - 1, x, y + 1, z)
        val shadow = 1f
        val brightness1 = shadow * ao1 * light1
        val brightness2 = shadow * ao2 * light2
        val brightness3 = shadow * ao3 * light3
        val brightness4 = shadow * ao4 * light4
        val mesh = if (meshType == ChunkMeshType.TRANSLUCENT) builder.translucentMesh else builder.solidMesh
        if (brightness2 + brightness4 > brightness1 + brightness3) face!!.putIntsPackedFlipped(
            mesh.verticesList,
            x,
            y,
            z,
            brightness1,
            brightness2,
            brightness3,
            brightness4
        ) else face!!.putIntsPacked(mesh.verticesList, x, y, z, brightness1, brightness2, brightness3, brightness4)
    }

    private fun buildNzFace(builder: ChunkBuilder, face: Face?, x: Int, y: Int, z: Int) {
        val light1 = builder.getLight(
            x,
            y,
            z - 1,
            x,
            y + 1,
            z - 1,
            x - 1,
            y + 1,
            z - 1,
            x,
            y + 1,
            z - 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light2 = builder.getLight(
            x,
            y,
            z - 1,
            x,
            y - 1,
            z - 1,
            x - 1,
            y - 1,
            z - 1,
            x,
            y - 1,
            z - 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light3 = builder.getLight(
            x,
            y,
            z - 1,
            x,
            y - 1,
            z - 1,
            x + 1,
            y - 1,
            z - 1,
            x,
            y - 1,
            z - 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light4 = builder.getLight(
            x,
            y,
            z - 1,
            x,
            y + 1,
            z - 1,
            x + 1,
            y + 1,
            z - 1,
            x,
            y + 1,
            z - 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val ao1 = builder.getAO(x - 1, y, z - 1, x, y + 1, z - 1, x - 1, y + 1, z - 1, x, y, z - 1)
        val ao2 = builder.getAO(x - 1, y, z - 1, x, y - 1, z - 1, x - 1, y - 1, z - 1, x, y, z - 1)
        val ao3 = builder.getAO(x + 1, y, z - 1, x, y - 1, z - 1, x + 1, y - 1, z - 1, x, y, z - 1)
        val ao4 = builder.getAO(x + 1, y, z - 1, x, y + 1, z - 1, x + 1, y + 1, z - 1, x, y, z - 1)
        val shadow = 0.7f
        val brightness1 = shadow * ao1 * light1
        val brightness2 = shadow * ao2 * light2
        val brightness3 = shadow * ao3 * light3
        val brightness4 = shadow * ao4 * light4
        val mesh = if (meshType == ChunkMeshType.TRANSLUCENT) builder.translucentMesh else builder.solidMesh
        if (brightness2 + brightness4 > brightness1 + brightness3) face!!.putIntsPackedFlipped(
            mesh.verticesList,
            x,
            y,
            z,
            brightness1,
            brightness2,
            brightness3,
            brightness4
        ) else face!!.putIntsPacked(mesh.verticesList, x, y, z, brightness1, brightness2, brightness3, brightness4)
    }

    private fun buildPzFace(builder: ChunkBuilder, face: Face?, x: Int, y: Int, z: Int) {
        val light1 = builder.getLight(
            x,
            y,
            z + 1,
            x,
            y + 1,
            z + 1,
            x + 1,
            y + 1,
            z + 1,
            x + 1,
            y,
            z + 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light2 = builder.getLight(
            x,
            y,
            z + 1,
            x,
            y - 1,
            z + 1,
            x + 1,
            y - 1,
            z + 1,
            x + 1,
            y,
            z + 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light3 = builder.getLight(
            x,
            y,
            z + 1,
            x,
            y - 1,
            z + 1,
            x - 1,
            y - 1,
            z + 1,
            x - 1,
            y,
            z + 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val light4 = builder.getLight(
            x,
            y,
            z + 1,
            x,
            y + 1,
            z + 1,
            x - 1,
            y + 1,
            z + 1,
            x - 1,
            y,
            z + 1
        ) / ChunkUtils.MAX_LIGHT_LEVEL
        val ao1 = builder.getAO(x + 1, y, z + 1, x, y + 1, z + 1, x + 1, y + 1, z + 1, x, y, z + 1)
        val ao2 = builder.getAO(x + 1, y, z + 1, x, y - 1, z + 1, x + 1, y - 1, z + 1, x, y, z + 1)
        val ao3 = builder.getAO(x - 1, y, z + 1, x, y - 1, z + 1, x - 1, y - 1, z + 1, x, y, z + 1)
        val ao4 = builder.getAO(x - 1, y, z + 1, x, y + 1, z + 1, x - 1, y + 1, z + 1, x, y, z + 1)
        val shadow = 0.7f
        val brightness1 = shadow * ao1 * light1
        val brightness2 = shadow * ao2 * light2
        val brightness3 = shadow * ao3 * light3
        val brightness4 = shadow * ao4 * light4
        val mesh = if (meshType == ChunkMeshType.TRANSLUCENT) builder.translucentMesh else builder.solidMesh
        if (brightness2 + brightness4 > brightness1 + brightness3) face!!.putIntsPackedFlipped(
            mesh.verticesList,
            x,
            y,
            z,
            brightness1,
            brightness2,
            brightness3,
            brightness4
        ) else face!!.putIntsPacked(mesh.verticesList, x, y, z, brightness1, brightness2, brightness3, brightness4)
    }

    fun rotated(rotation: BlockRotation): BlockModel {
        val model = BlockModel(meshType)
        for (face in faces) model.face(face!!.rotated(rotation))
        for (face in nxFaces) {
            val normal = Direction.NEGATIVE_X.normal.copy().mul(rotation.matrix)
            model.face(Direction.Companion.fromNormal(normal), face!!.rotated(rotation))
        }
        for (face in pxFaces) {
            val normal = Direction.POSITIVE_X.normal.copy().mul(rotation.matrix)
            model.face(Direction.Companion.fromNormal(normal), face!!.rotated(rotation))
        }
        for (face in nyFaces) {
            val normal = Direction.NEGATIVE_Y.normal.copy().mul(rotation.matrix)
            model.face(Direction.Companion.fromNormal(normal), face!!.rotated(rotation))
        }
        for (face in pyFaces) {
            val normal = Direction.POSITIVE_Y.normal.copy().mul(rotation.matrix)
            model.face(Direction.Companion.fromNormal(normal), face!!.rotated(rotation))
        }
        for (face in nzFaces) {
            val normal = Direction.NEGATIVE_Z.normal.copy().mul(rotation.matrix)
            model.face(Direction.Companion.fromNormal(normal), face!!.rotated(rotation))
        }
        for (face in pzFaces) {
            val normal = Direction.POSITIVE_Z.normal.copy().mul(rotation.matrix)
            model.face(Direction.Companion.fromNormal(normal), face!!.rotated(rotation))
        }
        return model
    }
}
