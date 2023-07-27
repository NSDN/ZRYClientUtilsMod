package com.zjinja.mcmod.zry_client_utils_mod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.ZRYClientUtilsMod;
import com.zjinja.mcmod.zry_client_utils_mod.cui.CUIRegionManager;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandGetWESelPos {
    public static final String CmdName = "get-we-sel-pos";

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(CmdName);
        command.executes((it) -> {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.INFO,
                    "cmd/get-we-sel-pos", "test command executed."
            );
            var player = Minecraft.getInstance().player;
            if (player == null) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "cmd/get-we-sel-pos", "player is null"
                );
                return Command.SINGLE_SUCCESS;
            }
            String desc;
            CUIRegionManager crm = CUIRegionManager.getInstance();
            if (crm != null) {
                desc = crm.describeSelection();
            }else{
                desc = "<ERROR: CUIRegionManager is null>";
            }
            player.sendSystemMessage(Component.literal(desc));
            return Command.SINGLE_SUCCESS;
        });

        dispatcher.register(command);
    }
}
