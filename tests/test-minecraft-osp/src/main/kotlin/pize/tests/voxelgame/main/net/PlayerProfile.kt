package pize.tests.voxelgame.main.net

import pize.math.Maths.random
import pize.util.StringUtils.isBlank

class PlayerProfile(val name: String?) {

    companion object {
        fun isNameInvalid(name: String?): Boolean {
            return isBlank(name) || name!!.length > 16 || name.length < 3 || name.contains(" ")
        }

        fun genFunnyName(): String {
            val funnyNames = arrayOf(
                "Makcum",
                "Kriper",
                "IlyaPro",
                "ViktorPlay",
                "Kirbo",
                "IbremMiner",
                "intbyte",
                "ABelevka",
                "Dmitry"
            )
            return funnyNames[random(0, funnyNames.size - 1)] + random(51, 99)
        }
    }
}
