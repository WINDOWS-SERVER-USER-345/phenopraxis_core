package org.bandeng.phenopraxis_core;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.bandeng.phenopraxis_core.config.VeinGenConfig;
import org.bandeng.phenopraxis_core.config.VeinTypeConfig;
import org.bandeng.phenopraxis_core.core.command.FindVeinCommand;
import org.bandeng.phenopraxis_core.init.*;
import org.slf4j.Logger;

/**
 * Phenopraxis Core 模组主入口
 *
 * <p>本模组的核心目标是打造更科学的矿业科技线，
 * 包含自定义矿脉生成系统、品位系统和储量管理。</p>
 *
 * <h3>初始化流程：</h3>
 * <ol>
 *   <li>注册配置文件（TOML 格式的矿脉全局参数）</li>
 *   <li>注册方块、物品、创造标签页（通过 DeferredRegister 延迟注册）</li>
 *   <li>注册自定义世界生成特征（OreVeinFeature）</li>
 *   <li>注册数据包重载监听器（加载 vein_types.json 矿物类型配置）</li>
 *   <li>注册 Forge 游戏事件监听器（服务器启动等）</li>
 * </ol>
 */
@Mod(Phenopraxis.MODID)
public class Phenopraxis {
    public static final String MODID = "phenopraxis_core";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Phenopraxis() {
        // 获取模组事件总线（用于注册方块/物品/Feature 等生命周期内容）
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // ===== 1. 注册配置文件 =====
        // VeinGenConfig 控制矿脉生成的全局参数（间距、概率等），生成 TOML 配置文件
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                VeinGenConfig.COMMON_SPEC,
                "phenopraxis_core/vein_gen.toml"
        );

        // ===== 2. 注册方块、物品、创造标签页、世界生成特征 =====
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);

        // ===== 3. 注册数据包重载监听器 =====
        // 注意：AddReloadListenerEvent 是 Forge 游戏事件，必须注册到 MinecraftForge.EVENT_BUS
        // 而不是 modEventBus，否则会报 IllegalArgumentException
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new VeinTypeConfig());
        });

        // ===== 4. 注册 Forge 游戏事件总线 =====
        // 用于监听服务器启动等游戏运行时事件
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Phenopraxis Core mod initialized");
    }

    /**
     * 服务器启动时触发的回调
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Phenopraxis Core server starting");
    }

    /**
     * 注册模组命令
     * @param event 命令注册事件
     */
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        FindVeinCommand.register(event.getDispatcher());
        LOGGER.info("Phenopraxis Core commands registered");
    }
}
