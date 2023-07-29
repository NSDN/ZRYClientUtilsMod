package com.zjinja.mcmod.zry_client_utils_mod.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.ZRYClientUtilsMod;
import com.zjinja.mcmod.zry_client_utils_mod.commands.CommandGetWESelPos;
import com.zjinja.mcmod.zry_client_utils_mod.commands.CommandReloadConfig;
import com.zjinja.mcmod.zry_client_utils_mod.cui.CUIRegionManager;
import com.zjinja.mcmod.zry_client_utils_mod.gui.GuiWEHelpPanel;
import com.zjinja.mcmod.zry_client_utils_mod.keybinds.KeyBindings;
import com.zjinja.mcmod.zry_client_utils_mod.networking.NetworkHandlerWECUI;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RenderContext;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ConfigMgr;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
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
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "key-event", "error in onKeyInput event: player is null."
                );
                return;
            }
            if(KeyBindings.KEY_MAPPING_WE_PANEL.consumeClick()){
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.INFO,
                        "key-event", "we_panel key pressed."
                );
                mc.setScreen(new GuiWEHelpPanel());
            }
        }

        @SubscribeEvent
        public static void registerClientCommand(RegisterClientCommandsEvent event) {
            new CommandGetWESelPos().register(event.getDispatcher());
            new CommandReloadConfig().register(event.getDispatcher());
        }

        @SubscribeEvent
        public static void loggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.INFO,
                    "client-logged-in", "Logged into server."
            );
            NetworkHandlerWECUI.onEnterServer();
        }

        @SubscribeEvent
        public static void loggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.INFO,
                    "client-logged-out", "Logged out of server."
            );
            NetworkHandlerWECUI.onQuitServer();
        }

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent event) {
            //if(event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS)) {
            if(true) {
                LevelRenderer lr = event.getLevelRenderer();
                PoseStack ps = event.getPoseStack();
                RenderContext rctx = new RenderContext(ps);
                CUIRegionManager.render(rctx);
            }
        }


    }

    @Mod.EventBusSubscriber(modid = ZRYClientUtilsMod.MODID, value = Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.INFO,
                    "client-setup", "ZRY Client Utils Mod Loaded."
            );
            ConfigMgr cm = ConfigMgr.getInstance();
            if(cm == null) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.INFO,
                        "client-setup", "Failed get ConfigMgr: is null."
                );
            }else{
                cm.init();
            }
            NetworkHandlerWECUI.hook();
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.KEY_MAPPING_WE_PANEL);
        }
    }
}
