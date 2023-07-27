package pize.graphics.gl

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL41
import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import java.nio.FloatBuffer
import java.nio.IntBuffer

object Gl {
    @JvmStatic
    @JvmOverloads
    fun clearColor(r: Float, g: Float, b: Float, a: Float = 1f) {
        GL11.glClearColor(r, g, b, a)
    }

    @JvmStatic
    @JvmOverloads
    fun clearColor(r: Double, g: Double, b: Double, a: Double = 1.0) {
        GL11.glClearColor(r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())
    }

    @JvmStatic
    fun clearColor(color: IColor) {
        clearColor(color.r(), color.g(), color.b(), color.a())
    }

    @JvmStatic
    fun clearColorBuffer() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
    }

    fun clearDepthBuffer() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT)
    }

    fun clearStencilBuffer() {
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)
    }

    @JvmStatic
    fun clearColorDepthBuffers() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    fun clearCDSBuffers() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)
    }

    fun isEnabled(target: Target): Boolean {
        return GL11.glIsEnabled(target.GL)
    }

    fun isEnabled(target: Target, index: Int): Boolean {
        return GL30.glIsEnabledi(target.GL, index)
    }

    @JvmStatic
    fun enable(target: Target) {
        GL11.glEnable(target.GL)
    }

    fun enable(target: Target, index: Int) {
        GL30.glEnablei(target.GL, index)
    }

    fun enable(vararg targets: Target) {
        for (target in targets) enable(target)
    }

    @JvmStatic
    fun disable(target: Target) {
        GL11.glDisable(target.GL)
    }

    fun disable(target: Target, index: Int) {
        GL30.glDisablei(target.GL, index)
    }

    fun disable(vararg targets: Target) {
        for (target in targets) disable(target)
    }

    fun blendFunc(sFactor: BlendFactor, dFactor: BlendFactor) {
        GL11.glBlendFunc(sFactor.GL, dFactor.GL)
    }

    @JvmStatic
    fun depthFunc(func: DepthFunc) {
        GL11.glDepthFunc(func.GL)
    }

    @JvmStatic
    fun depthMask(flag: Boolean) {
        GL11.glDepthMask(flag)
    }

    @JvmStatic
    fun cullFace(mode: Face) {
        GL11.glCullFace(mode.GL)
    }

    fun pointSize(size: Float) {
        GL11.glPointSize(size)
    }

    @JvmStatic
    fun lineWidth(width: Float) {
        GL11.glLineWidth(width)
    }

    fun viewport(x: Int, y: Int, width: Int, height: Int) {
        GL11.glViewport(x, y, width, height)
    }

    fun viewport(width: Int, height: Int) {
        viewport(0, 0, width, height)
    }

    fun viewportIndexed(index: Int, x: Float, y: Float, width: Float, height: Float) {
        GL41.glViewportIndexedf(index, x, y, width, height)
    }

    fun viewportIndexed(index: Int, array: FloatArray?) {
        GL41.glViewportIndexedfv(index, array)
    }

    fun viewportIndexed(index: Int, buffer: FloatBuffer?) {
        GL41.glViewportIndexedfv(index, buffer)
    }

    fun viewportArray(first: Int, array: FloatArray?) {
        GL41.glViewportArrayv(first, array)
    }

    fun viewportArray(first: Int, buffer: FloatBuffer?) {
        GL41.glViewportArrayv(first, buffer)
    }

    @JvmStatic
    fun polygonMode(face: Face, mode: PolygonMode) {
        GL11.glPolygonMode(face.GL, mode.GL)
    }

    @JvmStatic
    fun polygonOffset(factor: Float, units: Float) {
        GL11.glPolygonOffset(factor, units)
    }

    fun scissor(x: Int, y: Int, width: Int, height: Int) {
        GL11.glScissor(x, y, width, height)
    }

    fun scissorIndexed(index: Int, x: Int, y: Int, width: Int, height: Int) {
        GL41.glScissorIndexed(index, x, y, width, height)
    }

    fun scissorArray(first: Int, array: IntArray?) {
        GL41.glScissorArrayv(first, array)
    }

    fun scissorArray(first: Int, buffer: IntBuffer?) {
        GL41.glScissorArrayv(first, buffer)
    }

    fun hint(hint: Hint, mode: Mode) {
        GL11.glHint(hint.GL, mode.GL)
    }

    fun fog(fog: Fog, i: Int) {
        GL11.glFogi(fog.GL, i)
    }

    fun fog(fog: Fog, v: Float) {
        GL11.glFogf(fog.GL, v)
    }

    fun fog(color: Color) {
        GL11.glFogfv(Fog.COLOR.GL, color.toArray())
    }
}
