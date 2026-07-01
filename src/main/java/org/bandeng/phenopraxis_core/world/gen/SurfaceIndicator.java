package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bandeng.phenopraxis_core.config.VeinTypeConfig.VeinTypeData;

/**
 * 地表标识物生成
 * 在矿脉正上方的地表生成一圈散落的碎屑和中心标记，帮助玩家发现矿脉
 */
public class SurfaceIndicator {

    /**
     * 在矿脉上方地表放置标识物
     * @param level 世界
     * @param veinCenter 矿脉中心坐标（地下）
     * @param type 矿脉类型（包含标识物方块信息）
     */
    public static void place(LevelAccessor level, BlockPos veinCenter, VeinTypeData type) {
        // 找到地表最高点
        BlockPos surfacePos = findSurface(level, veinCenter);
        if (surfacePos == null) return;

        RandomSource random = RandomSource.create(veinCenter.asLong());

        // ----- 外围 5x5 散落碎屑 -----
        // 在中心周围 5x5 区域内随机放置标识物方块
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                // 只在边缘和随机位置放置，避免过于密集
                if (Math.abs(dx) == 2 || Math.abs(dz) == 2 || random.nextDouble() < 0.4) {
                    BlockPos pos = surfacePos.offset(dx, 0, dz);
                    if (isSurfaceBlock(level.getBlockState(pos))) {
                        level.setBlock(pos, type.getSurfaceIndicatorBlock().defaultBlockState(), 3);
                    }
                }
            }
        }

        // ----- 中心标记（更明显的矿石堆） -----
        // 将中心方块替换为指定的中心标识物
        BlockPos centerSurface = surfacePos.above();
        if (!level.isEmptyBlock(centerSurface)) {
            centerSurface = surfacePos;
        }
        if (isSurfaceBlock(level.getBlockState(surfacePos))) {
            level.setBlock(surfacePos, type.getCenterIndicatorBlock().defaultBlockState(), 3);
        } else {
            level.setBlock(centerSurface, type.getCenterIndicatorBlock().defaultBlockState(), 3);
        }
    }

    /**
     * 从给定位置向上查找地表最高非空方块
     */
    private static BlockPos findSurface(LevelAccessor level, BlockPos from) {
        for (int y = 255; y > 0; y--) {
            BlockPos check = new BlockPos(from.getX(), y, from.getZ());
            if (!level.isEmptyBlock(check)) {
                return check;
            }
        }
        return null;
    }

    /**
     * 判断是否为地表自然方块（可被标识物替换）
     */
    private static boolean isSurfaceBlock(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) ||
                state.is(Blocks.COARSE_DIRT) || state.is(Blocks.PODZOL) ||
                state.is(Blocks.STONE) || state.is(Blocks.ANDESITE) ||
                state.is(Blocks.DIORITE) || state.is(Blocks.GRANITE);
    }
}