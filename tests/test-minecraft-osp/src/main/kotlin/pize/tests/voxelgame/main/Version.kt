package pize.tests.voxelgame.main

import pize.tests.voxelgame.SharedConstants

class Version @JvmOverloads constructor(val name: String? = SharedConstants.VERSION_NAME) {
    val iD: Int
    val isStable: Boolean

    init {
        iD = name.hashCode()
        isStable = SharedConstants.STABLE
    }
}
