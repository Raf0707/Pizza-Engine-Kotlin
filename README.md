# PIZE - Pizza Engine [Kotlin]
The engine focuses on being clear, simple and productive

## Getting Started
Repository contains examples in 'tests' module

Modules:
* Core
* Net
* Physics
* UI

### Core:
* Audio (OGG, MP3, WAV)
* Graphics (2D, 3D; Fonts, Postprocessing)
* Math (Vectors, Matrices)
* IO (Keyboard, Mouse, Monitors)
* Files (Resources: Internal / External)
* Utils (FastReader, FpsCounter, Sync, Stopwatch, TickGenerator, ...etc)

#### 1. 2D Graphics:
``` kotlin
val batch = new TextureBatch() // canvas for textures
texture = new Texture("texture.png")

// rotate, shear and scale for subsequent textures
batch.rotate(angle)
batch.shear(angle_x, angle_y)
batch.scale(scale)

// texture drawing
batch.draw(texture, x, y, width, height)
```

#### 2. Input:
``` kotlin
val mouse: Mouse = Mouse()
val key: Key = Key()
val window: Window = Window()
val monitor: Monitor = Monitor()
val fps: Float = 0.0

val mouse: Mouse = Mouse()
val key: Key = Key()
val window: Window = Window()
val monitor: Monitor = Monitor()
val fps: Float = 0.0

// mouse
fun mouse(): Mouse { 
    val x = mouse.getX() // position
    val y = mouse.getY()
    val isTouched = mouse.isTouched()
    val isTouchDown = mouse.isTouchDown()
    val isTouchReleased = mouse.isTouchReleased()
    val scroll = mouse.mouse().getScroll() // scroll
    val width = window.getWidth()
    val height = window.getHeight()
    val aspect = window.getAspect()
    val fps = Float(fps)
}

// keyboard
fun key(): Key {
    val space = key.space()
    val enter = key.enter()
    val escape = key.escape()
}

// window
fun window(): Window {
    val width = window.getWidth()
    val height = window.getHeight()
    val aspect = window.getAspect()
    val fps = Float(fps)
}

// monitor
fun monitor(): Monitor {
    val width = monitor.getWidth()
    val height = monitor.getHeight()
    val aspect = monitor.getAspect()
}

// fps
fun fps(): Float {
    return fps
}
```

#### 3. Audio:
``` kotlin
val sound = Sound("sound.mp3")

sound.setVolume(0.5F)
sound.setLooping(true)
sound.setPitch(1.5F)

sound.play()
```

#### 4. Resources:
``` kotlin
// internal / external
val res: Resource = Resource(path, true)

res.isExternal()
res.isInternal()

// text
val text: Resource = Resource("file.txt")
text.writeString("write text")
text.appendString("append text")
text.readString()

// file
val res: Resource = Resource("file.ext")

res.getExtension() // returns 'ext'
res.getSimpleName() // returns 'file'

res.getFile()
res.exists()
res.mkDirsAndFile()

res.inStream()
res.outStream()

res.getReader() // returns pize.util.io.FastReader
res.getWriter() // returns PrintStream

val res: Resource = Resource( ... )

new Texture(res)
new Sound(res)
PixmapIO.load(res)
new Shader(res_vert, res_frag)
AudioLoader.load(new AudioBuffer(), res)
```

### Net:
* Security Keys (AES, RSA)
* TCP / UDP Client and Server

#### 1. (Low)Encrypted Server-Client Example:
``` kotlin
val keyAES = new KeyAES(128) // generate key for connection encoding

// server
val server = TcpServer(new TcpListener {
    override fun received(bytes: ByteArray) {
    println("received: $bytes") // 'received: Hello, World!'
    }
    override fun connected(channel: TcpChannel) {
    channel.encrypt(key)
    }
    override fun disconnected(channel: TcpChannel) { ... }
    }
   server.run(":8080", server.port)
    }
    val client = TcpClient(new TcpListener { ... })
    client.connect("localhost", 8080)
    client.encrypt(key)
    client.send("Hello, World!".getBytes()) // send 'Hello, World!'
```

### Physics:
* AABB Collider (2D, 3D)
* Utils (Velocity, Body)

#### 1. Collider Example:
``` kotlin
box_1 = BoxBody(new BoundingBox(-1, -1, -1, 1, 1, 1))
box_2 = BoxBody(new BoundingBox(-1, -1, -1, 1, 1, 1))

box_1.getPosition().set(-5.0f, 0.0f, 0.0f)

box_2.getPosition().add(box_1.getPosition())
```

### UI:
* Constraint (pixel, relative, aspect) - used to determine the static or dynamic position and size of ui components.
* Align (center, up, right, ...etc)
* LayoutType (vertical / horizontal / none)

#### 1. UI Example:
``` kotlin
// init
val layout = Layout()
layout.setLayoutType( LayoutType.HORIZONTAL )
layout.alignItems( Align.CENTER )

val image = Image(image_texture)
layout.put("image_id", image)

// render
batch.begin()
layout.render(batch)
batch.end()
```

## Bugs and Feedback
For bugs, questions and discussions please use the [GitHub Issues](https://github.com/Raf0707/Pizza-Engine-Kotlin/issues/1).
