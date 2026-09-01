import os
import re

base = r"C:\Users\mahou\OneDrive\Documents\GitHub\TheYear3001\1.20.1"
forge_items = open(os.path.join(base, "forge/src/main/java/tn/nightbeam/robotica/init/RoboticaModItemsForge.java"), encoding="utf-8").read()
forge_blocks = open(os.path.join(base, "forge/src/main/java/tn/nightbeam/robotica/init/RoboticaModBlocksForge.java"), encoding="utf-8").read()

block_class = {
    "SCRAP_BLOCK": "ScrapBlockBlock",
    "CRAFTING_WORKSTATION": "CraftingWorkstationBlock",
    "SCRAP_SLAB": "ScrapSlabBlock",
    "SCRAP_STAIRS_BLOCK": "ScrapStairsBlockBlock",
    "SCRAP_DEBRIS": "ScrapDebrisBlock",
    "TITANIUM_ORE": "TitaniumOreBlock",
    "COBALT_ORE": "CobaltOreBlock",
    "ORICHALCUM_ORE": "OrichalcumOreBlock",
}

block_paths = {}
for m in re.finditer(r'public static final RegistryObject<Block> (\w+) = REGISTRY\.register\("([^"]+)"', forge_blocks):
    block_paths[m.group(1)] = m.group(2)

item_regs = []
for m in re.finditer(r'public static final RegistryObject<Item> (\w+) = REGISTRY\.register\("([^"]+)", \(\) -> ([^;]+)\);', forge_items):
    field = m.group(1)
    if field in ("OIL_BUCKET", "SENTINEL_SPAWN_EGG"):
        continue
    item_regs.append((field, m.group(2), m.group(3)))
for m in re.finditer(r'public static final RegistryObject<Item> (\w+) = block\(RoboticaModBlocksForge\.(\w+)\);', forge_items):
    bp = block_paths.get(m.group(2), m.group(2).lower())
    item_regs.append((m.group(1), bp, f"new BlockItem(RoboticaModBlocks.{m.group(2)}.get(), new Item.Properties())"))

lines = []
lines.append("package tn.nightbeam.robotica;")
lines.append("")
lines.append("import net.fabricmc.api.ModInitializer;")
lines.append("import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;")
lines.append("import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;")
lines.append("import net.minecraft.core.Registry;")
lines.append("import net.minecraft.core.registries.BuiltInRegistries;")
lines.append("import net.minecraft.resources.ResourceLocation;")
lines.append("import net.minecraft.world.entity.EntityDimensions;")
lines.append("import net.minecraft.world.entity.EntityType;")
lines.append("import net.minecraft.world.entity.MobCategory;")
lines.append("import net.minecraft.world.item.BlockItem;")
lines.append("import net.minecraft.world.item.Item;")
lines.append("import net.minecraft.world.item.Items;")
lines.append("import net.minecraft.world.item.Rarity;")
lines.append("import net.minecraft.world.item.SpawnEggItem;")
lines.append("import net.minecraft.world.level.block.Block;")
lines.append("import net.minecraft.world.level.block.Blocks;")
lines.append("import net.minecraft.world.level.material.FlowingFluid;")
lines.append("import net.minecraft.world.level.material.Fluids;")
lines.append("import tn.nightbeam.robotica.block.*;")
lines.append("import tn.nightbeam.robotica.entity.*;")
lines.append("import tn.nightbeam.robotica.events.FabricRoboticaModEvents;")
lines.append("import tn.nightbeam.robotica.init.*;")
lines.append("import tn.nightbeam.robotica.item.*;")
lines.append("")
lines.append("public class RoboticaModFabric implements ModInitializer {")
lines.append("\tprivate static ResourceLocation id(String path) {")
lines.append("\t\treturn new ResourceLocation(RoboticaMod.MOD_ID, path);")
lines.append("\t}")
lines.append("")
lines.append("\t@Override")
lines.append("\tpublic void onInitialize() {")
lines.append("\t\tstubOilFluids();")
lines.append("\t\tregisterBlocks();")
lines.append("\t\tregisterItems();")
lines.append("\t\tregisterEntities();")
lines.append("\t\tRoboticaModBlockEntitiesFabric.register();")
lines.append("\t\tRoboticaModSoundsFabric.register();")
lines.append("\t\tRoboticaModTabsFabric.register();")
lines.append("\t\tRoboticaModMenusFabric.register();")
lines.append("\t\tFabricRoboticaModEvents.register();")
lines.append("\t}")
lines.append("")
lines.append("\tprivate void stubOilFluids() {")
lines.append("\t\tRoboticaModFluids.OIL = () -> (FlowingFluid) Fluids.WATER;")
lines.append("\t\tRoboticaModFluids.FLOWING_OIL = () -> (FlowingFluid) Fluids.FLOWING_WATER;")
lines.append("\t\tRoboticaModBlocks.OIL = () -> Blocks.BARRIER;")
lines.append("\t}")
lines.append("")
lines.append("\tprivate void registerBlocks() {")
for field, path in block_paths.items():
    if field == "OIL":
        continue
    cls = block_class[field]
    lines.append(f"\t\tBlock {field.lower()} = Registry.register(BuiltInRegistries.BLOCK, id(\"{path}\"), new {cls}());")
    lines.append(f"\t\tRoboticaModBlocks.{field} = () -> {field.lower()};")
