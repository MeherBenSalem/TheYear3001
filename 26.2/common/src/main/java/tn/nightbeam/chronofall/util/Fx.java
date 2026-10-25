package tn.nightbeam.chronofall.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Particle shapes: beams between two points, rings, spirals. Client-side variants add particles directly. */
public final class Fx {
    private Fx() {
    }

    /** Client side: particles every {@code spacing} blocks from {@code from} to {@code to}. */
    public static void beam(Level level, ParticleOptions particle, Vec3 from, Vec3 to, double spacing) {
        Vec3 delta = to.subtract(from);
        int steps = Math.max(1, (int) (delta.length() / spacing));
        double jitter = level.getRandom().nextDouble();
        for (int i = 0; i <= steps; i++) {
            Vec3 p = from.add(delta.scale((i + jitter * 0.5) / steps));
            level.addParticle(particle, p.x, p.y, p.z, 0, 0, 0);
        }
    }

    /** Server side: a beam every nearby player sees (up to {@code range} blocks away, past the usual 32). */
    public static void beam(ServerLevel level, ParticleOptions particle, Vec3 from, Vec3 to, double spacing, double range) {
        Vec3 delta = to.subtract(from);
        int steps = Math.max(1, (int) (delta.length() / spacing));
        for (int i = 0; i <= steps; i++) {
            Vec3 p = from.add(delta.scale((double) i / steps));
            send(level, particle, p.x, p.y, p.z, 1, 0, 0, 0, 0, range);
        }
    }

    /** Server side: a horizontal ring of {@code count} particles. */
    public static void ring(ServerLevel level, ParticleOptions particle, Vec3 centre, double radius, int count, double phase, double range) {
        for (int i = 0; i < count; i++) {
            double a = phase + Math.PI * 2 * i / count;
            send(level, particle, centre.x + Math.cos(a) * radius, centre.y, centre.z + Math.sin(a) * radius, 1, 0, 0, 0, 0, range);
        }
    }

    /** Sends particles to every player in the level within {@code range} blocks, with long-distance rendering. */
    public static void send(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double dx, double dy, double dz,
                            double speed, double range) {
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(x, y, z) <= range * range) {
                level.sendParticles(player, particle, true, true, x, y, z, count, dx, dy, dz, speed);
            }
        }
    }
}
