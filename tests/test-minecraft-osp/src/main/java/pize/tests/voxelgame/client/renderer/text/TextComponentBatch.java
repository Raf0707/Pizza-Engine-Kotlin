package pize.tests.voxelgame.client.renderer.text;

import pize.Pize;
import pize.app.Disposable;
import pize.graphics.font.BitmapFont;
import pize.graphics.font.FontLoader;
import pize.graphics.font.Glyph;
import pize.graphics.util.batch.TextureBatch;
import pize.graphics.util.color.Color;
import pize.graphics.util.color.IColor;
import pize.math.Maths;
import pize.math.vecmath.vector.Vec2f;
import pize.tests.voxelgame.main.text.Component;
import pize.tests.voxelgame.main.text.ComponentText;
import pize.tests.voxelgame.main.text.TextStyle;

import java.util.List;

public class TextComponentBatch implements Disposable{
    
    private final TextureBatch batch;
    private final BitmapFont font;
    private final Color background;
    
    public TextComponentBatch(){
        this.batch = new TextureBatch(1024);
        this.font = FontLoader.loadFnt("font/minecraft/default.fnt");
        font.scale = 3;
        font.lineGaps = 1;
        background = new Color();
    }
    
    
    public void updateScale(){
        font.scale = Maths.round(Pize.getHeight() / 330F);
    }
    
    
    public void drawComponents(List<ComponentText> components, float x, float y, float width, float alpha){
        // Подготовим batch и font
        batch.begin();
        
        // Позиция следующего символа
        final float lineAdvance = font.getLineAdvance();
        
        float advanceX = 0;
        float advanceY = 0;
        
        // Фон
        if(background.a() != 0){
            final StringBuilder text = new StringBuilder();
            for(ComponentText component: components)
                text.append(component.toString());
            
            final Vec2f bounds = font.getBounds(text.toString());
            batch.drawQuad(background, x, y, bounds.x, bounds.y);
        }
        
        // Пройдемся по каждому компоненту дерева, разложенному в плоский список
        for(ComponentText component: components){
            
            // Стиль компонента
            final TextStyle style = component.getStyle();
            batch.shear(style.isItalic() ? 15 : 0, 0);
            
            // Цвет компонента
            final Color color = component.getColor().copy();
            color.setA(color.a() * alpha);
            
            // Текст
            final String text = component.getText();
            
            // Рендерим каждый символ
            for(int i = 0; i < text.length(); i++){
                final char code = text.charAt(i);
                
                // Перенос на новую строку соответствующим символом
                if(code == 10){
                    advanceX = 0;
                    advanceY -= lineAdvance;
                    continue;
                }
                
                // Глиф
                final Glyph glyph = font.getGlyph(code);
                if(glyph == null)
                    continue;
                
                // Перенос на новую строку если текст не вмещается в заданную ширину
                if(width > 0 && (advanceX + glyph.advanceX) * font.scale >= width){
                    advanceX = 0;
                    advanceY -= lineAdvance;
                }
                
                // Координаты глифа
                final float glyphX = x + (advanceX + glyph.offsetX) * font.scale;
                final float glyphY = y + (advanceY + glyph.offsetY) * font.scale;
                
                // Рендерим тень
                batch.setColor(color.copy().mul(0.25));
                glyph.render(batch, glyphX + font.scale, glyphY - font.scale);
                // Рендерим основной символ
                batch.setColor(color);
                glyph.render(batch, glyphX, glyphY);
                
                advanceX += glyph.advanceX;
            }
        }
        
        // Закончим отрисовку
        batch.shear(0, 0);
        batch.resetColor();
        batch.end();
    }
    
    
    public void drawComponents(List<ComponentText> components, float x, float y, float alpha){
        drawComponents(components, x, y, -1, alpha);
    }
    
    public void drawComponents(List<ComponentText> components, float x, float y){
        drawComponents(components, x, y, 1F);
    }
    
    
    public void drawComponent(Component component, float x, float y, float width, float alpha){
        drawComponents(component.toFlatList(), x, y, width, alpha);
    }
    
    public void drawComponent(Component component, float x, float y, float alpha){
        drawComponents(component.toFlatList(), x, y, -1, alpha);
    }
    
    public void drawComponent(Component component, float x, float y){
        drawComponents(component.toFlatList(), x, y, 1F);
    }
    
    
    public TextureBatch getBatch(){
        return batch;
    }
    
    public BitmapFont getFont(){
        return font;
    }
    
    
    public void setBackgroundColor(double r, double g, double b, double a){
        background.set(r, g, b, a);
    }
    
    public void setBackgroundColor(double r, double g, double b){
        background.set(r, g, b);
    }
    
    public void setBackgroundColor(IColor color){
        background.set(color);
    }
    
    
    @Override
    public void dispose(){
        batch.dispose();
        font.dispose();
    }

}
