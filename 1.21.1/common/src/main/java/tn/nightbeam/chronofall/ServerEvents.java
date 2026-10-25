package tn.nightbeam.chronofall;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tn.nightbeam.chronofall.entity.MechEntity;
import tn.nightbeam.chronofall.item.Jetpack;
import tn.nightbeam.chronofall.network.Control;
import tn.nightbeam.chronofall.rift.RiftEvents;

/** Server-side hooks the loaders call: the end of every server tick, command registration, logouts. */
public final class ServerEvents {
    private ServerEvents() {
    }

    public static void serverTick(MinecraftServer server) {
        RiftEvents.tick(server);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Jetpack.serverTick(player);
        }
    }

    public static void logout(ServerPlayer player) {
        Control.forget(player);
    }

    /** Whether {@code player} attacking {@code target} is allowed (riders cannot hit their own mech). */
    public static boolean allowAttack(Player player, Entity target) {
        return !(target instanceof MechEntity && target.hasPassenger(player));
    }

    /**
     * {@code /chronofall rift [player]} opens a rift near a player; {@code /chronofall rift_at <pos>} opens one above
     * a position (operators only).
     */
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chronofall")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("rift")
                        .executes(ctx -> rift(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> rift(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))))
                .then(Commands.literal("rift_at")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(ctx -> {
                                    BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
                                    RiftEvents.open(ctx.getSource().getLevel(), pos);
                                    ctx.getSource().sendSuccess(() -> Component.translatable("commands.chronofall.rift",
                                            pos.getX(), pos.getY(), pos.getZ()), true);
                                    return 1;
                                })))
                .then(Commands.literal("last_landing")
                        .executes(ctx -> {
                            BlockPos pos = RiftEvents.lastLanding();
                            ctx.getSource().sendSuccess(() -> pos == null ? Component.translatable("commands.chronofall.no_landing")
                                    : Component.translatable("commands.chronofall.last_landing", pos.getX(), pos.getY(), pos.getZ()), false);
                            return pos == null ? 0 : 1;
                        })));
    }

    private static int rift(CommandSourceStack source, ServerPlayer player) {
        BlockPos pos = RiftEvents.openNear(player.serverLevel(), player);
        source.sendSuccess(() -> Component.translatable("commands.chronofall.rift", pos.getX(), pos.getY(), pos.getZ()), true);
        return 1;
    }
}
