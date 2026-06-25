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

                            // 设置创造标签在界面中显示的图标。
                            // 这里使用石头作为示例图标，后续可以替换为模组物品。
                            .icon(() -> new ItemStack(Items.STONE))

                            // 设置标签的显示名称。
                            // 使用可本地化文本（语言文件中定义）。
                            .title(Component.translatable("tab.phenopraxis_core"))

                            // 定义该标签中显示的物品内容。
                            // output.accept(...) 用于向标签中添加物品。
                            .displayItems((itemDisplayParameters, output) -> {

                            })

                            // 构建最终的 CreativeModeTab 实例。
                            .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}