package tn.nightbeam.chronofall.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.network.Control;

/**
 * Jetpack flight. The client moves the player (player movement is client-side in Minecraft) and tells the server
 * whether it is thrusting; the server pays the Flux, cancels fall damage while the jetpack has charge, plays the
 * thrust for everyone else and keeps the "flying is not enabled" check from kicking the player.
 */
public final class Jetpack {
    public static final double MAX_RISE = 0.5;
    public static final double LIFT = 0.13;
    public static final double PUSH = 0.045;
    public static final double MAX_HORIZONTAL = 0.75;

    private static int lastSent;
    private static int sinceSent;

    private Jetpack() {
    }

    public static ItemStack worn(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        return chest.getItem() instanceof JetpackItem ? chest : ItemStack.EMPTY;
    }

    /** Client tick for the local player. */
    public static void clientTick(Player player) {
        ItemStack pack = worn(player);
        boolean thrust = !pack.isEmpty() && ((JetpackItem) pack.getItem()).flux(pack) >= Flux.JETPACK_PER_TICK
                && Chronofall.client().jumpHeld() && !player.getAbilities().flying && !player.isPassenger() && !player.isSpectator()
                && !player.isFallFlying();
        if (thrust) {
            Vec3 v = player.getDeltaMovement();
            double yaw = player.getYRot() * Mth.DEG_TO_RAD;
            double forward = player.zza;
            double strafe = player.xxa;
            double vx = v.x + (-Math.sin(yaw) * forward + Math.cos(yaw) * strafe) * PUSH;
            double vz = v.z + (Math.cos(yaw) * forward + Math.sin(yaw) * strafe) * PUSH;
            double horizontal = Math.sqrt(vx * vx + vz * vz);
            if (horizontal > MAX_HORIZONTAL) {
                vx *= MAX_HORIZONTAL / horizontal;
                vz *= MAX_HORIZONTAL / horizontal;
            }
            player.setDeltaMovement(vx, Math.min(MAX_RISE, v.y + LIFT), vz);
            player.resetFallDistance();
            flames(player);
            Chronofall.client().loopSound(player, "jetpack");
        }
        int flags = thrust ? Control.THRUST : 0;
        sinceSent++;
        if (flags != lastSent || (flags != 0 && sinceSent >= Control.RESEND)) {
            Chronofall.platform().sendControl(flags);
            lastSent = flags;
            sinceSent = 0;
        }
    }

    /** Thruster flames from both nozzles (client side). */
    public static void flames(Player player) {
        float yaw = player.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 back = new Vec3(Math.sin(yaw), 0, -Math.cos(yaw)).scale(0.32);
        Vec3 side = new Vec3(Math.cos(yaw), 0, Math.sin(yaw)).scale(0.16);
        for (int s = -1; s <= 1; s += 2) {
            Vec3 p = player.position().add(back).add(side.scale(s)).add(0, 0.62, 0);
            for (int i = 0; i < 2; i++) {
                player.level().addParticle(ModContent.THRUSTER_PARTICLE.get(), p.x + player.getRandom().triangle(0, 0.04), p.y,
                        p.z + player.getRandom().triangle(0, 0.04), 0, -0.25 - player.getRandom().nextDouble() * 0.1, 0);
            }
        }
    }

    /** Server tick for every player. */
    public static void serverTick(ServerPlayer player) {
        ItemStack pack = worn(player);
        if (pack.isEmpty()) {
            return;
        }
        JetpackItem item = (JetpackItem) pack.getItem();
        boolean creative = player.getAbilities().instabuild;
        if (Control.has(player, Control.THRUST) && (creative || item.drain(pack, Flux.JETPACK_PER_TICK))) {
            player.resetFallDistance();
            Chronofall.platform().resetFloating(player);
            if (player.tickCount % 8 == 0) {
                player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), ModContent.sound("jetpack_loop"),
                        SoundSource.PLAYERS, 0.5F, 1.0F);
            }
            float yaw = player.yBodyRot * Mth.DEG_TO_RAD;
            Vec3 at = player.position().add(new Vec3(Math.sin(yaw), 0, -Math.cos(yaw)).scale(0.32)).add(0, 0.55, 0);
            for (ServerPlayer other : player.serverLevel().players()) {
                if (other != player && other.distanceToSqr(player) < 64 * 64) {
                    player.serverLevel().sendParticles(other, ModContent.THRUSTER_PARTICLE.get(), true, at.x, at.y, at.z, 3, 0.1, 0.05, 0.1, 0.02);
                }
            }
        } else if (player.fallDistance > 0 && item.flux(pack) > 0) {
            if (!creative && player.tickCount % 2 == 0) {
                item.drain(pack, 1);
            }
            player.resetFallDistance();
        }
    }
}
