package org.bandeng.phenopraxis_core.init;

import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis_core;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Phenopraxis_core.MODID);


    public static final RegistryObject<CreativeModeTab> PHENOPRAXIS =
            CREATIVE_MODE_TABS.register("phenopraxia_core",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.RAW_MATERIAL.get()))
                            .title(Component.translatable("tab.phenopraxis_core"))
                            .displayItems((itemDisplayParameters, output) -> {
                                output.accept(ModItems.RAW_MATERIAL.get());    // 粗材料
                                output.accept(ModBlocks.RAW_MATERIAL_BLOCK.get());    // 粗材料块
                            })

                            .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}