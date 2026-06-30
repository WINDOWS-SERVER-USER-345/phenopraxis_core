package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bandeng.phenopraxis_core.Phenopraxis;
import org.bandeng.phenopraxis_core.config.VeinGenConfig;

import java.util.ArrayList;
import java.util.List;

public class OreVeinPlacer {

    // 每个区块生成矿脉的概率（约4%，即平均25个区块生成1个）
    private static final double CHANCE_PER_CHUNK = 0.04;

    public static void placeVeins(WorldGenLevel level, BlockPos chunkOrigin, RandomSource random) {
        // 使用区块坐标作为种子，保证世界一致性
        long seed = level.getSeed() + chunkOrigin.getX() * 31L + chunkOrigin.getZ() * 17L;
        RandomSource chunkRandom = RandomSource.create(seed);

        // 概率控制
        if (chunkRandom.nextDouble() > CHANCE_PER_CHUNK) {
            return;
        }

        // 在区块内随机偏移（0~15）
        int offsetX = chunkRandom.nextInt(16);
        int offsetZ = chunkRandom.nextInt(16);
        // 随机选择矿脉类型
        String type = selectVeinType(chunkRandom);
        // 随机高度
        int minY = VeinGenConfig.getMinHeight(type);
        int maxY = VeinGenConfig.getMaxHeight(type);
        int centerY = minY + chunkRandom.nextInt(maxY - minY + 1);

        BlockPos center = new BlockPos(
                chunkOrigin.getX() + offsetX,
                centerY,
                chunkOrigin.getZ() + offsetZ
        );

        // 防止重叠
        if (VeinRegistry.isTooClose(center, VeinGenConfig.getMinVeinDistance())) {
            return;
        }

        // 获取配置参数
        int coreRadius = VeinGenConfig.getCoreRadius(type);
        double coreDensity = VeinGenConfig.getCoreDensity(type);
        int edgeRadius = VeinGenConfig.getEdgeRadius(type);
        double edgeDensity = VeinGenConfig.getEdgeDensity(type);
        double grade = VeinGenConfig.getMinGrade(type) +
                chunkRandom.nextDouble() * (VeinGenConfig.getMaxGrade(type) - VeinGenConfig.getMinGrade(type));

        // 生成椭球体
        List<BlockPos> orePositions = generateEllipsoidOres(
                center, coreRadius, coreDensity, edgeRadius, edgeDensity, chunkRandom
        );

        if (orePositions.isEmpty()) {
            return;
        }

        BlockState oreBlock = getOreBlock(type);
        int placedCount = 0;
        for (BlockPos pos : orePositions) {
            if (level.getBlockState(pos).is(Blocks.STONE) ||
                    level.getBlockState(pos).is(Blocks.DEEPSLATE) ||
                    level.getBlockState(pos).is(Blocks.ANDESITE) ||
                    level.getBlockState(pos).is(Blocks.DIORITE) ||
                    level.getBlockState(pos).is(Blocks.GRANITE)) {
                level.setBlock(pos, oreBlock, 3);
                placedCount++;
            }
        }

        if (placedCount == 0) {
            return;
        }

        VeinRegistry.registerVein(center, type, placedCount, grade);
        SurfaceIndicator.place(level, center, type);

        Phenopraxis.LOGGER.info("Placed {} vein at {} with {} ores, grade {}%",
                type, center, placedCount, String.format("%.1f", grade * 100));
    }

    private static List<BlockPos> generateEllipsoidOres(
            BlockPos center, int coreRadius, double coreDensity,
            int edgeRadius, double edgeDensity, RandomSource random) {

        List<BlockPos> positions = new ArrayList<>();
        int coreRSq = coreRadius * coreRadius;
        int edgeRSq = edgeRadius * edgeRadius;

        for (int dx = -edgeRadius; dx <= edgeRadius; dx++) {
            for (int dy = -edgeRadius; dy <= edgeRadius; dy++) {
                for (int dz = -edgeRadius; dz <= edgeRadius; dz++) {
                    double distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq <= edgeRSq) {
                        double density;
                        if (distSq <= coreRSq) {
                            density = coreDensity;
                        } else {
                            double t = (distSq - coreRSq) / (double) (edgeRSq - coreRSq);
                            density = coreDensity + (edgeDensity - coreDensity) * t;
                        }
                        if (random.nextDouble() < density) {
                            positions.add(center.offset(dx, dy, dz));
                        }
                    }
                }
            }
        }
        return positions;
    }

    private static String selectVeinType(RandomSource random) {
        double r = random.nextDouble();
        return "raw_material_block";
    }

    private static BlockState getOreBlock(String type) {
        switch (type.toLowerCase()) {
            case "raw_material_block": return Blocks.RAW_IRON_BLOCK.defaultBlockState();
            default: return Blocks.IRON_ORE.defaultBlockState();
        }
    }
}