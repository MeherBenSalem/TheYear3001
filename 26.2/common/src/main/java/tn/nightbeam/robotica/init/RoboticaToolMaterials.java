package tn.nightbeam.robotica.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import tn.nightbeam.robotica.RoboticaMod;

public final class RoboticaToolMaterials {
	public static final TagKey<Item> REPAIR_SCRAP = itemTag("repair_scrap");
	public static final TagKey<Item> REPAIR_TITANIUM = itemTag("repair_titanium");
	public static final TagKey<Item> REPAIR_COBALT = itemTag("repair_cobalt");
	public static final TagKey<Item> REPAIR_ORICHALCUM = itemTag("repair_orichalcum");

	public static final ToolMaterial SCRAP = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 256, 4f, 0f, 15, REPAIR_SCRAP);
	public static final ToolMaterial TITANIUM = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2500, 10f, 6f, 18, REPAIR_TITANIUM);
	public static final ToolMaterial COBALT = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2800, 12f, 7f, 20, REPAIR_COBALT);
	public static final ToolMaterial ORICHALCUM = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 3500, 13f, 8f, 25, REPAIR_ORICHALCUM);

	private RoboticaToolMaterials() {
	}

	private static TagKey<Item> itemTag(String path) {
		return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, path));
	}
}
