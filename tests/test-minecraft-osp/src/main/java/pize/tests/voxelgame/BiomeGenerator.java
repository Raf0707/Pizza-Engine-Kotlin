package pize.tests.voxelgame;

import pize.app.AppAdapter;
import pize.graphics.gl.Gl;
import pize.graphics.texture.Pixmap;
import pize.graphics.texture.Texture;
import pize.graphics.util.batch.TextureBatch;
import pize.math.Maths;
import pize.math.function.FastNoiseLite;

public class BiomeGenerator extends AppAdapter{
    
    private final TextureBatch batch;
    private Texture mapTexture;
    
    private final FastNoiseLite
        continentalnessNoise, erosionNoise, peaksValleysNoise, temperatureNoise, humidityNoise;
    
    public BiomeGenerator(){
        batch = new TextureBatch();
        
        continentalnessNoise = new FastNoiseLite();
        erosionNoise = new FastNoiseLite();
        peaksValleysNoise = new FastNoiseLite();
        temperatureNoise = new FastNoiseLite();
        humidityNoise = new FastNoiseLite();
        
        continentalnessNoise.setFrequency(0.002F);
        continentalnessNoise.setFractalType(FastNoiseLite.FractalType.FBm);
        continentalnessNoise.setFractalOctaves(7);
        
        erosionNoise.setFrequency(0.002F);
        erosionNoise.setFractalType(FastNoiseLite.FractalType.FBm);
        erosionNoise.setFractalOctaves(5);
    }
    
    
    public void init(){
        final Pixmap map = new Pixmap(1024, 1024);
        
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                final float continentalness = continentalnessNoise.getNoise(x, y);
                final float erosion = erosionNoise.getNoise(x, y);
                
                
                final float grayscale = Maths.round(Maths.map(erosion, -0.55 * Maths.Sqrt2, 0.55 * Maths.Sqrt2, 0, 1) * 5) / 5F;
                
                map.setPixel(x, y, grayscale, grayscale, grayscale, 1);
            }
        }
        
        mapTexture = new Texture(map);
    }
    
    
    public void render(){
        Gl.clearColorBuffer();
        batch.begin();
        batch.draw(mapTexture, 0, 0, 1280, 1280);
        batch.end();
    }
    
}
