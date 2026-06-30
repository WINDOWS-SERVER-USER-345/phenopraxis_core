package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bandeng.phenopraxis_core.init.ModBlocks;

public class SurfaceIndicator {

    /**
     * 在矿脉正上方的地表生成标识物
     * @param level 世界
     * @param veinCenter 矿脉中心坐标
     * @param type 矿脉类型（iron/copper/tin/gold/coal）
     */
    public static void place(LevelAccessor level, BlockPos veinCenter, String type) {
        // 从矿脉中心向上找到地表
        BlockPos surfacePos = findSurface(level, veinCenter);
        if (surfacePos == null) {
            return;
        }

        RandomSource random = RandomSource.create(veinCenter.asLong());

        // 获取对应类型的标识物方块
        Block indicatorBlock = getIndicatorBlock(type);
        Block centerBlock = getCenterIndicatorBlock(type);

        // 在表面生成一个 5x5 的碎屑散落区域
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                // 只在外围和随机位置放置
                if (Math.abs(dx) == 2 || Math.abs(dz) == 2 || random.nextDouble() < 0.4) {
                    BlockPos pos = surfacePos.offset(dx, 0, dz);
                    BlockState state = level.getBlockState(pos);
                    // 只替换地表自然方块
                    if (state.is(Blocks.GRASS_BLOCK) ||
                            state.is(Blocks.DIRT) ||
                            state.is(Blocks.COARSE_DIRT) ||
                            state.is(Blocks.PODZOL) ||
                            state.is(Blocks.STONE) ||
                            state.is(Blocks.ANDESITE) ||
                            state.is(Blocks.DIORITE) ||
                            state.is(Blocks.GRANITE)) {
                        level.setBlock(pos, indicatorBlock.defaultBlockState(), 3);
                    }
                }
            }
        }

        // 在中心位置放置一个更明显的标记（如小矿石堆）
        BlockPos centerSurface = surfacePos.above();
        if (!level.isEmptyBlock(centerSurface)) {
            // 如果中心位置有方块，放在旁边
            centerSurface = surfacePos;
        }
        // 如果地面是草地/泥土，直接替换；否则放在上面
        if (level.getBlockState(surfacePos).is(Blocks.GRASS_BLOCK) ||
                level.getBlockState(surfacePos).is(Blocks.DIRT)) {
            level.setBlock(surfacePos, centerBlock.defaultBlockState(), 3);
        } else {
            level.setBlock(centerSurface, centerBlock.defaultBlockState(), 3);
        }
    }

    /**
     * 从矿脉中心向上找到地表最高方块
     */
    private static BlockPos findSurface(LevelAccessor level, BlockPos from) {
        int x = from.getX();
        int z = from.getZ();
        for (int y = 255; y > 0; y--) {
            BlockPos check = new BlockPos(x, y, z);
            if (!level.isEmptyBlock(check)) {
                return check;
            }
        }
        return null;
    }

    /**
     * 根据矿脉类型获取外围碎屑方块
     * 这里先用原版方块占位，后续替换成自己的
     */
    private static Block getIndicatorBlock(String type) {
        switch (type.toLowerCase()) {
            case "raw_material_block": return Blocks.BLACKSTONE;   // 用黑色碎石标识
            default: return Blocks.COBBLESTONE;
        }
    }


    /**
     * 根据矿脉类型获取中心标识物方块
     */
    public static Block getCenterIndicatorBlock(String type) {
        switch (type.toLowerCase()) {
            case "raw_material_block": return ModBlocks.RAW_MATERIAL_BLOCK.get(); // 用磁铁矿本身
            default: return Blocks.STONE;
        }
    }

}