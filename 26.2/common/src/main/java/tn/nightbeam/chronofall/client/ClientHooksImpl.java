package tn.nightbeam.chronofall.client;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import tn.nightbeam.chronofall.ClientHooks;
import tn.nightbeam.chronofall.content.ModContent;

public final class ClientHooksImpl implements ClientHooks {
    private final Map<String, LoopSound> loops = new HashMap<>();

    @Override
    public boolean jumpHeld() {
        return Minecraft.getInstance().options.keyJump.isDown();
    }

    @Override
    public boolean descendHeld() {
        return Minecraft.getInstance().options.keySprint.isDown();
    }

    @Override
    public boolean isLocalPlayer(Entity entity) {
        return entity == Minecraft.getInstance().player;
    }

    @Override
    public void loopSound(Entity entity, String kind) {
        String key = entity.getId() + ":" + kind;
        LoopSound sound = loops.get(key);
        if (sound != null && !sound.finished()) {
            sound.refresh();
            return;
        }
        sound = new LoopSound(ModContent.sound(kind + "_loop"), entity, kind);
        loops.put(key, sound);
        Minecraft.getInstance().getSoundManager().play(sound);
        loops.values().removeIf(LoopSound::finished);
    }
}
