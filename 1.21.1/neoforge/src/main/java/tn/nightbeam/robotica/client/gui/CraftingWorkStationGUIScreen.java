package tn.nightbeam.robotica.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import tn.nightbeam.robotica.init.ScreenAccessor;
import tn.nightbeam.robotica.neoforge.NeoForgeNetworking;
import tn.nightbeam.robotica.network.CraftingWorkStationGUIButtonMessage;
import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;

public class CraftingWorkStationGUIScreen extends AbstractContainerScreen<CraftingWorkStationGUIMenu> implements ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;

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

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("robotica", "textures/screens/crafting_work_station_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("robotica", "textures/screens/crafting_work_station_gui_overlay.png"), this.leftPos + 0, this.topPos + -1, 0, 0, 176, 166, 176, 166);
		guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("robotica", "textures/screens/arrow.png"), this.leftPos + 102, this.topPos + 36, 0, 0, 22, 14, 22, 14);
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
		this.addRenderableWidget(Button.builder(Component.empty(), e -> {
			int x = CraftingWorkStationGUIScreen.this.x;
			int y = CraftingWorkStationGUIScreen.this.y;
			int z = CraftingWorkStationGUIScreen.this.z;
			PacketDistributor.sendToServer(new NeoForgeNetworking.CraftingButtonPayload(0, x, y, z));
			CraftingWorkStationGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
		}).bounds(this.leftPos + 104, this.topPos + 52, 18, 18).build());
	}
}
