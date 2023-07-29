package com.zjinja.mcmod.zry_client_utils_mod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.cui.CUIRegionManager;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ConfigMgr;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandReloadConfig {
    public static final String CmdName = "reload-zry-client-utils-config";

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(CmdName);
        command.executes((it) -> {
            var player = Minecraft.getInstance().player;
            if (player == null) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "cmd/reload-zry-client-utils-config", "player is null"
                );
                return Command.SINGLE_SUCCESS;
            }
            var cm = ConfigMgr.getInstance();
            if (cm == null) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "cmd/reload-zry-client-utils-config", "ConfigMgr is null"
                );
                player.sendSystemMessage(Component.translatable("chat.tip.reload_config_fail"));
                return Command.SINGLE_SUCCESS;
            }
            boolean isOK = cm.reloadConfig();
            if(isOK) {
                player.sendSystemMessage(Component.translatable("chat.tip.reload_config_ok"));
            }else {
                player.sendSystemMessage(Component.translatable("chat.tip.reload_config_fail"));
            }
            return Command.SINGLE_SUCCESS;
        });

        dispatcher.register(command);
    }
}
