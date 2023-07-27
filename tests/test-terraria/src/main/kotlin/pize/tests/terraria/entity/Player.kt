package pize.tests.terraria.entity

import pize.Pize.dt
import pize.Pize.x
import pize.Pize.y
import pize.graphics.camera.Camera2D.x
import pize.graphics.camera.Camera2D.y
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.math.Maths.floor
import pize.math.vecmath.vector.Vec2f
import pize.math.vecmath.vector.Vec2f.mul
import pize.physic.BoundingBox2
import pize.physic.Collider2f.getCollidedMotion
import pize.physic.RectBody
import pize.tests.terraria.map.WorldMap
import pize.tests.terraria.world.World

class Player : Entity(BoundingBox2(0.0, 0.0, 2.0, 3.0)) {
    private val rectList: MutableList<RectBody>

    init {
        rectList = ArrayList()
        velocity.setMax(50f)
    }

    override fun render(batch: TextureBatch) {
        batch.drawQuad(1.0, 1.0, 1.0, 1.0, pos().x, pos().y, rect()!!.width, rect()!!.height)
        for (r in rectList) batch.drawQuad(1.0, 0.0, 0.0, 0.5, r.min.x, r.min.y, r.rect()!!.width, r.rect()!!.height)
    }

    fun update(world: World?) {
        val tileMap = world.getTileMap()

        // Getting the nearest tiles
        var rects = getRects(tileMap, Vec2f(), 1f)
        val isCollideUp = isCollide(0f, Float.MIN_VALUE, rects)
        val isCollideDown = isCollide(0f, -Float.MIN_VALUE, rects)
        val isCollideLeft = isCollide(-Float.MIN_VALUE, 0f, rects)
        val isCollideRight = isCollide(Float.MIN_VALUE, 0f, rects)

        // Moving
        val delta = dt
        if (Key.A.isPressed) velocity.x -= 0.7.toFloat()
        if (Key.D.isPressed) velocity.x += 0.7.toFloat()

        // Auto jump
        if (isCollideDown && !isCollideUp) {
            val rectBody = copy()
            rectBody.pos().y++
            if ((velocity.x > 0 && isCollideRight
                        && !getCollidedMotion(rectBody, Vec2f(Float.MIN_VALUE, 0f), *rects).isZero)
                ||
                (velocity.x < 0 && isCollideLeft
                        && !getCollidedMotion(rectBody, Vec2f(-Float.MIN_VALUE, 0f), *rects).isZero)
            ) velocity.y = 21f
        }

        // Gravity & Jump
        velocity.y -= 2f
        if (Key.SPACE.isPressed && isCollideDown) velocity.y = 50f

        // Process collisions
        val motion: Vec2f = velocity.copy().mul(delta)
        rects = getRects(tileMap, motion, 0f)
        val collidedVel = getCollidedMotion(this, motion, *rects)
        velocity.reduce(0.5)
        velocity.collidedAxesToZero(collidedVel)
        velocity.clampToMax()
        pos().add(collidedVel)
    }

    fun isCollide(x: Float, y: Float, rects: Array<RectBody>): Boolean {
        return getCollidedMotion(this, Vec2f(x, y), *rects).isZero
    }

    fun getRects(map: WorldMap?, vel: Vec2f, padding: Float): Array<RectBody> {
        rectList.clear()
        var i = floor((min.x + vel.x - padding).toDouble())
        while (i < max.x + vel.x + padding) {
            var j = floor((min.y + vel.y - padding).toDouble())
            while (j < max.y + vel.y + padding) {
                val tile = map!!.getTile(i, j)
                if (tile != null && tile.type.collidable) {
                    val body = RectBody(TILE_BOUNDING_RECT)
                    body.pos()[i.toFloat()] = j.toFloat()
                    rectList.add(body)
                }
                j++
            }
            i++
        }
        return rectList.toTypedArray<RectBody>()
    }

    companion object {
        private val TILE_BOUNDING_RECT = BoundingBox2(0.0, 0.0, 1.0, 1.0)
    }
}
