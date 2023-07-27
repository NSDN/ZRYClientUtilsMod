package com.zjinja.mcmod.zry_client_utils_mod.networking;

import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.cui.CUIRegionManager;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkInstance;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class NetworkHandlerWECUI {
    public static EventNetworkChannel WENetworkChannel;

    public static void hook() {
        var resLoc = new ResourceLocation("worldedit", "cui");
        var nr = NetworkRegistry.class;
        try {
            var methFindTarget = nr.getDeclaredMethod("findTarget", ResourceLocation.class);
            methFindTarget.setAccessible(true);
            var result = methFindTarget.invoke(null, resLoc);
            if(result.getClass().equals(Optional.class)){
                Optional<Object> niop = (Optional<Object>) result;
                if(niop.isPresent()){
                    Object niobj = niop.get();
                    if(niobj instanceof NetworkInstance) {
                        NetworkInstance ni = (NetworkInstance) niobj;
                        //ni.addListener(NetworkHandlerWECUI::onClientPacketData);
                        ni.addListener(NetworkHandlerWECUI::onServerPacketData);
                        ZLogUtil.log(
                                LogUtils.getLogger(), ZLogUtil.Level.INFO,
                                "wecui/getni",
                                "Set network package listener for 'worldedit:cui': OK."
                        );
                    }else{
                        ZLogUtil.log(
                                LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                                "wecui/reflect",
                                "Return value of method 'findTarget' from 'NetworkRegistry': Type error:" +
                                        "expected 'Optional<NetworkInstance>', got 'Optional<{}>'",
                                niobj.getClass().getTypeName()
                        );
                    }
                }else{
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                            "wecui/getni",
                            "Can not get NetworkInstance of 'worldedit:cui'"
                    );
                }
            }else{
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                        "wecui/reflect",
                        "Return value of method 'findTarget' from 'NetworkRegistry': Type error:" +
                                "expected 'Optional<T>', got '{}<T>'",
                        result.getClass().getTypeName()
                );
            }


        } catch (NoSuchMethodException e) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "wecui/reflect", "Can not get method 'findTarget' from 'NetworkRegistry'"
            );
        } catch (InvocationTargetException e) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "wecui/reflect",
                    "Can not call method 'findTarget' from 'NetworkRegistry': InvocationTargetException.",
                    e
            );
        } catch (IllegalAccessException e) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "wecui/reflect",
                    "Can not call method 'findTarget' from 'NetworkRegistry': IllegalAccessException.",
                    e
            );
        }
    }

    public static void onEnterServer() {
        onQuitServer();
        var player = Minecraft.getInstance().player;
        if (player != null){
            player.commandUnsigned("we cui");
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.INFO,
                    "wecui/server-cui-load",
                    "issue server command '/we cui': OK."
            );
        }else {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "wecui/server-cui-load",
                    "issue server command '/we cui' failed: player is null."
            );
        }
    }

    public static void onQuitServer() {
        var crm = CUIRegionManager.getInstance();
        if (crm != null) {
            crm.clearSelection();
            crm.clearRegions();
        }
    }

    public static void onServerPacketData(NetworkEvent.ServerCustomPayloadEvent event) {
        String payload = event.getPayload().toString(StandardCharsets.UTF_8);
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.INFO,
                "debug/net/pkg/wecui", "Received ServerCustomPayloadEvent: {}",
                payload
        );
        processMessage(payload);
    }

    public static void processMessage(String message)
    {
        String[] split = message.split("\\|", -1);
        if(split.length < 1) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.INFO,
                    "wecui/process-pkg", "Failed process message: split.length<=0"
            );
            return;
        }
        boolean multi = split[0].startsWith("+");
        String type = split[0].substring(multi ? 1 : 0);
        String args = message.substring(type.length() + (multi ? 2 : 1));
        var crm = CUIRegionManager.getInstance();
        if (crm != null){
            crm.update(multi, type, args.split("\\|", -1));
        }else{
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "wecui/process-pkg", "CUIRegionManager INSTANCE is null."
            );
        }
    }

/*
        ServerPlayer player = event.getSource().get().getSender();
        LocalSession session = ForgeWorldEdit.inst.getSession(player);
        String text = event.getPayload().toString(StandardCharsets.UTF_8);
        final ForgePlayer actor = adaptPlayer(player);
        session.handleCUIInitializationMessage(text, actor);
*/
}
