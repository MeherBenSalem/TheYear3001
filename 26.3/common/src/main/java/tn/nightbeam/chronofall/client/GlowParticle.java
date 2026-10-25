package tn.nightbeam.chronofall.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.RandomSource;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * The mod's particles: full-bright glowing sprites that fade out. Flux (cyan sparks), rift (magenta motes), laser
 * (red sparks) and thruster (a flame that cools from white through amber to smoke-dark).
 */
public class GlowParticle extends SingleQuadParticle {
    public enum Kind {
        FLUX, RIFT, LASER, THRUSTER
    }

    private final Kind kind;
    private final float startSize;

    protected GlowParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites, Kind kind) {
        super(level, x, y, z, vx, vy, vz, sprites.first());
        this.kind = kind;
        xd = vx;
        yd = vy;
        zd = vz;
        hasPhysics = false;
        gravity = 0;
        setSprite(sprites.get(random));
        switch (kind) {
            case FLUX -> {
                lifetime = 10 + random.nextInt(10);
                quadSize = 0.07F + random.nextFloat() * 0.05F;
                friction = 0.9F;
            }
            case RIFT -> {
                lifetime = 18 + random.nextInt(14);
                quadSize = 0.1F + random.nextFloat() * 0.08F;
                friction = 0.92F;
            }
            case LASER -> {
                lifetime = 5 + random.nextInt(4);
                quadSize = 0.1F + random.nextFloat() * 0.04F;
                friction = 0.85F;
            }
            case THRUSTER -> {
                lifetime = 7 + random.nextInt(6);
                quadSize = 0.12F + random.nextFloat() * 0.08F;
                friction = 0.88F;
                hasPhysics = true;
            }
        }
        startSize = quadSize;
    }

    @Override
    public void tick() {
        super.tick();
        float life = (float) age / lifetime;
        alpha = 1.0F - life * life;
        if (kind == Kind.THRUSTER) {
            quadSize = startSize * (1.0F + life * 1.5F);
            rCol = 1.0F - life * 0.7F;
            gCol = Math.max(0.15F, 0.95F - life * 1.2F);
            bCol = Math.max(0.1F, 0.7F - life * 1.6F);
        } else {
            quadSize = startSize * (1.0F - life * 0.6F);
        }
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    @Override
    public int getLightCoords(float partialTick) {
        return 0xF000F0;
    }

    public record Provider(SpriteSet sprites, Kind kind) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz,
                                       RandomSource random) {
            return new GlowParticle(level, x, y, z, vx, vy, vz, sprites, kind);
        }
    }
}
