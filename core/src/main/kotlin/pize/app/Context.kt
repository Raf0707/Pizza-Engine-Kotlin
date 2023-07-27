package pize.app

import org.lwjgl.glfw.GLFW
import pize.audio.Audio
import pize.audio.util.TaskExecutor
import pize.graphics.gl.Gl
import pize.graphics.texture.*
import pize.graphics.util.*
import pize.graphics.vertex.*
import pize.io.keyboard.Keyboard
import pize.io.mouse.Mouse
import pize.io.window.Window
import pize.util.Utils
import pize.util.time.DeltaTimeCounter
import pize.util.time.PerSecCounter
import pize.util.time.TickGenerator
import pize.util.time.Tickable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Context(window: Window, keyboard: Keyboard, mouse: Mouse) {
    val audio: Audio
    val window: Window
    val keyboard: Keyboard
    val mouse: Mouse
    private val fpsCounter: PerSecCounter
    val renderDeltaTime: DeltaTimeCounter
    val fixedUpdateDeltaTime: DeltaTimeCounter
    private val fixedUpdateExecutor: ExecutorService
    private var fixedUpdateGenerator: TickGenerator? = null
    private var initialUpdateTPS = 0f
    private var screen: Screen? = null
    private var exitRequest = false
    private val syncTaskExecutor: TaskExecutor

    init {
        audio = Audio()
        this.window = window
        this.keyboard = keyboard
        this.mouse = mouse
        fpsCounter = PerSecCounter()
        fpsCounter.count()
        renderDeltaTime = DeltaTimeCounter()
        renderDeltaTime.update()
        fixedUpdateExecutor = Executors.newFixedThreadPool(3)
        fixedUpdateDeltaTime = DeltaTimeCounter()
        syncTaskExecutor = TaskExecutor()
    }

    fun begin(listener: AppAdapter) {
        listener.init() /* INIT */

        // Window initialization
        window.show()
        window.addSizeCallback { width: Int, height: Int ->
            listener.resize(width, height)
            Gl.viewport(width, height)
        }

        // Fixed update

        // Fixed update
        if (initialUpdateTPS != 0f) {
            fixedUpdateGenerator = TickGenerator(initialUpdateTPS)
            fixedUpdateGenerator!!.startAsync(object : Tickable {
                override fun tick() {
                    if (fixedUpdateExecutor.isShutdown) return //@startAsync
                    fixedUpdateDeltaTime.update()
                    fixedUpdateExecutor.submit { listener.fixedUpdate() } /* FIXED UPDATE */
                }
            })
        }

        // Render loop
        while (!window.closeRequest()) {
            if (exitRequest) break
            render(listener)

            // Pize.syncExec() tasks
            syncTaskExecutor.executeTasks()
        }

        // Stop fixed update
        if (fixedUpdateGenerator != null) {
            fixedUpdateGenerator!!.stop()
            fixedUpdateExecutor.shutdownNow()
        }

        // Unbind OGL objects
        Shader.Companion.unbind()
        VertexArray.Companion.unbind()
        VertexBuffer.Companion.unbind()
        ElementBuffer.Companion.unbind()
        Texture.Companion.unbind()
        CubeMap.Companion.unbind()
        TextureArray.Companion.unbind()

        // Dispose
        window.dispose()
        audio.dispose()
        listener.dispose() /* DISPOSE */

        // Static dispose methods
        Utils.invokeStatic(ScreenQuad::class.java, "dispose")
        Utils.invokeStatic(ScreenQuadShader::class.java, "dispose")
        Utils.invokeStatic(TextureUtils::class.java, "dispose")
        Utils.invokeStatic(BaseShader::class.java, "disposeShaders")

        // Terminate
        GLFW.glfwTerminate()
    }

    private fun render(listener: AppAdapter) {
        GLFW.glfwPollEvents()

        // Render screen
        if (screen != null) screen!!.render() /* RENDER */

        // FPS & DeltaTime
        fpsCounter.count()
        renderDeltaTime.update()

        // Render app
        listener.update() /* UPDATE */
        listener.render() /* RENDER */

        // Reset
        mouse.reset()
        keyboard.reset()
        window.swapBuffers()
    }

    fun setScreen(screen: Screen?) {
        this.screen!!.hide()
        this.screen = screen
        this.screen!!.show()
    }

    val fps: Int
        get() = fpsCounter.get()

    fun getRenderDeltaTime(): Float {
        return renderDeltaTime.get()
    }

    fun getFixedUpdateDeltaTime(): Float {
        return fixedUpdateDeltaTime.get()
    }

    fun setFixedUpdateTPS(updateTPS: Float) {
        if (fixedUpdateGenerator != null) fixedUpdateGenerator!!.setTPS(updateTPS) else initialUpdateTPS = updateTPS
    }

    fun execSync(runnable: Runnable) {
        syncTaskExecutor.newTask(runnable)
    }

    fun exit() {
        syncTaskExecutor.dispose()
        exitRequest = true
    }
}
