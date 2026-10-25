package tn.nightbeam.chronofall.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.entity.MechEntity;
import tn.nightbeam.chronofall.entity.SkiffEntity;
import tn.nightbeam.chronofall.item.Jetpack;
import tn.nightbeam.chronofall.item.JetpackItem;

/** The flux gauge at the bottom left while flying a jetpack or riding a mech or skiff (speed and altitude in a skiff). */
public final class FluxHud {
    private FluxHud() {
    }

    public static void render(GuiGraphicsExtractor g) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }
        Entity vehicle = player.getVehicle();
        String label;
        int stored;
        int capacity;
        String extra = null;
        if (vehicle instanceof SkiffEntity skiff) {
            label = "hud.chronofall.skiff";
            stored = skiff.flux();
            capacity = skiff.fluxCapacity();
            extra = Component.translatable("hud.chronofall.skiff_stats", Math.round(Math.abs(skiff.speed()) * 20), (int) Math.floor(skiff.getY())).getString();
        } else if (vehicle instanceof MechEntity mech) {
            label = "hud.chronofall.mech";
            stored = mech.flux();
            capacity = mech.fluxCapacity();
            extra = Component.translatable("hud.chronofall.mech_stats", (int) Math.ceil(mech.getHealth()), (int) mech.getMaxHealth()).getString();
        } else {
            ItemStack pack = Jetpack.worn(player);
            if (pack.isEmpty() || player.onGround()) {
                return;
            }
            label = "hud.chronofall.jetpack";
            stored = ((JetpackItem) pack.getItem()).flux(pack);
            capacity = Flux.JETPACK_CAPACITY;
        }
        int x = 8;
        int y = g.guiHeight() - (extra == null ? 30 : 40);
        int w = 96;
        g.fill(x - 3, y - 3, x + w + 3, y + (extra == null ? 19 : 29), 0xAA0B0F1E);
        g.outline(x - 3, y - 3, w + 6, extra == null ? 22 : 32, 0xFF1B8C9A);
        g.text(mc.font, Component.translatable(label), x, y, 0xFF38F3FF, false);
        String pct = Flux.percent(stored, capacity) + "%";
        g.text(mc.font, pct, x + w - mc.font.width(pct), y, 0xFFFFFFFF, false);
        g.fill(x, y + 11, x + w, y + 16, 0xFF141A28);
        int fill = capacity <= 0 ? 0 : (int) ((long) stored * w / capacity);
        g.fill(x, y + 11, x + fill, y + 16, 0xFF000000 | Flux.barColor(stored, capacity));
        if (extra != null) {
            g.text(mc.font, extra, x, y + 20, 0xFFC8D2E0, false);
        }
    }
}
