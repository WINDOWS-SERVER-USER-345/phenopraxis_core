package org.bandeng.phenopraxis_core.init;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis;
import org.bandeng.phenopraxis_core.content.block.ConcentrationOreBlock;

/**
 * 方块注册表 —— 统一管理本模组所有方块
 *
 * <p>浓度矿石统一使用 {@link ConcentrationOreBlock}，
 * 只需在此注册 + 写对应 blockstate/models 即可扩展新矿种。</p>
 */
public class ModBlocks {

    public static final DeferredRegister<net.minecraft.world.level.block.Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Phenopraxis.MODID);

    /** 磁铁矿（黑色，低/中/高三种贴图） */
    public static final RegistryObject<ConcentrationOreBlock> MAGNETITE_ORE = BLOCKS.register("magnetite_ore",
            () -> new ConcentrationOreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(3.0f, 3.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            )
    );

    /** 赤铁矿（红色，低/中/高三种贴图） */
    public static final RegistryObject<ConcentrationOreBlock> HEMATITE_ORE = BLOCKS.register("hematite_ore",
            () -> new ConcentrationOreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(3.0f, 3.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            )
    );
}
