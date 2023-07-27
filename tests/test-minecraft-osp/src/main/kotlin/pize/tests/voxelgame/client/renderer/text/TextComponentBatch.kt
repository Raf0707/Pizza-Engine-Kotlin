package pize.tests.voxelgame.client.renderer.text

import pize.Pize.height
import pize.app.Disposable
import pize.graphics.font.BitmapFont
import pize.graphics.font.FontLoader.loadFnt
import pize.graphics.util.batch.TextureBatch
import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import pize.math.Maths.round
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.ComponentText

class TextComponentBatch : Disposable {
    val batch: TextureBatch
    val font: BitmapFont
    private val background: Color

    init {
        batch = TextureBatch(1024)
        font = loadFnt("font/minecraft/default.fnt")
        font.scale = 3f
        font.lineGaps = 1f
        background = Color()
    }

    fun updateScale() {
        font.scale = round((height / 330f).toDouble()).toFloat()
    }

    fun drawComponents(components: List<ComponentText?>?, x: Float, y: Float, width: Float, alpha: Float) {
        // Подготовим batch и font
        batch.begin()

        // Позиция следующего символа
        val lineAdvance = font.lineAdvance
        var advanceX = 0f
        var advanceY = 0f

        // Фон
        if (background.a() != 0f) {
            val text = StringBuilder()
            for (component in components!!) text.append(component.toString())
            val bounds = font.getBounds(text.toString())
            batch.drawQuad(background, x, y, bounds!!.x, bounds.y)
        }

        // Пройдемся по каждому компоненту дерева, разложенному в плоский список
        for (component in components!!) {

            // Стиль компонента
            val style = component.getStyle()
            batch.shear((if (style!!.isItalic) 15 else 0).toFloat(), 0f)

            // Цвет компонента
            val color = component.getColor().copy()
            color.setA(color.a() * alpha)

            // Текст
            val text = component.getText()

            // Рендерим каждый символ
            for (i in 0 until text!!.length) {
                val code = text!![i]

                // Перенос на новую строку соответствующим символом
                if (code.code == 10) {
                    advanceX = 0f
                    advanceY -= lineAdvance
                    continue
                }

                // Глиф
                val glyph = font.getGlyph(code.code) ?: continue

                // Перенос на новую строку если текст не вмещается в заданную ширину
                if (width > 0 && (advanceX + glyph.advanceX) * font.scale >= width) {
                    advanceX = 0f
                    advanceY -= lineAdvance
                }

                // Координаты глифа
                val glyphX = x + (advanceX + glyph.offsetX) * font.scale
                val glyphY = y + (advanceY + glyph.offsetY) * font.scale

                // Рендерим тень
                batch.setColor(color.copy().mul(0.25))
                glyph.render(batch, glyphX + font.scale, glyphY - font.scale)
                // Рендерим основной символ
                batch.setColor(color)
                glyph.render(batch, glyphX, glyphY)
                advanceX += glyph.advanceX
            }
        }

        // Закончим отрисовку
        batch.shear(0f, 0f)
        batch.resetColor()
        batch.end()
    }

    @JvmOverloads
    fun drawComponents(components: List<ComponentText?>?, x: Float, y: Float, alpha: Float = 1f) {
        drawComponents(components, x, y, -1f, alpha)
    }

    fun drawComponent(component: Component, x: Float, y: Float, width: Float, alpha: Float) {
        drawComponents(component.toFlatList(), x, y, width, alpha)
    }

    fun drawComponent(component: Component, x: Float, y: Float, alpha: Float) {
        drawComponents(component.toFlatList(), x, y, -1f, alpha)
    }

    fun drawComponent(component: Component?, x: Float, y: Float) {
        drawComponents(component!!.toFlatList(), x, y, 1f)
    }

    fun setBackgroundColor(r: Double, g: Double, b: Double, a: Double) {
        background[r, g, b] = a
    }

    fun setBackgroundColor(r: Double, g: Double, b: Double) {
        background[r, g] = b
    }

    fun setBackgroundColor(color: IColor?) {
        background.set(color!!)
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}
