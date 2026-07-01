package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;
import org.bandeng.phenopraxis_core.Phenopraxis;

import java.util.HashMap;
import java.util.Map;

/**
 * 矿脉注册表（内存缓存）
 * 用于在生成时记录所有矿脉的位置，防止新生成的矿脉与已有矿脉重叠
 * 真正的持久化账本（储量管理）将在后续实现
 */
public class VeinRegistry {

    // 存储矿脉中心坐标 -> 矿脉信息
    private static final Map<BlockPos, VeinEntry> REGISTERED_VEINS = new HashMap<>();

    /**
     * 注册一个矿脉
     * @param center 矿脉中心坐标
     * @param type 矿脉类型ID
     * @param totalOres 矿石总数
     * @param grade 品位
     */
    public static void registerVein(BlockPos center, String type, int totalOres, double grade) {
        VeinEntry entry = new VeinEntry(center, type, totalOres, grade);
        REGISTERED_VEINS.put(center, entry);
        Phenopraxis.LOGGER.debug("Registered vein: {} at {}, {} ores, grade {}%",
                type, center, totalOres, String.format("%.1f", grade * 100));
    }

    /**
     * 检查某个位置是否离已有矿脉太近
     * @param pos 待检查位置
     * @param minDistance 最小允许距离（格）
     * @return true 如果太近
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
     * 清空注册表（用于世界重载）
     */
    public static void clear() {
        REGISTERED_VEINS.clear();
    }

    /**
     * 获取所有已注册的矿脉（只读副本）
     */
    public static Map<BlockPos, VeinEntry> getAllVeins() {
        return new HashMap<>(REGISTERED_VEINS);
    }

    /**
     * 查找距离指定位置最近的指定类型矿脉
     * @param from 搜索起点（通常是玩家位置）
     * @param type 矿脉类型 ID（如 "magnetite"、"hematite"）
     * @return 最近的矿脉条目，找不到返回 null
     */
    public static VeinEntry findNearest(BlockPos from, String type) {
        VeinEntry nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (VeinEntry entry : REGISTERED_VEINS.values()) {
            if (entry.type.equals(type)) {
                double dist = entry.center.distSqr(from);
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearest = entry;
                }
            }
        }
        return nearest;
    }

    /** 矿脉信息条目 */
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