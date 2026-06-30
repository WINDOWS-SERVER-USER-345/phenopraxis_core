package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;
import org.bandeng.phenopraxis_core.Phenopraxis;

import java.util.HashMap;
import java.util.Map;

/**
 * 矿脉注册表 - 用于在生成时记录所有矿脉，防止重叠
 * 注意：这只是内存缓存，真正的持久化在 OreVeinDataManager 中
 */
public class VeinRegistry {

    // 内存缓存：矿脉中心坐标 -> 矿脉信息
    private static final Map<BlockPos, VeinEntry> REGISTERED_VEINS = new HashMap<>();

    /**
     * 注册一个矿脉
     */
    public static void registerVein(BlockPos center, String type, int totalOres, double grade) {
        VeinEntry entry = new VeinEntry(center, type, totalOres, grade);
        REGISTERED_VEINS.put(center, entry);
        Phenopraxis.LOGGER.info("Registered vein: {} at {}, {} ores, grade {}%",
                type, center, totalOres, String.format("%.1f", grade * 100));
        // TODO: 同时写入全局账本（OreVeinDataManager）
    }

    /**
     * 检查一个位置是否离已有矿脉太近
     */
    public static boolean isTooClose(BlockPos pos, int minDistance) {
        int minDistSq = minDistance * minDistance;
        for (BlockPos existing : REGISTERED_VEINS.keySet()) {
            if (existing.distSqr(pos) < minDistSq) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取已注册的所有矿脉（用于调试）
     */
    public static Map<BlockPos, VeinEntry> getRegisteredVeins() {
        return new HashMap<>(REGISTERED_VEINS);
    }

    /**
     * 清空注册表（用于世界重载时）
     */
    public static void clear() {
        REGISTERED_VEINS.clear();
    }

    /**
     * 矿脉条目
     */
    public static class VeinEntry {
        public final BlockPos center;
        public final String type;
        public final int totalOres;
        public final double grade;

        public VeinEntry(BlockPos center, String type, int totalOres, double grade) {
            this.center = center;
            this.type = type;
            this.totalOres = totalOres;
            this.grade = grade;
        }
    }
}