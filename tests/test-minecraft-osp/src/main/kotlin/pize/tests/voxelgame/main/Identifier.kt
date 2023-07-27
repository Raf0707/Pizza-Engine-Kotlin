package pize.tests.voxelgame.main

object Identifier {
    const val DEFAULT_NAMESPACE = "minecraft"
    fun namespaceID(namespace: String, id: String): String {
        return "$namespace:$id"
    }

    fun namespaceID(id: String): String {
        return namespaceID(DEFAULT_NAMESPACE, id)
    }
}
