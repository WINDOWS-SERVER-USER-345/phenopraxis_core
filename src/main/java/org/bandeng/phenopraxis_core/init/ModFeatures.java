package org.bandeng.phenopraxis_core.init;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis;
import org.bandeng.phenopraxis_core.world.gen.OreVeinFeature;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, Phenopraxis.MODID);

    public static final RegistryObject<OreVeinFeature> ORE_VEIN =
            FEATURES.register("ore_vein", OreVeinFeature::new);
}