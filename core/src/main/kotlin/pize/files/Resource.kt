package pize.files

import pize.util.io.FastReader
import java.io.*

class Resource // Main
@JvmOverloads constructor(
    @JvmField val file: File,
    val isExternal: Boolean = false,
    private val classLoader: Class<*> = Resource::class.java
) {
    constructor(
        filepath: String,
        external: Boolean,
        classLoader: Class<*>
    ) : this(File((if (!external) "/" else "") + filepath), external, classLoader)

    // Parent child
    constructor(parent: File?, child: String?, external: Boolean, classLoader: Class<*>) : this(
        File(parent, child),
        external,
        classLoader
    )

    constructor(parent: File?, child: String?, external: Boolean) : this(
        File(parent, child),
        external,
        Resource::class.java
    )

    // Internal
    @JvmOverloads
    constructor(filepath: String, classLoader: Class<*> = Resource::class.java) : this(filepath, false, classLoader)

    // External
    constructor(filepath: String, external: Boolean) : this(
        File((if (!external) "/" else "") + filepath),
        external,
        Resource::class.java
    )

    fun create(): Boolean { // External only
        return try {
            file.createNewFile()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun mkDir(): Boolean { // External only
        return file.mkdir()
    }

    fun mkDirs(): Boolean { // External only
        return file.mkdirs()
    }

    fun mkParentDirs(): Boolean { // External only
        return file.getParentFile().mkdirs()
    }

    fun mkDirsAndFile(): Boolean { // External only
        return mkParentDirs() && create()
    }

    fun inStream(): InputStream {
        return try {
            if (isExternal) FileInputStream(file) else {
                val inputStream = classLoader.getResourceAsStream(path)
                    ?: throw FileNotFoundException("Internal file does not exists: " + path)
                inputStream
            }
        } catch (e: FileNotFoundException) {
            throw RuntimeException(e)
        }
    }

    fun outStream(): FileOutputStream {
        if (!isExternal) throw RuntimeException("Cannot write into internal file: $file")
        return try {
            FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            throw RuntimeException("File does not exists: $file")
        }
    }

    val reader: FastReader
        get() = FastReader(inStream())
    val writer: PrintStream
        get() = PrintStream(outStream())

    fun readString(): String {
        try {
            inStream().use { `in` -> return String(`in`.readAllBytes()) }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun writeString(string: String) {
        try {
            outStream().use { out ->
                out.write(string.toByteArray())
                out.flush()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun appendString(string: CharSequence) {
        writeString(readString() + string)
    }

    val name: String
        get() = file.getName()
    val extension: String
        get() {
            val name = name
            val dotIndex = name.lastIndexOf(".")
            return if (dotIndex == -1) "" else name.substring(dotIndex + 1)
        }
    val simpleName: String
        get() {
            val name = name
            val dotIndex = name.lastIndexOf('.')
            return if (dotIndex == -1) name else name.substring(0, dotIndex)
        }

    fun listResources(): Array<Resource?> {
        val relativePaths = file.list() ?: return arrayOfNulls(0)
        val resources = arrayOfNulls<Resource>(relativePaths.size)
        for (i in relativePaths.indices) resources[i] = getChild(relativePaths[i])
        return resources
    }

    fun listResources(filter: FilenameFilter): Array<Resource?> {
        val resources = listResources()
        val filteredResources: MutableList<Resource?> = ArrayList()
        for (resource in resources) if (filter.accept(file, resource!!.name)) filteredResources.add(resource)
        return filteredResources.toTypedArray<Resource?>()
    }

    fun list(): Array<String> {
        return file.list()
    }

    fun list(filter: FilenameFilter?): Array<String> {
        return file.list(filter)
    }

    fun getChild(name: String): Resource {
        return if (path.length == 0) Resource(name, isExternal, classLoader) else Resource(
            File(file, name),
            isExternal,
            classLoader
        )
    }

    fun exists(): Boolean {
        return file.exists()
    }

    val path: String
        get() = file.path.replace("\\", "/")
    val absolutePath: String
        get() = file.absolutePath
    val isInternal: Boolean
        get() = !isExternal

    override fun toString(): String {
        return absolutePath
    }

    companion object {
        @JvmOverloads
        fun readString(filepath: String, external: Boolean = false): String {
            return Resource(filepath, external).readString()
        }
    }
}