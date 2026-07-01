package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bandeng.phenopraxis_core.Phenopraxis;
import org.bandeng.phenopraxis_core.config.VeinGenConfig;
import org.bandeng.phenopraxis_core.config.VeinTypeConfig;
import org.bandeng.phenopraxis_core.config.VeinTypeConfig.VeinTypeData;
import org.bandeng.phenopraxis_core.content.block.ConcentrationOreBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * 矿脉放置主逻辑
 *
 * <p>完全数据驱动，所有矿物类型从 {@link VeinTypeConfig} 读取。
 * 浓度矿石（ConcentrationOreBlock）根据到矿脉中心的距离自动分配浓度等级：
 * 核心区=高浓度，中间区=中浓度，边缘区=低浓度。</p>
 */
public class OreVeinPlacer {

    private static final double CHANCE_PER_CHUNK = 0.04;

    public static void placeVeins(WorldGenLevel level, BlockPos chunkOrigin, RandomSource random) {
        // ===== 1. 概率控制 =====
        long seed = level.getSeed() + chunkOrigin.getX() * 31L + chunkOrigin.getZ() * 17L;
        RandomSource chunkRandom = RandomSource.create(seed);
        if (chunkRandom.nextDouble() > CHANCE_PER_CHUNK) {
            return;
        }

        // ===== 2. 选择矿脉类型 =====
        VeinTypeData type = VeinTypeConfig.selectRandomType(chunkRandom);
        if (type == null || !type.isValid()) {
            return;
        }

        // ===== 3. 生物群系过滤（数据驱动：从 JSON 的 biome_tags 字段读取） =====
        if (!type.biomeTags.isEmpty()) {
            Holder<Biome> biome = level.getBiome(chunkOrigin);
            boolean matchesAnyTag = false;
            for (String tagStr : type.biomeTags) {
                ResourceLocation tagLoc = ResourceLocation.tryParse(tagStr);
                if (tagLoc != null && biome.is(TagKey.create(Registries.BIOME, tagLoc))) {
                    matchesAnyTag = true;
                    break;
                }
            }
            if (!matchesAnyTag) {
                Phenopraxis.LOGGER.debug("Skipped {} vein: biome does not match required tags {}", type.id, type.biomeTags);
                return;
            }
        }

        // ===== 4. 中心定位 =====
        int offsetX = chunkRandom.nextInt(16);
        int offsetZ = chunkRandom.nextInt(16);
        int centerY = type.minHeight + chunkRandom.nextInt(type.maxHeight - type.minHeight + 1);
        BlockPos center = new BlockPos(
                chunkOrigin.getX() + offsetX,
                centerY,
                chunkOrigin.getZ() + offsetZ
        );

        // ===== 5. 防重叠 =====
        if (VeinRegistry.isTooClose(center, VeinGenConfig.getMinVeinDistance())) {
            return;
        }

        // ===== 6. 生成矿石位置 =====
        double grade = type.randomGrade(chunkRandom);
        int coreRadius = type.coreRadius;
        int edgeRadius = type.edgeRadius;

        List<BlockPos> orePositions = generateEllipsoidOres(
                center, coreRadius, type.coreDensity, edgeRadius, type.edgeDensity, chunkRandom
        );
        if (orePositions.isEmpty()) return;

        // ===== 7. 放置方块（根据距离分配浓度等级） =====
        long coreRSq = (long) coreRadius * coreRadius;
        long edgeRSq = (long) edgeRadius * edgeRadius;
        int placedCount = 0;

        for (BlockPos pos : orePositions) {
            if (!isReplaceable(level.getBlockState(pos))) continue;

            BlockState blockState;
            if (type.getOreBlock() instanceof ConcentrationOreBlock concentrationOre) {
                long distSq = (long) pos.distSqr(center);
                int tier;
                if (distSq <= coreRSq) {
                    tier = 2; // 高浓度
                } else if (distSq <= edgeRSq) {
                    tier = 1; // 中浓度
                } else {
                    tier = 0; // 低浓度
                }
                blockState = concentrationOre.withTier(tier);
            } else {
                blockState = type.getOreBlock().defaultBlockState();
            }

            level.setBlock(pos, blockState, 3);
            placedCount++;
        }

        if (placedCount == 0) return;

        // ===== 8. 注册账本 + 地表标识 =====
        VeinRegistry.registerVein(center, type.id, placedCount, grade);
        SurfaceIndicator.place(level, center, type);

        Phenopraxis.LOGGER.info("Placed {} vein at {} with {} ores, grade {}%",
                type.id, center, placedCount, String.format("%.1f", grade * 100));
    }

    private static List<BlockPos> generateEllipsoidOres(
            BlockPos center, int coreRadius, double coreDensity,
            int edgeRadius, double edgeDensity, RandomSource random) {

        List<BlockPos> positions = new ArrayList<>();
        long coreRSq = (long) coreRadius * coreRadius;
        long edgeRSq = (long) edgeRadius * edgeRadius;

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

    private static boolean isReplaceable(BlockState state) {
        return state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE) ||
                state.is(Blocks.ANDESITE) || state.is(Blocks.DIORITE) ||
                state.is(Blocks.GRANITE);
    }
}
