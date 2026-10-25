package tn.nightbeam.chronofall.block;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.ChronoConfig;

/**
 * Keeps the partner gate's position and teleports players who stand on the gate for {@code charge_ticks}. The
 * partner may be in another dimension; if it is gone the link is cleared.
 */
public class RiftGateBlockEntity extends BlockEntity {
    /** Game time before which a player may not jump again, shared by all gates. */
    private static final Map<UUID, Long> COOLDOWN = new HashMap<>();

    @Nullable
    private ResourceKey<Level> targetDimension;
    @Nullable
    private BlockPos target;
    private final Map<UUID, Integer> charging = new HashMap<>();

    public RiftGateBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.RIFT_GATE_ENTITY.get(), pos, state);
    }

    public boolean linked() {
        return target != null && targetDimension != null;
    }

    public void link(ResourceKey<Level> dimension, BlockPos pos) {
        targetDimension = dimension;
        target = pos.immutable();
        setLinkedState(true);
    }

    public void unlink() {
        targetDimension = null;
        target = null;
        setLinkedState(false);
    }

    private void setLinkedState(boolean on) {
        setChanged();
        if (level != null && getBlockState().getValue(RiftGateBlock.LINKED) != on) {
            level.setBlock(worldPosition, getBlockState().setValue(RiftGateBlock.LINKED, on), 3);
        }
    }

    public Component describe() {
        if (!linked()) {
            return Component.translatable("message.chronofall.gate.unlinked");
        }
        return Component.translatable("message.chronofall.gate.linked", target.getX(), target.getY(), target.getZ(),
                targetDimension.identifier().getPath());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RiftGateBlockEntity gate) {
        if (!(level instanceof ServerLevel server)) {
            return;
        }
        List<ServerPlayer> standing = server.getEntitiesOfClass(ServerPlayer.class,
                new AABB(pos.getX(), pos.getY() + 0.1, pos.getZ(), pos.getX() + 1, pos.getY() + 1.0, pos.getZ() + 1), p -> !p.isSpectator() && !p.isPassenger());
        Iterator<UUID> it = gate.charging.keySet().iterator();
        while (it.hasNext()) {
            UUID id = it.next();
            if (standing.stream().noneMatch(p -> p.getUUID().equals(id))) {
                it.remove();
            }
        }
        if (!gate.linked()) {
            return;
        }
        ChronoConfig.Gates cfg = ChronoConfig.get().gates();
        long now = server.getGameTime();
        for (ServerPlayer player : standing) {
            if (COOLDOWN.getOrDefault(player.getUUID(), 0L) > now) {
                continue;
            }
            int ticks = gate.charging.merge(player.getUUID(), 1, Integer::sum);
            if (ticks == 1) {
                server.playSound(null, pos, ModContent.sound("gate_charge"), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            double frac = (double) ticks / cfg.chargeTicks();
            for (int i = 0; i < 3; i++) {
                double a = ticks * 0.5 + i * Math.PI * 2 / 3;
                double r = 0.8 - 0.4 * frac;
                server.sendParticles(ModContent.RIFT_PARTICLE.get(), player.getX() + Math.cos(a) * r, pos.getY() + 0.3 + frac * 2.0 * ((i + 1) / 3.0),
                        player.getZ() + Math.sin(a) * r, 1, 0, 0, 0, 0);
            }
            if (ticks >= cfg.chargeTicks()) {
                gate.charging.remove(player.getUUID());
                gate.warp(server, player, now + cfg.cooldownTicks());
            }
        }
    }

    private void warp(ServerLevel here, ServerPlayer player, long cooldownUntil) {
        ServerLevel there = here.getServer().getLevel(targetDimension);
        if (there == null || !(there.getBlockEntity(target) instanceof RiftGateBlockEntity partner)) {
            unlink();
            player.sendOverlayMessage(Component.translatable("message.chronofall.gate.lost"));
            return;
        }
        if (!partner.linked()) {
            partner.link(here.dimension(), worldPosition);
        }
        COOLDOWN.put(player.getUUID(), cooldownUntil);
        here.playSound(null, worldPosition, ModContent.sound("gate_warp"), SoundSource.BLOCKS, 1.0F, 1.0F);
        here.sendParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY() + 1, player.getZ(), 40, 0.3, 0.8, 0.3, 0.1);
        double x = target.getX() + 0.5;
        double y = target.getY() + 0.3;
        double z = target.getZ() + 0.5;
        if (there == here) {
            player.teleportTo(x, y, z);
        } else {
            player.teleportTo(there, x, y, z, java.util.Set.of(), player.getYRot(), player.getXRot(), true);
        }
        player.resetFallDistance();
        there.playSound(null, target, ModContent.sound("gate_warp"), SoundSource.BLOCKS, 1.0F, 1.2F);
        there.sendParticles(ModContent.RIFT_PARTICLE.get(), x, y + 1, z, 40, 0.3, 0.8, 0.3, 0.1);
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        if (tag.getString("TargetDim").isPresent()) {
            targetDimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse(tag.getStringOr("TargetDim", "")));
            target = new BlockPos(tag.getIntOr("TargetX", 0), tag.getIntOr("TargetY", 0), tag.getIntOr("TargetZ", 0));
        } else {
            targetDimension = null;
            target = null;
        }
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        if (linked()) {
            tag.putString("TargetDim", targetDimension.identifier().toString());
            tag.putInt("TargetX", target.getX());
            tag.putInt("TargetY", target.getY());
            tag.putInt("TargetZ", target.getZ());
        }
    }
}
