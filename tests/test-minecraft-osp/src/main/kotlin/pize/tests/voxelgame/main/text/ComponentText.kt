package pize.tests.voxelgame.main.text

import pize.graphics.util.color.Color

class ComponentText(style: TextStyle?, color: Color, val text: String?) : Component(style, color) {

    override fun toString(): String {
        return text + super.toString()
    }
}
