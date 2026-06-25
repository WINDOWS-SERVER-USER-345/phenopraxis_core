package org.bandeng.phenopraxis_core.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis_core;
import net.minecraftforge.registries.ForgeRegistries;
public class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Phenopraxis_core.MODID);
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final RegistryObject<Block> RAW_MATERIAL_BLOCK =
            BLOCKS.register("raw_material_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

}