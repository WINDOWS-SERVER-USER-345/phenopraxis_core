package org.bandeng.phenopraxis_core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class VeinGenConfig {

    public static class Common {
        // ===== 全局配置 =====

        public final ForgeConfigSpec.IntValue spacing;
        public final ForgeConfigSpec.IntValue minVeinDistance;
        public final ForgeConfigSpec.DoubleValue generationChance;
        public final ForgeConfigSpec.IntValue maxVeinsPerRegion;
        public final ForgeConfigSpec.IntValue regionSize;

        // ===== 各矿脉配置 =====
        public final VeinParams raw_material_block;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("global");
            spacing = builder
                    .comment("矿脉网格间距（格），统一值保证分布均匀")
                    .defineInRange("spacing", 400, 100, 1000);
            minVeinDistance = builder
                    .comment("任意两个矿脉之间的最小距离（格）")
                    .defineInRange("min_vein_distance", 300, 100, 800);
            generationChance = builder
                    .comment("每个网格点生成矿脉的概率（0-1）")
                    .defineInRange("vein_generation_chance", 0.35, 0.01, 1.0);
            maxVeinsPerRegion = builder
                    .comment("每个区域（region_size x region_size）最多生成矿脉数")
                    .defineInRange("max_veins_per_region", 2, 0, 5);
            regionSize = builder
                    .comment("区域大小（格），配合 max_veins_per_region 使用")
                    .defineInRange("region_size", 500, 100, 1000);
            builder.pop();

            raw_material_block = new VeinParams(builder, "raw_material_block", 4, 400, 10, 50, 10, 0.8, 18, 0.3, 0.3, 0.7);
        }
    }

    public static class VeinParams {
        public final ForgeConfigSpec.IntValue weight;
        public final ForgeConfigSpec.IntValue spacing;
        public final ForgeConfigSpec.IntValue minHeight;
        public final ForgeConfigSpec.IntValue maxHeight;
        public final ForgeConfigSpec.IntValue coreRadius;
        public final ForgeConfigSpec.DoubleValue coreDensity;
        public final ForgeConfigSpec.IntValue edgeRadius;
        public final ForgeConfigSpec.DoubleValue edgeDensity;
        public final ForgeConfigSpec.DoubleValue minGrade;
        public final ForgeConfigSpec.DoubleValue maxGrade;

        VeinParams(ForgeConfigSpec.Builder builder, String name, int defaultWeight,
                   int defaultSpacing, int defaultMinY, int defaultMaxY,
                   int defaultCoreR, double defaultCoreD,
                   int defaultEdgeR, double defaultEdgeD,
                   double defaultMinG, double defaultMaxG) {
            builder.push(name);
            weight = builder
                    .comment("生成权重（越高越常见）")
                    .defineInRange("weight", defaultWeight, 0, 10);
            spacing = builder
                    .comment("该矿脉的网格间距（建议与global.spacing保持一致）")
                    .defineInRange("spacing", defaultSpacing, 100, 1000);
            minHeight = builder
                    .comment("最低生成高度")
                    .defineInRange("min_height", defaultMinY, 0, 255);
            maxHeight = builder
                    .comment("最高生成高度")
                    .defineInRange("max_height", defaultMaxY, 0, 255);
            coreRadius = builder
                    .comment("核心区半径（高密度区）")
                    .defineInRange("core_radius", defaultCoreR, 2, 30);
            coreDensity = builder
                    .comment("核心区矿石填充概率")
                    .defineInRange("core_density", defaultCoreD, 0.1, 1.0);
            edgeRadius = builder
                    .comment("边缘区半径（过渡区）")
                    .defineInRange("edge_radius", defaultEdgeR, 2, 40);
            edgeDensity = builder
                    .comment("边缘区矿石填充概率")
                    .defineInRange("edge_density", defaultEdgeD, 0.01, 0.8);
            minGrade = builder
                    .comment("该矿脉最低品位")
                    .defineInRange("min_grade", defaultMinG, 0.0, 1.0);
            maxGrade = builder
                    .comment("该矿脉最高品位")
                    .defineInRange("max_grade", defaultMaxG, 0.0, 1.0);
            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    // ========== 静态便捷访问方法 ==========

    public static int getSpacing() {
        return COMMON.spacing.get();
    }

    public static int getMinVeinDistance() {
        return COMMON.minVeinDistance.get();
    }

    public static double getGenerationChance() {
        return COMMON.generationChance.get();
    }

    public static VeinParams getVeinParams(String type) {
        switch (type.toLowerCase()) {
            case "magnetite": return COMMON.raw_material_block;
            default: return null;
        }
    }

    public static int getWeight(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.weight.get() : 0;
    }

    public static int getMinHeight(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.minHeight.get() : 0;
    }

    public static int getMaxHeight(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.maxHeight.get() : 255;
    }

    public static int getCoreRadius(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.coreRadius.get() : 8;
    }

    public static double getCoreDensity(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.coreDensity.get() : 0.8;
    }

    public static int getEdgeRadius(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.edgeRadius.get() : 16;
    }

    public static double getEdgeDensity(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.edgeDensity.get() : 0.25;
    }

    public static double getMinGrade(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.minGrade.get() : 0.1;
    }

    public static double getMaxGrade(String type) {
        VeinParams p = getVeinParams(type);
        return p != null ? p.maxGrade.get() : 0.9;
    }
}