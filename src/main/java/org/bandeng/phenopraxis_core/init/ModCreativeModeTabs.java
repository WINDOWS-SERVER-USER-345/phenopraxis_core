package org.bandeng.phenopraxis_core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis;

/**
 * 创造模式标签页注册
 *
 * <p>在创造模式物品栏中创建一个专属标签页，
 * 方便开发者和玩家快速获取本模组的所有物品。</p>
 */
public class ModCreativeModeTabs {

    /** 标签页延迟注册器 */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Phenopraxis.MODID);

    /**
     * Phenopraxis 专属创造标签页
     * <p>图标使用磁铁矿方块，后续可替换为更有代表性的物品。</p>
     */
    public static final RegistryObject<CreativeModeTab> PHENOPRAXIS_TAB =
            CREATIVE_MODE_TABS.register("phenopraxis_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.MAGNETITE_ORE_ITEM.get()))
                            .title(Component.translatable("creativetab.phenopraxis_core.main"))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.MAGNETITE_ORE_ITEM.get());
                                output.accept(ModItems.HEMATITE_ORE_ITEM.get());
                            })
                            .build()
            );
}
