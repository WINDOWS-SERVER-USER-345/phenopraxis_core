package org.bandeng.phenopraxis_core.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bandeng.phenopraxis_core.Phenopraxis_core;

import java.util.function.Consumer;
import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS,
                    Phenopraxis_core.MODID
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> RAW_MATERIAL = registerItem("raw_material");

    private static RegistryObject<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> registerItem(String name, Function<Item.Properties, Item> factory) {
        return ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    private static RegistryObject<Item> registerSimplePropItem(
            String name,
            Consumer<Item.Properties> propertiesModifier) {

        return ITEMS.register(name, () -> {
            Item.Properties props = new Item.Properties();

            propertiesModifier.accept(props);

            return new Item(props);
        });
    }
}
