package org.bandeng.phenopraxis_core.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.bandeng.phenopraxis_core.Phenopraxis;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 自定义矿脉生成特征
 * 由 Minecraft 世界生成器调用，负责在特定区块尝试生成矿脉
 * 实际放置逻辑委托给 OreVeinPlacer
 */
@ParametersAreNonnullByDefault
public class OreVeinFeature extends Feature<NoneFeatureConfiguration> {

    public OreVeinFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();  // 当前区块的原点坐标（方块坐标）
        RandomSource random = context.random();

        try {
            // 调用矿脉放置主逻辑
            OreVeinPlacer.placeVeins(level, origin, random);
            return true;
        } catch (Exception e) {
            Phenopraxis.LOGGER.error("Error placing ore vein at {}", origin, e);
            return false;
        }
    }
}