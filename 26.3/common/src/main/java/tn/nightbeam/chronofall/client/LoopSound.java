package tn.nightbeam.chronofall.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import tn.nightbeam.chronofall.entity.SkiffEntity;

/** A looping sound that follows an entity and stops a few ticks after the game stops asking for it. */
public class LoopSound extends AbstractTickableSoundInstance {
    private final Entity entity;
    private final String kind;
    private int sinceRefresh;

    public LoopSound(SoundEvent sound, Entity entity, String kind) {
        super(sound, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.kind = kind;
        looping = true;
        delay = 0;
        volume = 0.6F;
        x = entity.getX();
        y = entity.getY();
        z = entity.getZ();
    }

    public void refresh() {
        sinceRefresh = 0;
    }

    public String kind() {
        return kind;
    }

    @Override
    public void tick() {
        if (entity.isRemoved() || ++sinceRefresh > 4) {
            stop();
            return;
        }
        x = entity.getX();
        y = entity.getY();
        z = entity.getZ();
        if (entity instanceof SkiffEntity skiff) {
            float speed = (float) Math.min(1.0, Math.abs(skiff.speed()) / SkiffEntity.MAX_SPEED);
            pitch = 0.75F + speed * 0.6F;
            volume = 0.45F + speed * 0.4F;
        }
    }

    public boolean finished() {
        return isStopped();
    }
}
