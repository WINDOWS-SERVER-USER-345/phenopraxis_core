package org.bandeng.phenopraxis_core.init;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis;
import org.bandeng.phenopraxis_core.world.gen.OreVeinFeature;

/**
 * 注册自定义世界生成特征（Feature）
 * 这是我们矿脉生成的核心入口，被数据包中的 ConfiguredFeature 引用
 */
public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, Phenopraxis.MODID);

    /**
     * 注册矿脉生成特征
     * 该特征无配置参数（NoneFeatureConfiguration）
     */
    public static final RegistryObject<OreVeinFeature> ORE_VEIN =
            FEATURES.register("ore_vein", OreVeinFeature::new);
}