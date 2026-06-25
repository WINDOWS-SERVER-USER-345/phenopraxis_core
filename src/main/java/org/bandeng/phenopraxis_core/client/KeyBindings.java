package org.bandeng.phenopraxis_core.client; // 放在 client 包下

import com.mojang.blaze3d.platform.InputConstants;
import com.sighs.apricityui.ApricityUI;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.bandeng.phenopraxis_core.Phenopraxis_core;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Phenopraxis_core.MODID, value = Dist.CLIENT)
public class KeyBindings {
    public static final String KEY_CATEGORY = "key.category." + Phenopraxis_core.MODID;
    public static final String KEY_OPEN_TECH_TREE = "key." + Phenopraxis_core.MODID + ".open_tech_tree";

    public static KeyMapping openTechTreeKey = new KeyMapping(
            KEY_OPEN_TECH_TREE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            KEY_CATEGORY
    );

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(openTechTreeKey);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // 按下按键且未按下其他修饰键（Ctrl/Shift等），避免误触
        if (openTechTreeKey.consumeClick()) {
            ApricityUI.openScreen("tech_tree.html");
        }
    }
}