package tn.nightbeam.chronofall.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.menu.FluxChargerMenu;

/** Fuel on the left, the flux tank in the middle, the device being charged on the right. */
public class FluxChargerScreen extends AbstractContainerScreen<FluxChargerMenu> {
    private static final ResourceLocation TEXTURE = Chronofall.id("textures/gui/flux_charger.png");
    private static final int TANK_X = 70;
    private static final int TANK_Y = 17;
    private static final int TANK_W = 36;
    private static final int TANK_H = 52;

    public FluxChargerScreen(FluxChargerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
        if (mouseX >= leftPos + TANK_X && mouseX < leftPos + TANK_X + TANK_W && mouseY >= topPos + TANK_Y && mouseY < topPos + TANK_Y + TANK_H) {
            g.renderTooltip(font, Component.translatable("tooltip.chronofall.flux", Flux.format(menu.stored(), Flux.CHARGER_CAPACITY)), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        g.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int x = leftPos + TANK_X;
        int y = topPos + TANK_Y;
        int fill = menu.stored() * (TANK_H - 2) / Flux.CHARGER_CAPACITY;
        if (fill > 0) {
            g.fillGradient(x + 1, y + TANK_H - 1 - fill, x + TANK_W - 1, y + TANK_H - 1, 0xFF7FF8FF, 0xFF1B8C9A);
            long t = minecraft.level == null ? 0 : minecraft.level.getGameTime();
            for (int i = 0; i < 3; i++) {
                int ly = y + TANK_H - 1 - (int) ((t * 2 + i * 17) % Math.max(1, fill));
                g.fill(x + 3 + i * 11, ly, x + 6 + i * 11, ly + 1, 0xCCFFFFFF);
            }
        }
        int burn = menu.burn();
        if (burn > 0) {
            int h = Math.max(1, burn * 13 / 100);
            g.fillGradient(leftPos + FluxChargerMenu.FUEL_X + 2, topPos + 70 - h, leftPos + FluxChargerMenu.FUEL_X + 14, topPos + 70, 0xFFFFE08A, 0xFFFF7A2A);
        }
        if (menu.charging()) {
            int pulse = (int) ((minecraft.level.getGameTime() / 2) % 12);
            g.fill(leftPos + 108, topPos + 42, leftPos + 108 + pulse, topPos + 44, 0xFF38F3FF);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, (imageWidth - font.width(title)) / 2, 6, 0xFF38F3FF, false);
        g.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xFF8FA3BF, false);
    }
}
