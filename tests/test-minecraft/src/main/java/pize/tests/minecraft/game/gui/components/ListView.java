package pize.tests.minecraft.game.gui.components;

import pize.Pize;
import pize.graphics.util.batch.TextureBatch;
import pize.math.Maths;

public class ListView extends MComponent{
    
    private float scrollX, scrollY;
    
    @Override
    protected void render(TextureBatch batch, float x, float y, float width, float height){
        batch.scissor.begin(228, x, y, width, height);
        
        scrollX = Maths.clamp(scrollX + Pize.mouse().getScrollX(Pize.keyboard()) * 50, -1000, 1000);
        scrollY = Maths.clamp(scrollY + Pize.mouse().getScrollY(Pize.keyboard()) * 50, -200, 0);
        setChildShift(scrollX, -scrollY);
    }
    
    @Override
    protected void renderEnd(TextureBatch batch){
        batch.scissor.end(228);
    }
    
    
    public boolean isHoverIgnoreChildren(){
        float mouseX = Pize.getX();
        float mouseY = Pize.getY();
        return !(mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height);
    }
    
}
