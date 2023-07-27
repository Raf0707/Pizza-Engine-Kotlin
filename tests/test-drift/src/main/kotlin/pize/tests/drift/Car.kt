package pize.tests.drift

import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.math.Mathc.asin
import pize.math.Mathc.sqrt
import pize.math.Maths
import pize.math.Maths.clamp
import pize.math.Maths.cosDeg
import pize.math.Maths.sinDeg
import pize.math.vecmath.vector.Vec2f
import kotlin.math.abs

class Car : CarPart(Texture("coin.png"), 282f, 612f) {
    private val wheelFront1: Wheel?
    private val wheelFront2: Wheel?
    private val wheelBack1: Wheel?
    private val wheelBack2: Wheel?
    private var backWheelsVelocity = 0f
    private var wheelsRotation = 0f

    init {
        super.setOrigin<CarPart>(0.5, 0.288)
        wheelFront1 = Wheel(Texture("wheel.png"), 28f, 75f).setParent<CarPart>(this).setPosition<CarPart>(-105.0, 340.0)
            .setOrigin(0.8, 0.5)
        wheelFront2 = Wheel(Texture("wheel.png"), 28f, 75f).setParent<CarPart>(this).setPosition<CarPart>(105.0, 340.0)
            .setOrigin(0.2, 0.5)
        wheelBack1 = Wheel(Texture("wheel.png"), 28f, 75f).setParent<CarPart>(this).setPosition<CarPart>(-105.0, 0.0)
            .setOrigin(0.8, 0.5)
        wheelBack2 = Wheel(Texture("wheel.png"), 28f, 75f).setParent<CarPart>(this).setPosition<CarPart>(105.0, 0.0)
            .setOrigin(0.2, 0.5)
    }

    fun update() {
        // Control moving
        backWheelsVelocity *= 0.995.toFloat()
        if (Key.W.isPressed) backWheelsVelocity += 0.3.toFloat()
        if (Key.S.isPressed) backWheelsVelocity -= 0.3.toFloat()

        // Control wheels rotation
        // wheelsRotation = -(Pize.getX() - Pize.getWidth() / 2F) / Pize.getWidth() * 500;
        // wheelsRotation = Mathc.pow(Math.abs(wheelsRotation), 0.75) * Math.signum(wheelsRotation);
        if (Key.A.isPressed) wheelsRotation += 0.4.toFloat()
        if (Key.D.isPressed) wheelsRotation -= 0.4.toFloat()
        wheelsRotation = clamp(wheelsRotation, -65f, 65f)

        // Velocity
        val directedLocalVelocity = Vec2f(0f, abs(cosDeg(wheelsRotation.toDouble()).toDouble()) * backWheelsVelocity)
        val velocity = Vec2f(directedLocalVelocity.copy().rotDeg(rotation.toDouble()))
        super.getPosition().add(velocity)

        // Rotation
        super.rotate(
            asin((backWheelsVelocity / sqrt((340 * 340 + backWheelsVelocity * backWheelsVelocity).toDouble())).toDouble()) * 180 / Maths.PI * sinDeg(
                wheelsRotation.toDouble()
            )
        )
        wheelFront1!!.setRotation<CarPart>(wheelsRotation)
        wheelFront2!!.setRotation<CarPart>(wheelsRotation)
    }

    override fun render(batch: TextureBatch) {
        super.render(batch)
        wheelFront1!!.render(batch)
        wheelFront2!!.render(batch)
        wheelBack1!!.render(batch)
        wheelBack2!!.render(batch)
        batch.rotate(0f)
    }

    override fun dispose() {
        super.dispose()
        wheelFront1!!.dispose()
        wheelFront2!!.dispose()
        wheelBack1!!.dispose()
        wheelBack2!!.dispose()
    }
}
