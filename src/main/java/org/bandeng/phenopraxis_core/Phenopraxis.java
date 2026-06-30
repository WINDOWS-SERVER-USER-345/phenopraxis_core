package org.bandeng.phenopraxis_core;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.bandeng.phenopraxis_core.config.VeinGenConfig;
import org.bandeng.phenopraxis_core.init.*;
import org.slf4j.Logger;

@Mod(Phenopraxis.MODID)
public class Phenopraxis {
    public static final String MODID = "phenopraxis_core";
    public static final Logger LOGGER = LogUtils.getLogger();


    public Phenopraxis() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        // 注册配置
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                VeinGenConfig.COMMON_SPEC,
                "phenopraxis_core/vein_gen.toml"
        );

        // 注册物品/方块
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // 注册世界生成 Feature
        ModFeatures.FEATURES.register(modEventBus);
        // 生命周期事件
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        forgeEventBus.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Phenopraxis Core: Common setup complete.");
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Phenopraxis Core: Client setup complete.");
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // 添加物品到创造模式标签页（可选）
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Phenopraxis Core: Server starting.");
    }
}