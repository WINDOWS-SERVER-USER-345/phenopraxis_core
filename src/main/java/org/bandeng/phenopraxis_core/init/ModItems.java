package org.bandeng.phenopraxis_core.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis;

/**
 * 物品注册表 —— 统一管理本模组所有物品
 *
 * <p>方块类物品使用 {@link BlockItem}，它会自动关联对应方块的属性。
 * 后续如需添加粉末、锭等非方块物品，直接注册 {@link Item} 即可。</p>
 */
public class ModItems {

    /** 物品延迟注册器 */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Phenopraxis.MODID);

    /** 磁铁矿方块物品 —— 玩家可在物品栏中持有和放置 */
    public static final RegistryObject<BlockItem> MAGNETITE_ORE_ITEM = ITEMS.register("magnetite_ore",
            () -> new BlockItem(ModBlocks.MAGNETITE_ORE.get(), new Item.Properties())
    );

    /** 赤铁矿方块物品 */
    public static final RegistryObject<BlockItem> HEMATITE_ORE_ITEM = ITEMS.register("hematite_ore",
            () -> new BlockItem(ModBlocks.HEMATITE_ORE.get(), new Item.Properties())
    );
}
