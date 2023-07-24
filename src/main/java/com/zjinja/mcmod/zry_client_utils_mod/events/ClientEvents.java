package com.zjinja.mcmod.zry_client_utils_mod.events;

import com.zjinja.mcmod.zry_client_utils_mod.ZRYClientUtilsMod;
import com.zjinja.mcmod.zry_client_utils_mod.gui.GuiWEHelpPanel;
import com.zjinja.mcmod.zry_client_utils_mod.keybinds.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ZRYClientUtilsMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents{
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){
            var mc = Minecraft.getInstance();
            var player = mc.player;
            if (player == null) {
                ZRYClientUtilsMod.LOGGER.warn("error in onKeyInput event: player is null.");
                return;
            }
            if(KeyBindings.KEY_MAPPING_WE_PANEL.consumeClick()){
                ZRYClientUtilsMod.LOGGER.info("we_panel key pressed.");
                mc.setScreen(new GuiWEHelpPanel());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ZRYClientUtilsMod.MODID, value = Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            ZRYClientUtilsMod.LOGGER.info("ZRY Client Utils Mod Loaded.");
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.KEY_MAPPING_WE_PANEL);
        }
    }
}
