package pize.tests.voxelgame.client.entity;

import pize.Pize;
import pize.math.Mathc;
import pize.math.Maths;
import pize.math.vecmath.vector.Vec3f;
import pize.tests.voxelgame.client.block.BlockProperties;
import pize.tests.voxelgame.client.block.Blocks;
import pize.tests.voxelgame.main.audio.SoundGroup;
import pize.tests.voxelgame.main.level.Level;

public class LocalPlayer extends AbstractClientPlayer{
    
    private final Vec3f moveControl;
    private float jumpDownY, lastVelocityY, fallHeight;
    
    public BlockProperties holdBlock = Blocks.GRASS;
    
    public LocalPlayer(Level levelOF, String name){
        super(levelOF, name);

        moveControl = new Vec3f();
    }

    @Override
    public void tick(){
        super.tick();
        
        /** -------- Vertical Move -------- */
        
        // Jumping
        if(isJumping()){
            if(isOnGround()){
                // Jump
                getVelocity().y = 0.42F;
                
                // Jump-boost
                if(isSprinting()){
                    final float yaw = getRotation().yaw * Maths.ToRad;
                    final float jumpBoost = 0.2F;
                    getVelocity().x += jumpBoost * Mathc.cos(yaw);
                    getVelocity().z += jumpBoost * Mathc.sin(yaw);
                }
            }
        }

        // Interrupt Flying
        if(isOnGround() && isFlying())
            setFlying(false);

        if(isFlying()){
            if(isSneaking())
                getVelocity().y -= 0.05F;
            
            if(isJumping())
                getVelocity().y += 0.05F;
            
            if(!isFlyEnabled())
                setFlying(false);
        }

        // In Water
        final Vec3f position = getPosition();
        if(getLevel().getBlock(position.xf(), position.yf(), position.zf()) == Blocks.WATER.getID()){
            //getVelocity().x += Maths.random(0, 2) * Maths.cosDeg(getRotation().yaw);
            //getVelocity().y += 0.4;
            //getVelocity().z += Maths.random(0, 2) * Maths.sinDeg(getRotation().yaw);
            //Pize.execSync(() -> getLevel().getSession().getAudioPlayer().play(SoundGroup.HIT.random(), 0.3F, 1, 0, 0, 0) );
            //getLevel().getSession().getGame().getCamera().getRotation().roll = 50F;
        }

        getLevel().getSession().getGame().getCamera().rotation.roll /= 1.4F;

        // Gravity
        if(!isOnGround() && !isFlying())
            getVelocity().y -= 0.08;
        
        // Reduce Vertical Motion
        getVelocity().y *= 0.98;
        
        
        /** -------- Horizontal Move -------- */
        
        // Movement multiplier
        float movementMul = 0.98F; // Default
        if(isSneaking() && !isFlying())
            movementMul *= 10; // Sneaking
        if(isSprinting())
            movementMul *= 10; // Sprinting
        if(isFlying())
            movementMul *= 100; // Flying
        
        
        // Slipperiness multiplier
        float slipperinessMul = 1; // Air
        if(isOnGround())
            slipperinessMul *= 0.9; // Ground
        
        // Reduce Last Motion
        final float reduceHorizontal = slipperinessMul * 0.91F;
        getVelocity().mul(reduceHorizontal, 1, reduceHorizontal);
        
        // Move
        float moveControlLen = moveControl.len();
        if(moveControlLen > 0){
            final Vec3f acceleration = new Vec3f(moveControl.x, 0, moveControl.z);
            
            if(isOnGround()){
                final float slipperiness = 0.6F / slipperinessMul;
                acceleration.mul(0.1 * movementMul * slipperiness * slipperiness * slipperiness);
            }else
                acceleration.mul(0.02 * movementMul);
            
            getVelocity().add(acceleration);
        }
        
        
        /** -------- Other -------- */
        
        // Fall height
        if(getVelocity().y < 0 && lastVelocityY >= 0)
            jumpDownY = getPosition().y;
        
        if(isOnGround() && jumpDownY != 0){
            fallHeight = jumpDownY - getPosition().y;
            jumpDownY = 0;
        }
        
        lastVelocityY = getVelocity().y;
        
        // Move entity
        final Vec3f collidedMotion = moveEntity(getVelocity());
        getVelocity().collidedAxesToZero(collidedMotion);
        
        // Disable sprinting
        if(collidedMotion.x == 0 || collidedMotion.z == 0)
            setSprinting(false);
    }
    
    public float getFallHeight(){
        return fallHeight;
    }
    
    
    public void moveControl(Vec3f motion){
        moveControl.set(motion);
    }
    
}
