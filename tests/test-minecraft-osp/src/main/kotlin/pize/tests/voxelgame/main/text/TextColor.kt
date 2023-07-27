package pize.tests.voxelgame.main.text

import pize.graphics.util.color.ImmutableColor

enum class TextColor(val color: ImmutableColor) {
    BLACK(ImmutableColor(0, 0, 0, 255)),
    DARK_BLUE(ImmutableColor(0, 0, 170, 255)),
    DARK_GREEN(ImmutableColor(0, 170, 0, 255)),
    DARK_AQUA(ImmutableColor(0, 170, 170, 255)),
    DARK_RED(ImmutableColor(200, 0, 0, 255)),
    DARK_PURPLE(ImmutableColor(170, 0, 170, 255)),
    ORANGE(ImmutableColor(255, 170, 0, 255)),
    GRAY(ImmutableColor(170, 170, 170, 255)),
    DARK_GRAY(ImmutableColor(85, 85, 85, 255)),
    BLUE(ImmutableColor(30, 144, 255, 255)),
    GREEN(ImmutableColor(85, 255, 85, 255)),
    AQUA(ImmutableColor(85, 255, 255, 255)),
    RED(ImmutableColor(255, 85, 85, 255)),
    LIGHT_PURPLE(ImmutableColor(255, 85, 255, 255)),
    YELLOW(ImmutableColor(255, 255, 85, 255)),
    WHITE(ImmutableColor(255, 255, 255, 255))
}
