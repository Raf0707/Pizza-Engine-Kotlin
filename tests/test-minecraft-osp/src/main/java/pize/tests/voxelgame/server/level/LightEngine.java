package pize.tests.voxelgame.server.level;

import pize.math.vecmath.vector.Vec2i;
import pize.math.vecmath.vector.Vec3i;
import pize.tests.voxelgame.client.block.BlockProperties;
import pize.tests.voxelgame.client.block.Blocks;
import pize.tests.voxelgame.main.Direction;
import pize.tests.voxelgame.main.chunk.storage.ChunkPos;
import pize.tests.voxelgame.main.chunk.storage.Heightmap;
import pize.tests.voxelgame.main.chunk.storage.HeightmapType;
import pize.tests.voxelgame.server.chunk.ServerChunk;

import java.util.concurrent.ConcurrentLinkedQueue;

import static pize.tests.voxelgame.main.chunk.ChunkUtils.*;

public class LightEngine{

    private static class LightNode{

        public final ServerChunk chunk;
        public final byte lx;
        public final short y;
        public final byte lz;
        public final byte level;

        public LightNode(ServerChunk chunk, int lx, int y, int lz, int level){
            this.chunk = chunk;
            this.lx = (byte) lx;
            this.y = (short) y;
            this.lz = (byte) lz;
            this.level = (byte) level;
        }
    }


    private final ServerLevel level;
    private final ConcurrentLinkedQueue<LightNode> bfsIncreaseQueue, bfsDecreaseQueue;

    public LightEngine(ServerLevel level){
        this.level = level;
        this.bfsIncreaseQueue = new ConcurrentLinkedQueue<>();
        this.bfsDecreaseQueue = new ConcurrentLinkedQueue<>();
    }


    public void increase(ServerChunk chunk, int lx, int y, int lz, int level){
        if(chunk.getLight(lx, y, lz) >= level)
            return;

        chunk.setLight(lx, y, lz, level);
        addIncrease(chunk, lx, y, lz, level);

        propagateIncrease();
    }

    private void addIncrease(ServerChunk chunk, int lx, int y, int lz, int level){
        bfsIncreaseQueue.add(new LightNode(chunk, lx, y, lz, level));
    }

    private void propagateIncrease(){
        ServerChunk neighborChunk;
        int neighborX, neighborY, neighborZ;
        int targetLevel;

        while(!bfsIncreaseQueue.isEmpty()){
            final LightNode lightEntry = bfsIncreaseQueue.poll();

            final ServerChunk chunk = lightEntry.chunk;
            final byte x = lightEntry.lx;
            final short y = lightEntry.y;
            final byte z = lightEntry.lz;
            final byte level = lightEntry.level;

            for(int i = 0; i < 6; i++){
                final Vec3i normal = Direction.values()[i].getNormal();

                neighborX = x + normal.x;
                neighborZ = z + normal.z;

                if(neighborX > SIZE_IDX || neighborZ > SIZE_IDX || neighborX < 0 || neighborZ < 0){
                    neighborChunk = getNeighborChunk(chunk, normal.x, normal.z);
                    if(neighborChunk == null)
                        continue;

                    neighborX = getLocalCoord(neighborX);
                    neighborZ = getLocalCoord(neighborZ);
                }else
                    neighborChunk = chunk;

                neighborY = y + normal.y;
                if(neighborY < 0 || neighborY > HEIGHT_IDX)
                    continue;

                int neighborLevel = neighborChunk.getLight(neighborX, neighborY, neighborZ);
                if(neighborLevel >= level - 1)
                    continue;

                final BlockProperties neighborProperties = neighborChunk.getBlockProps(neighborX, neighborY, neighborZ);
                targetLevel = level - Math.max(1, neighborProperties.getOpacity());

                if(targetLevel > neighborLevel){
                    neighborChunk.setLight(neighborX, neighborY, neighborZ, targetLevel);
                    addIncrease(neighborChunk, neighborX, neighborY, neighborZ, targetLevel);
                }
            }
        }
    }


    public void decrease(ServerChunk chunk, int lx, int y, int lz, int level){
        if(chunk.getLight(lx, y, lz) >= level)
            return;

        chunk.setLight(lx, y, lz, 0);
        addDecrease(chunk, lx, y, lz, level);

        propagateDecrease();
    }

