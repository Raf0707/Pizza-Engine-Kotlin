package pize.tests.voxelgame

import pize.Pize.create
import pize.Pize.dt
import pize.Pize.run
import pize.Pize.setFixedUpdateTPS
import pize.app.AppAdapter
import pize.audio.sound.Sound
import pize.files.Resource
import pize.graphics.camera.Camera3D.x
import pize.graphics.camera.Camera3D.y
import pize.graphics.camera.Camera3D.z
import pize.graphics.gl.Gl.clearColorDepthBuffers
import pize.graphics.texture.Region.set
import pize.graphics.texture.Texture
import pize.graphics.util.color.Color.mul
import pize.graphics.util.color.Color.set
import pize.math.Mathc.signum
import pize.math.Maths.frac
import pize.math.Maths.random
import pize.math.util.EulerAngles.set
import pize.math.vecmath.matrix.Matrix4f.mul
import pize.math.vecmath.matrix.Matrix4f.set
import pize.math.vecmath.vector.Vec2f.set
import pize.math.vecmath.vector.Vec3f
import pize.math.vecmath.vector.Vec3f.mul
import pize.math.vecmath.vector.Vec3f.set
import pize.math.vecmath.vector.Vec3i.mul
import pize.math.vecmath.vector.Vec3i.set
import pize.physic.Velocity3f
import pize.tests.voxelgame.client.ClientGame
import pize.tests.voxelgame.client.audio.AudioPlayer
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.client.control.GameController
import pize.tests.voxelgame.client.options.Options
import pize.tests.voxelgame.client.renderer.GameRenderer
import pize.tests.voxelgame.client.renderer.particle.Particle
import pize.tests.voxelgame.client.renderer.particle.ParticleInstance
import pize.tests.voxelgame.client.resources.GameResources
import pize.tests.voxelgame.client.resources.VanillaAudio
import pize.tests.voxelgame.client.resources.VanillaBlocks
import pize.tests.voxelgame.client.resources.VanillaMusic
import pize.tests.voxelgame.main.Version
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.modification.loader.ModEntryPointType
import pize.tests.voxelgame.main.modification.loader.ModLoader
import pize.tests.voxelgame.main.net.PlayerProfile
import pize.tests.voxelgame.main.time.GameTime
import pize.tests.voxelgame.server.IntegratedServer
import pize.util.Utils.delayElapsed
import pize.util.time.*

class Minecraft : AppAdapter() {
    val resources: GameResources
    val options: Options
    val fpsSync: Sync
    val version: Version
    val profile: PlayerProfile
    val controller: GameController
    val renderer: GameRenderer
    var integratedServer: IntegratedServer? = null
        private set
    val game: ClientGame
    val audioPlayer: AudioPlayer
    val modLoader: ModLoader
    override fun init() {
        // Run local server //
        val address = options.host.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (address[0] == "0.0.0.0") {
            integratedServer = IntegratedServer(this)
            integratedServer!!.run()
        }

        // Init mods //
        modLoader.initializeMods(ModEntryPointType.CLIENT)
        modLoader.initializeMods(ModEntryPointType.MAIN)

        // Connect to server //
        delayElapsed(1000)
        game.connect(address[0], address[1].toInt())
        val sound = Sound("music/game/sweden.ogg")
        sound.play()
    }

    override fun render() {
        fpsSync.sync()
        controller.update()
        game.update()
        clearColorDepthBuffers()
        renderer.render()
        modLoader.invokeMethod(ModEntryPointType.CLIENT, "render")
    }

    override fun fixedUpdate() {
        game.tick()
    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
        game.camera.resize(width, height)
    }

    override fun dispose() {
        // Save options
        options.save()

        // Stop server
        if (integratedServer != null) integratedServer!!.stop() else game.disconnect()

        // Free resources
        renderer.dispose()
        resources.dispose()
    }

    val sessionToken: String
        get() = Companion.sessionToken

    /**                 SOME HORRIBLE CODE                 */
    val BREAK_PARTICLE = Particle()
        .init { instance: ParticleInstance? ->
            instance!!.size = random(0.02f, 0.15f)
            instance.region[random(0.0, 0.5), random(0.0, 0.5), random(0.5, 1.0)] = random(0.5, 1.0)
            instance.rotation = random(1, 360).toFloat()
            instance.lifeTimeSeconds = random(0.5f, 2f)
            instance.velocity.set(random(-0.04f, 0.04f), random(-0.02f, 0.1f), random(-0.04f, 0.04f))
        }
        .texture(Texture("texture/block/grass_block_side.png"))
        .animate { instance: ParticleInstance? ->
            instance!!.velocity.y -= (dt * 0.35).toFloat()
            instance.velocity.mul(0.95)
            collide(instance.position, instance.velocity)
            instance.position.add(instance.velocity)
        }

    init {
        // Create Instances //
        Thread.currentThread().setName("Render-Thread")

        // Resources //
        resources = GameResources()
        VanillaBlocks.register(resources)
        VanillaAudio.register(resources)
        VanillaMusic.register(resources)
        resources.load()

        // Other //
        version = Version()
        options = Options(this, SharedConstants.GAME_DIR_PATH)
        fpsSync = Sync(0.0)
        controller = GameController(this)
        renderer = GameRenderer(this)
        game = ClientGame(this)
        audioPlayer = AudioPlayer(this)
        renderer.init()
        Resource(SharedConstants.GAME_DIR_PATH, true).mkDirs()
        Resource(SharedConstants.MODS_PATH, true).mkDirs()
        setFixedUpdateTPS(GameTime.Companion.TICKS_PER_SECOND.toFloat())
        options.load()
        profile = PlayerProfile(options.playerName)
        Blocks.init(this)

        // Mod Loader //
        modLoader = ModLoader()
        modLoader.loadMods(SharedConstants.MODS_PATH)
    }

    fun collide(position: Vec3f?, velocity: Velocity3f?) {
        val level = game.level
        val x: Double = velocity.x.toDouble()
        //if(BlockState.getID(level.getBlock(position.xf() + Mathc.signum(x), position.yf(), position.zf())) != 0){
        //    double nx = Maths.frac(position.x) + x;
        //    if(nx > 1)
        //        x = 1;
        //    else if(nx < 0)
        //        x = 0;
        //}
        var y: Double = velocity.y.toDouble()
        if (BlockData.getID(level!!.getBlock(position!!.xf(), position.yf() + signum(x), position.zf())).toInt() != 0) {
            val ny = frac(position.y) + y
            //if(ny > 1)
            //    y = 1;
            //else
            if (ny < 0) y = 0.0
        }
        val z: Double = velocity.z.toDouble()
        //if(BlockState.getID(level.getBlock(position.xf(), position.yf(), position.zf() + Mathc.signum(x))) != 0){
        //    double nz = Maths.frac(position.z) + z;
        //    if(nz > 1)
        //        z = 1;
        //    else if(nz < 0)
        //        z = 0;
        //}
        velocity.set(x, y, z)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Minecraft OSP", 1280, 720)
            run(instance!!)
        }

        private const val sessionToken = "54_54-iWantPizza-54_54"

        @get:Synchronized
        var instance: Minecraft? = null
            get() {
                if (field == null) field = Minecraft()
                return field
            }
            private set
    }
}
