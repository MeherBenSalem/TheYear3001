package tn.nightbeam.chronofall.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.block.FabricatorBlockEntity;
import tn.nightbeam.chronofall.core.FabricatorRecipes;
import tn.nightbeam.chronofall.menu.FabricatorMenu;

/**
 * Recipe grid on the left, the selected recipe on the right with "have / need" counts in green or red, the Fabricate
 * button, the print progress and the output slot.
 */
public class FabricatorScreen extends AbstractContainerScreen<FabricatorMenu> {
    private static final Identifier TEXTURE = Chronofall.id("textures/gui/fabricator.png");
    private static final int GRID_X = 10;
    private static final int GRID_Y = 20;
    private static final int CELL = 20;
    private static final int COLUMNS = 5;
    private static final int BUTTON_X = 124;
    private static final int BUTTON_Y = 96;
    private static final int BUTTON_W = 84;
    private static final int BUTTON_H = 18;
    private final List<ItemStack> icons = new ArrayList<>();

    public FabricatorScreen(FabricatorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, FabricatorMenu.WIDTH, FabricatorMenu.HEIGHT);
        inventoryLabelX = FabricatorMenu.INVENTORY_X;
        inventoryLabelY = FabricatorMenu.INVENTORY_Y - 11;
        for (FabricatorRecipes.Recipe recipe : FabricatorRecipes.ALL) {
            icons.add(FabricatorBlockEntity.result(recipe));
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(g, mouseX, mouseY, partialTick);
        int hovered = recipeAt(mouseX, mouseY);
        if (hovered >= 0) {
            g.setTooltipForNextFrame(font, icons.get(hovered), mouseX, mouseY);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(g, mouseX, mouseY, partialTick);
        g.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        int selected = menu.selected();
        Map<String, Integer> have = FabricatorBlockEntity.counts(minecraft.player.getInventory());
        for (int i = 0; i < icons.size(); i++) {
            int x = leftPos + GRID_X + (i % COLUMNS) * CELL;
            int y = topPos + GRID_Y + (i / COLUMNS) * CELL;
            boolean affordable = minecraft.player.getAbilities().instabuild || FabricatorRecipes.canAfford(FabricatorRecipes.ALL.get(i), have);
            g.fill(x, y, x + 18, y + 18, i == selected ? 0xFF1B6E7A : affordable ? 0xFF1A2333 : 0xFF141821);
            if (i == selected) {
                g.outline(x - 1, y - 1, 20, 20, 0xFF38F3FF);
            } else if (recipeAt(mouseX, mouseY) == i) {
                g.outline(x - 1, y - 1, 20, 20, 0xFF6A7FA0);
            }
            g.item(icons.get(i), x + 1, y + 1);
            if (!affordable) {
                g.fill(x, y, x + 18, y + 18, 0x88000000);
            }
        }
        FabricatorRecipes.Recipe recipe = FabricatorRecipes.get(selected);
        if (recipe == null) {
            return;
        }
        int px = leftPos + 122;
        int py = topPos + 20;
        g.text(font, icons.get(selected).getHoverName(), px, py, 0xFF38F3FF, false);
        int row = 0;
        for (Map.Entry<String, Integer> need : recipe.totals().entrySet()) {
            ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(need.getKey())));
            int count = have.getOrDefault(need.getKey(), 0);
            int y = py + 12 + row * 13;
            g.pose().pushMatrix();
            g.pose().translate(px, y - 2);
            g.pose().scale(0.75F, 0.75F);
            g.item(stack, 0, 0);
            g.pose().popMatrix();
            String name = stack.getHoverName().getString();
            if (font.width(name) > 74) {
                name = font.plainSubstrByWidth(name, 70) + "…";
            }
            g.text(font, name, px + 15, y, 0xFFC8D2E0, false);
            String amount = Math.min(count, 999) + "/" + need.getValue();
            g.text(font, amount, px + 126 - font.width(amount), y, count >= need.getValue() ? 0xFF7CFFB0 : 0xFFFF6A7A, false);
            row++;
        }
        boolean busy = menu.printing() >= 0;
        boolean affordable = minecraft.player.getAbilities().instabuild || FabricatorRecipes.canAfford(recipe, have);
        boolean hover = inside(mouseX, mouseY, BUTTON_X, BUTTON_Y, BUTTON_W, BUTTON_H);
        int bx = leftPos + BUTTON_X;
        int by = topPos + BUTTON_Y;
        int color = busy || !affordable ? 0xFF2A3140 : hover ? 0xFF2AB8C8 : 0xFF1B8C9A;
        g.fill(bx, by, bx + BUTTON_W, by + BUTTON_H, color);
        g.outline(bx, by, BUTTON_W, BUTTON_H, busy || !affordable ? 0xFF3C4658 : 0xFF38F3FF);
        Component label = Component.translatable(busy ? "gui.chronofall.fabricator.printing" : "gui.chronofall.fabricator.fabricate");
        g.text(font, label, bx + (BUTTON_W - font.width(label)) / 2, by + 5, busy || !affordable ? 0xFF8090A0 : 0xFFFFFFFF, false);
        FabricatorRecipes.Recipe printing = FabricatorRecipes.get(menu.printing());
        int barX = leftPos + 122;
        int barY = topPos + 120;
        g.fill(barX, barY, barX + 120, barY + 4, 0xFF10151E);
        if (printing != null) {
            int w = Math.min(120, menu.progress() * 120 / printing.ticks());
            g.fillGradient(barX, barY, barX + w, barY + 4, 0xFF38F3FF, 0xFF1B8C9A);
            if (menu.blocked()) {
                g.text(font, Component.translatable("gui.chronofall.fabricator.full"), barX, barY + 6, 0xFFFF6A7A, false);
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        int recipe = recipeAt(mouseX, mouseY);
        if (recipe >= 0) {
            click(recipe);
            return true;
        }
        if (inside(mouseX, mouseY, BUTTON_X, BUTTON_Y, BUTTON_W, BUTTON_H) && menu.printing() < 0) {
            click(FabricatorMenu.FABRICATE);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void click(int id) {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, id);
    }

    private int recipeAt(double mouseX, double mouseY) {
        double x = mouseX - leftPos - GRID_X;
        double y = mouseY - topPos - GRID_Y;
        if (x < 0 || y < 0) {
            return -1;
        }
        int col = (int) (x / CELL);
        int row = (int) (y / CELL);
        int index = row * COLUMNS + col;
        return col < COLUMNS && x % CELL < 18 && y % CELL < 18 && index < icons.size() ? index : -1;
    }

    private boolean inside(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= leftPos + x && mouseX < leftPos + x + w && mouseY >= topPos + y && mouseY < topPos + y + h;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor g, int mouseX, int mouseY) {
        g.text(font, title, 10, 7, 0xFF38F3FF, false);
        g.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xFF8FA3BF, false);
    }
}
