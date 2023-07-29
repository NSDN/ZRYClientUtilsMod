package com.zjinja.mcmod.zry_client_utils_mod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.marker.MarkerMgr;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CommandMarker {
    public static final String CmdName = "zcu-marker";

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(CmdName);
        command
                .then(
                        literal("add-point")
                                .then(argument("x1", IntegerArgumentType.integer())
                                        .then(argument("y1", IntegerArgumentType.integer())
                                                .then(argument("z1", IntegerArgumentType.integer())
                                                        .executes(CommandMarker::cmdAddPoint)
                                                )
                                        )
                                )
                )
                .then(
                        literal("add-range")
                                .then(argument("x1", IntegerArgumentType.integer())
                                        .then(argument("y1", IntegerArgumentType.integer())
                                                .then(argument("z1", IntegerArgumentType.integer())
                                                        .then(argument("x2", IntegerArgumentType.integer())
                                                                .then(argument("y2", IntegerArgumentType.integer())
                                                                        .then(argument("z2", IntegerArgumentType.integer())
                                                                                .executes(CommandMarker::cmdAddRange)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                )
                .then(
                        literal("clear")
                                .executes(CommandMarker::cmdClear)
                )
                .then(
                        literal("from-we")
                                .executes(CommandMarker::fromWE)
                )
                .executes(CommandMarker::invalidArgs);


        dispatcher.register(command);
    }

    private static int invalidArgs(final CommandContext<CommandSourceStack> context) {
        Player player = Minecraft.getInstance().player;
        if(player == null){
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "cmd/zcu-marker", "player is null"
            );
            return Command.SINGLE_SUCCESS;
        }
        player.sendSystemMessage(Component.translatable("chat.tip.invalid_args"));
        return Command.SINGLE_SUCCESS;
    }

    private static int cmdAddPoint(final CommandContext<CommandSourceStack> context) {
        Player player = Minecraft.getInstance().player;
        if(player == null){
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "cmd/zcu-marker", "player is null"
            );
            return Command.SINGLE_SUCCESS;
        }
        int x1 = context.getArgument("x1", Integer.class);
        int y1 = context.getArgument("y1", Integer.class);
        int z1 = context.getArgument("z1", Integer.class);
        MarkerMgr mm = MarkerMgr.getInstance();
        if(mm != null) mm.addPointMarker(x1, y1, z1);
        player.sendSystemMessage(Component.translatable("chat.tip.marker_added"));
        return Command.SINGLE_SUCCESS;
    }

    private static int cmdAddRange(final CommandContext<CommandSourceStack> context) {
        Player player = Minecraft.getInstance().player;
        if(player == null){
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "cmd/zcu-marker", "player is null"
            );
            return Command.SINGLE_SUCCESS;
        }
        int x1 = context.getArgument("x1", Integer.class);
        int y1 = context.getArgument("y1", Integer.class);
        int z1 = context.getArgument("z1", Integer.class);
        int x2 = context.getArgument("x2", Integer.class);
        int y2 = context.getArgument("y2", Integer.class);
        int z2 = context.getArgument("z2", Integer.class);
        MarkerMgr mm = MarkerMgr.getInstance();
        if(mm != null) mm.addRangeMarker(x1, y1, z1, x2, y2, z2);
        player.sendSystemMessage(Component.translatable("chat.tip.marker_added"));
        return Command.SINGLE_SUCCESS;
    }

    private static int cmdClear(final CommandContext<CommandSourceStack> context) {
        Player player = Minecraft.getInstance().player;
        if(player == null){
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "cmd/zcu-marker", "player is null"
            );
            return Command.SINGLE_SUCCESS;
        }
        MarkerMgr mm = MarkerMgr.getInstance();
        if(mm != null) mm.clearMarkerList();
        player.sendSystemMessage(Component.translatable("chat.tip.marker_cleared"));
        return Command.SINGLE_SUCCESS;
    }

    private static int fromWE(final CommandContext<CommandSourceStack> context) {
        Player player = Minecraft.getInstance().player;
        if(player == null){
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "cmd/zcu-marker", "player is null"
            );
            return Command.SINGLE_SUCCESS;
        }
        MarkerMgr mm = MarkerMgr.getInstance();
        if(mm == null) {
            player.sendSystemMessage(Component.translatable("chat.tip.marker_from_we_err"));
            return Command.SINGLE_SUCCESS;
        }
        boolean res = mm.addRangeFromWE();
        if (res) {
            player.sendSystemMessage(Component.translatable("chat.tip.marker_from_we_ok"));
        }else {
            player.sendSystemMessage(Component.translatable("chat.tip.marker_from_we_fail"));
        }
        return Command.SINGLE_SUCCESS;
    }
}
