import re
import os

base = r"C:\Users\mahou\OneDrive\Documents\GitHub\TheYear3001\1.20.1"
forge_items = open(os.path.join(base, "forge/src/main/java/tn/nightbeam/robotica/init/RoboticaModItems.java"), encoding="utf-8").read()
forge_blocks = open(os.path.join(base, "forge/src/main/java/tn/nightbeam/robotica/init/RoboticaModBlocks.java"), encoding="utf-8").read()
forge_entities = open(os.path.join(base, "forge/src/main/java/tn/nightbeam/robotica/init/RoboticaModEntities.java"), encoding="utf-8").read()


def strip_mcreator(text):
    text = re.sub(r"/\*\s*\n\s*\*\s*MCreator note:.*?\*/\s*\n", "", text, flags=re.S)
    text = re.sub(r"^\s*// Start of user code block[^\n]*\n", "", text, flags=re.M)
    text = re.sub(r"^\s*// End of user code block[^\n]*\n", "", text, flags=re.M)
    return text


item_fields = re.findall(r"public static final RegistryObject<Item> (\w+) =", forge_items)
block_fields = re.findall(r"public static final RegistryObject<Block> (\w+) =", forge_blocks)
entity_fields = re.findall(r"public static final RegistryObject<EntityType<(\w+)>> (\w+) =", forge_entities)

common_items = """package tn.nightbeam.robotica.init;

import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public class RoboticaModItems {
"""
for f in item_fields:
    common_items += f"\tpublic static Supplier<Item> {f};\n"
common_items += "}\n"

common_blocks = """package tn.nightbeam.robotica.init;

import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;

public class RoboticaModBlocks {
"""
for f in block_fields:
    common_blocks += f"\tpublic static Supplier<Block> {f};\n"
common_blocks += "}\n"

common_entities = """package tn.nightbeam.robotica.init;

import net.minecraft.world.entity.EntityType;
import tn.nightbeam.robotica.entity.BoggieEntity;
import tn.nightbeam.robotica.entity.SentinelEntity;
import tn.nightbeam.robotica.entity.MechEntity;
import tn.nightbeam.robotica.entity.SpaceShipEntity;
import java.util.function.Supplier;

public class RoboticaModEntities {
"""
for etype, fname in entity_fields:
    common_entities += f"\tpublic static Supplier<EntityType<{etype}>> {fname};\n"
common_entities += "}\n"

common_menus = """package tn.nightbeam.robotica.init;

import net.minecraft.world.inventory.MenuType;
import tn.nightbeam.robotica.world.inventory.CraftingWorkStationGUIMenu;
import java.util.function.Supplier;

public class RoboticaModMenus {
\tpublic static Supplier<MenuType<CraftingWorkStationGUIMenu>> CRAFTING_WORK_STATION_GUI;
}
"""

menu_accessor = """package tn.nightbeam.robotica.init;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import tn.nightbeam.robotica.platform.Services;
import java.util.Map;

public interface MenuAccessor {
\tMap<String, Object> getMenuState();
\tMap<Integer, Slot> getSlots();

\tdefault void sendMenuStateUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
\t\tgetMenuState().put(elementType + ":" + name, elementState);
\t\tServices.PLATFORM.sendMenuUpdate(player, elementType, name, elementState, needClientUpdate);
\t}

\tdefault <T> T getMenuState(int elementType, String name, T defaultValue) {
\t\ttry {
\t\t\treturn (T) getMenuState().getOrDefault(elementType + ":" + name, defaultValue);
\t\t} catch (ClassCastException e) {
\t\t\treturn defaultValue;
\t\t}
\t}
}
"""

screen_accessor = """package tn.nightbeam.robotica.init;

public interface ScreenAccessor {
\tvoid updateMenuState(int elementType, String name, Object elementState);
}
"""

init_dir = os.path.join(base, "common/src/main/java/tn/nightbeam/robotica/init")
os.makedirs(init_dir, exist_ok=True)
for name, content in [
    ("RoboticaModItems.java", common_items),
    ("RoboticaModBlocks.java", common_blocks),
    ("RoboticaModEntities.java", common_entities),
    ("RoboticaModMenus.java", common_menus),
    ("MenuAccessor.java", menu_accessor),
    ("ScreenAccessor.java", screen_accessor),
]:
    with open(os.path.join(init_dir, name), "w", encoding="utf-8", newline="\n") as f:
        f.write(content)

for fname in ["RoboticaModItems.java", "RoboticaModBlocks.java", "RoboticaModEntities.java"]:
    path = os.path.join(base, f"forge/src/main/java/tn/nightbeam/robotica/init/{fname}")
    text = strip_mcreator(open(path, encoding="utf-8").read())
    cls = fname.replace(".java", "")
    assigns = []
    if cls == "RoboticaModItems":
        for f in item_fields:
            assigns.append(f"\t\ttn.nightbeam.robotica.init.RoboticaModItems.{f} = {f};")
    elif cls == "RoboticaModBlocks":
        for f in block_fields:
            assigns.append(f"\t\ttn.nightbeam.robotica.init.RoboticaModBlocks.{f} = {f};")
    elif cls == "RoboticaModEntities":
        for etype, f in entity_fields:
            assigns.append(f"\t\ttn.nightbeam.robotica.init.RoboticaModEntities.{f} = {f};")
    if assigns and "static {" not in text:
        text = text.rstrip()
        if text.endswith("}"):
            text = text[:-1]
        text += "\n\n\tstatic {\n" + "\n".join(assigns) + "\n\t}\n}\n"
    with open(path, "w", encoding="utf-8", newline="\n") as f:
        f.write(text)

print("Generated common init and patched forge init")
print("Items:", len(item_fields), "Blocks:", len(block_fields), "Entities:", len(entity_fields))