    private void addDecrease(ServerChunk chunk, int lx, int y, int lz, int level){
        bfsIncreaseQueue.add(new LightNode(chunk, lx, y, lz, level));
    }

    private void propagateDecrease(){
        ServerChunk neighborChunk;
        int neighborX, neighborY, neighborZ;

        while(!bfsDecreaseQueue.isEmpty()){
            final LightNode lightEntry = bfsDecreaseQueue.poll();

            final ServerChunk chunk = lightEntry.chunk;
            final byte x = lightEntry.lx;
            final short y = lightEntry.y;
            final byte z = lightEntry.lz;
            final byte level = lightEntry.level;

            for(int i = 0; i < 6; i++){
                final Vec3i normal = Direction.normal3DFromIndex(i);

                neighborX = x + normal.x;
                neighborZ = z + normal.z;

                if(neighborX > SIZE_IDX || neighborZ > SIZE_IDX || neighborX < 0 || neighborZ < 0){
                    neighborChunk = getNeighborChunk(chunk, normal.x, normal.z);
                    if(neighborChunk == null)
                        continue;

                    neighborX = getLocalCoord(neighborX);
                    neighborZ = getLocalCoord(neighborZ);
                }else
                    neighborChunk = chunk;

                neighborY = y + normal.y;
                if(neighborY < 0 || neighborY > HEIGHT_IDX)
                    continue;

                int neighborLevel = neighborChunk.getLight(neighborX, neighborY, neighborZ);
                if(neighborLevel != 0 && level > neighborLevel){
                    neighborChunk.setLight(neighborX, neighborY, neighborZ, 0);

                    BlockProperties neighborBlock = neighborChunk.getBlockProps(neighborX, neighborY, neighborZ);

                    addDecrease(neighborChunk, neighborX, neighborY, neighborZ, Math.max(level, neighborLevel + neighborBlock.getOpacity()) );
                }else if(level <= neighborLevel)
                    addIncrease(neighborChunk, neighborX, neighborY, neighborZ, neighborLevel);

            }
        }

        propagateIncrease();
    }



    public void updateSkyLight(ServerChunk chunk){
        final Heightmap heightmapHighest = chunk.getHeightMap(HeightmapType.HIGHEST);
        // final Heightmap heightmapOpaque = chunk.getHeightMap(HeightmapType.OPAQUE);

        for(int lx = 0; lx < SIZE; lx++){
            for(int lz = 0; lz < SIZE; lz++){
                final int height = heightmapHighest.getHeight(lx, lz);
                for(int y = HEIGHT_IDX; y >= height; y--){
                    //chunk.setLight(lx, y, lz, 15);

                    updateSideSkyLight(chunk, lx, lz);
                }

                increase(chunk, lx, height, lz, 15);
            }
        }
    }

    private void updateSideSkyLight(ServerChunk chunk, int x, int z){
        final ChunkPos chunkPos = chunk.getPosition();

        final Heightmap heightmapHighest = chunk.getHeightMap(HeightmapType.HIGHEST);
        final int height = heightmapHighest.getHeight(x, z);

        for(int i = 0; i < 4; i++){
            final Vec2i dirNor = Direction.normal2DFromIndex(i);
            final int globalSideX = chunkPos.globalX() + x + dirNor.x;
            final int globalSideZ = chunkPos.globalZ() + z + dirNor.y;

            final ServerChunk sideChunk = level.getBlockChunk(globalSideX, globalSideZ);
            if(sideChunk == null)
                continue;

            final int localSideX = getLocalCoord(globalSideX);
            final int localSideZ = getLocalCoord(globalSideZ);

            final int sideHeight = heightmapHighest.getHeight(localSideX, localSideZ);
            if(sideHeight <= height)
                continue;

            int checkY = height + 1;
            if(checkY == sideHeight)
                continue;

            for(; checkY < sideHeight; checkY++){
                if(chunk.getBlockID(localSideX, checkY, localSideZ) != Blocks.AIR.getID())
                    continue;

                increase(sideChunk, localSideX, checkY, localSideZ, 15);
                chunk.setBlock(localSideX, checkY, localSideZ, Blocks.GLASS.getDefaultData());
            }
        }

    }

}
