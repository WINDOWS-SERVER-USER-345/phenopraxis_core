package org.bandeng.phenopraxis_core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 矿脉生成全局配置（通过 TOML 文件配置）
 * 控制矿脉的网格间距、生成概率、最小距离等
 */
public class VeinGenConfig {

    public static class Common {
        public final ForgeConfigSpec.IntValue spacing;          // 矿脉网格间距（格）
        public final ForgeConfigSpec.IntValue minVeinDistance; // 任意两矿脉间最小距离
        public final ForgeConfigSpec.DoubleValue generationChance; // 每个网格点生成矿脉的概率

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

    // 静态便捷方法
    public static int getSpacing() {
        return COMMON.spacing.get();
    }

    public static int getMinVeinDistance() {
        return COMMON.minVeinDistance.get();
    }

    public static double getGenerationChance() {
        return COMMON.generationChance.get();
    }
}