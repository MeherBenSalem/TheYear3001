package tn.nightbeam.robotica.client.gui;

import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;
import tn.nightbeam.robotica.network.CraftingWorkStationGUIButtonMessage;
import tn.nightbeam.robotica.RoboticaMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class CraftingWorkStationGUIScreen extends AbstractContainerScreen<CraftingWorkStationGUIMenu> {
	private final static HashMap<String, Object> guistate = CraftingWorkStationGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_forge_button;

	public CraftingWorkStationGUIScreen(CraftingWorkStationGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("robotica:textures/screens/crafting_work_station_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		guiGraphics.blit(new ResourceLocation("robotica:textures/screens/crafting_work_station_gui_overlay.png"), this.leftPos + 0, this.topPos + -1, 0, 0, 176, 166, 176, 166);

		guiGraphics.blit(new ResourceLocation("robotica:textures/screens/arrow.png"), this.leftPos + 102, this.topPos + 36, 0, 0, 22, 14, 22, 14);

		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		imagebutton_forge_button = new ImageButton(this.leftPos + 104, this.topPos + 52, 18, 18, 0, 0, 18, new ResourceLocation("robotica:textures/screens/atlas/imagebutton_forge_button.png"), 18, 36, e -> {
			if (true) {
				RoboticaMod.PACKET_HANDLER.sendToServer(new CraftingWorkStationGUIButtonMessage(0, x, y, z));
				CraftingWorkStationGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:imagebutton_forge_button", imagebutton_forge_button);
		this.addRenderableWidget(imagebutton_forge_button);
	}
}
