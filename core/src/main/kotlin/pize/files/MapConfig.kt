package pize.files

import java.io.File

class MapConfig(resource: Resource?) {
    private var resource: Resource? = null
    @JvmField
    val map: HashMap<String, String>
    var separator: String

    init {
        setResource(resource)
        map = HashMap()
        separator = " : "
    }

    constructor(file: File) : this(Resource(file))

    fun load() {
        map.clear()
        val lines = resource!!.readString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            val entry = line.split(separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (entry.size == 2) map[entry[0]] = entry[1]
        }
    }

    fun save() {
        val writer = resource.getWriter()
        for ((key, value) in map) writer!!.println(key + separator + value)
        writer!!.close()
    }

    fun clear() {
        map.clear()
    }

    fun put(key: String, value: String) {
        map[key] = value
    }

    operator fun get(key: String): String? {
        return map[key]
    }

    fun getOrDefault(key: String, defaultValue: String): String {
        return map.getOrDefault(key, defaultValue)
    }

    fun setResource(resource: Resource?) {
        this.resource = resource
    }

    fun setFile(file: File) {
        resource = Resource(file)
    }
}
