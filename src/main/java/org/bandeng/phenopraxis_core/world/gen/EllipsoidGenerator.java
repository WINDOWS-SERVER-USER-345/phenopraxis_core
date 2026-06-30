package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EllipsoidGenerator {
    private final BlockPos center;
    private final int radiusX;
    private final int radiusY;
    private final int radiusZ;
    private final double density;

    public EllipsoidGenerator(BlockPos center, int radiusX, int radiusY, int radiusZ, double density) {
        this.center = center;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
        this.density = density;
    }

    public List<BlockPos> generatePositions(Random rand) {
        List<BlockPos> positions = new ArrayList<>();
        int radiusXSq = radiusX * radiusX;
        int radiusYSq = radiusY * radiusY;
        int radiusZSq = radiusZ * radiusZ;
        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dy = -radiusY; dy <= radiusY; dy++) {
                for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                    if (dx * dx / radiusXSq + dy * dy / radiusYSq + dz * dz / radiusZSq <= 1) {
                        positions.add(center.offset(dx, dy, dz));
                    }
                }
            }
        }
        return positions;
    }

    public int getEstimatedOreCount(){
        double volume = (4.0/3.0) * Math.PI * radiusX * radiusY * radiusZ;
        return (int) (volume * density);
    }
}