lines.append("\t}")
lines.append("")
lines.append("\tprivate void registerItems() {")
lines.append("\t\tItem oil_bucket = Registry.register(BuiltInRegistries.ITEM, id(\"oil_bucket\"), new Item(new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.UNCOMMON)));")
lines.append("\t\tRoboticaModItems.OIL_BUCKET = () -> oil_bucket;")
for field, path, expr in item_regs:
    lines.append(f"\t\tItem {field.lower()} = Registry.register(BuiltInRegistries.ITEM, id(\"{path}\"), {expr});")
    lines.append(f"\t\tRoboticaModItems.{field} = () -> {field.lower()};")
lines.append("\t}")
lines.append("")
lines.append("\tprivate void registerEntities() {")
entities = [
    ("BOGGIE", "boggie", "BoggieEntity", "BoggieEntity::new", 0.6, 1.0),
    ("SENTINEL", "sentinel", "SentinelEntity", "SentinelEntity::new", 0.6, 3.5),
    ("MECH", "mech", "MechEntity", "MechEntity::new", 2.0, 3.7),
    ("SPACE_SHIP", "space_ship", "SpaceShipEntity", "SpaceShipEntity::new", 4.0, 2.0),
]
for field, path, cls, ctor, w, h in entities:
    var = field.lower() + "Type"
    lines.append(f"\t\tEntityType<{cls}> {var} = Registry.register(BuiltInRegistries.ENTITY_TYPE, id(\"{path}\"),")
    lines.append(f"\t\t\t\tFabricEntityTypeBuilder.createMob().entityFactory({ctor}).spawnGroup(MobCategory.MONSTER)")
    lines.append(f"\t\t\t\t\t.dimensions(EntityDimensions.scalable({w}f, {h}f)).build());")
    lines.append(f"\t\tRoboticaModEntities.{field} = () -> {var};")
    lines.append(f"\t\tFabricDefaultAttributeRegistry.register({var}, {cls}.createAttributes());")
lines.append("\t\tItem sentinel_spawn_egg = Registry.register(BuiltInRegistries.ITEM, id(\"sentinel_spawn_egg\"), new SpawnEggItem(RoboticaModEntities.SENTINEL.get(), -13421773, -6710887, new Item.Properties()));")
lines.append("\t\tRoboticaModItems.SENTINEL_SPAWN_EGG = () -> sentinel_spawn_egg;")
lines.append("\t\tBoggieEntity.init();")
lines.append("\t\tSentinelEntity.init();")
lines.append("\t\tMechEntity.init();")
lines.append("\t\tSpaceShipEntity.init();")
lines.append("\t}")
lines.append("}")

out = os.path.join(base, "fabric/src/main/java/tn/nightbeam/robotica/RoboticaModFabric.java")
with open(out, "w", encoding="utf-8", newline="\n") as f:
    f.write("\n".join(lines) + "\n")
print("Wrote", out)
