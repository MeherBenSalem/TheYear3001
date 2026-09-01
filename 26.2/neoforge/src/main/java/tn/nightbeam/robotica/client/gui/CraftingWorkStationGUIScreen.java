package tn.nightbeam.robotica.client.gui;

import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;
import tn.nightbeam.robotica.network.CraftingWorkStationGUIButtonMessage;
import tn.nightbeam.robotica.init.ScreenAccessor;
import tn.nightbeam.robotica.neoforge.NeoForgeNetworking;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class CraftingWorkStationGUIScreen extends AbstractContainerScreen<CraftingWorkStationGUIMenu> implements ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	LegacyImageButton imagebutton_forge_button;

	public CraftingWorkStationGUIScreen(CraftingWorkStationGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 166);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	private static final Identifier texture = Identifier.fromNamespaceAndPath("robotica", "textures/screens/crafting_work_station_gui.png");

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("robotica", "textures/screens/crafting_work_station_gui_overlay.png"), this.leftPos, this.topPos - 1, 0, 0, 176, 166, 176, 166);
		graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("robotica", "textures/screens/arrow.png"), this.leftPos + 102, this.topPos + 36, 0, 0, 22, 14, 22, 14);
		super.extractContents(graphics, mouseX, mouseY, partialTicks);
		this.extractTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		imagebutton_forge_button = new LegacyImageButton(this.leftPos + 104, this.topPos + 52, 18, 18, 0, 0, 18,
				Identifier.fromNamespaceAndPath("robotica", "textures/screens/atlas/imagebutton_forge_button.png"), 18, 36, e -> {
					ClientPacketDistributor.sendToServer(new NeoForgeNetworking.CraftingButtonPayload(0, x, y, z));
					CraftingWorkStationGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
				});
		this.addRenderableWidget(imagebutton_forge_button);
	}
}
