package tn.nightbeam.chronofall.content;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.block.FabricatorBlock;
import tn.nightbeam.chronofall.block.FabricatorBlockEntity;
import tn.nightbeam.chronofall.block.FluxChargerBlock;
import tn.nightbeam.chronofall.block.FluxChargerBlockEntity;
import tn.nightbeam.chronofall.block.RiftBeaconBlock;
import tn.nightbeam.chronofall.block.RiftBeaconBlockEntity;
import tn.nightbeam.chronofall.block.RiftCacheBlock;
import tn.nightbeam.chronofall.block.RiftGateBlock;
import tn.nightbeam.chronofall.block.RiftGateBlockEntity;
import tn.nightbeam.chronofall.core.Catalog;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.entity.ChronoPodEntity;
import tn.nightbeam.chronofall.entity.DroneEntity;
import tn.nightbeam.chronofall.entity.LaserBoltEntity;
import tn.nightbeam.chronofall.entity.MechEntity;
import tn.nightbeam.chronofall.entity.SentinelEntity;
import tn.nightbeam.chronofall.entity.SkiffEntity;
import tn.nightbeam.chronofall.item.DescribedItem;
import tn.nightbeam.chronofall.item.JetpackItem;
import tn.nightbeam.chronofall.item.PlasmaCutterItem;
import tn.nightbeam.chronofall.item.PulseRifleItem;
import tn.nightbeam.chronofall.item.RiftTunerItem;
import tn.nightbeam.chronofall.item.RobotItem;
import tn.nightbeam.chronofall.menu.FabricatorMenu;
import tn.nightbeam.chronofall.menu.FluxChargerMenu;
import tn.nightbeam.chronofall.platform.Platform;
import tn.nightbeam.chronofall.platform.Registrar;

/** Everything the mod registers. Ids are listed in {@link Catalog}; old worlds are remapped by {@code LegacyIds}. */
public final class ModContent {
    public static final String NAMESPACE = tn.nightbeam.chronofall.Constants.MOD_ID;
    public static final Map<String, Supplier<Block>> BLOCKS = new LinkedHashMap<>();
    public static final Map<String, Supplier<Item>> ITEMS = new LinkedHashMap<>();
    public static final Map<String, Supplier<SoundEvent>> SOUNDS = new LinkedHashMap<>();

    public static Supplier<Block> FABRICATOR;
    public static Supplier<Block> FLUX_CHARGER;
    public static Supplier<Block> RIFT_GATE;
    public static Supplier<Block> RIFT_CACHE;
    public static Supplier<Block> RIFT_BEACON;
    public static Supplier<Block> RIFT_GLASS;
    public static Supplier<Block> SCRAP_DEBRIS;

    public static Supplier<BlockEntityType<FabricatorBlockEntity>> FABRICATOR_ENTITY;
    public static Supplier<BlockEntityType<FluxChargerBlockEntity>> FLUX_CHARGER_ENTITY;
    public static Supplier<BlockEntityType<RiftGateBlockEntity>> RIFT_GATE_ENTITY;
    public static Supplier<BlockEntityType<RiftBeaconBlockEntity>> RIFT_BEACON_ENTITY;

    public static Supplier<EntityType<SentinelEntity>> SENTINEL;
    public static Supplier<EntityType<DroneEntity>> DRONE;
    public static Supplier<EntityType<MechEntity>> MECH;
    public static Supplier<EntityType<SkiffEntity>> SKIFF;
    public static Supplier<EntityType<ChronoPodEntity>> CHRONO_POD;
    public static Supplier<EntityType<LaserBoltEntity>> LASER_BOLT;

    public static Supplier<MenuType<FabricatorMenu>> FABRICATOR_MENU;
    public static Supplier<MenuType<FluxChargerMenu>> FLUX_CHARGER_MENU;

    public static Supplier<SimpleParticleType> FLUX_PARTICLE;
    public static Supplier<SimpleParticleType> RIFT_PARTICLE;
    public static Supplier<SimpleParticleType> THRUSTER_PARTICLE;
    public static Supplier<SimpleParticleType> LASER_PARTICLE;

    public static Supplier<CreativeModeTab> TAB;

    /** Sound event paths; each has an entry in {@code sounds.json}. */
    public static final List<String> SOUND_IDS = List.of("rift_open", "pod_fall", "pod_impact", "cache_open", "laser_shot", "laser_hit",
            "jetpack_loop", "skiff_loop", "mech_step", "mech_slam", "gate_charge", "gate_warp", "fabricator_print", "fabricator_done",
            "charger_hum", "drone_beep", "sentinel_ambient", "sentinel_hurt", "sentinel_death", "robot_hurt", "cutter_loop");

    private ModContent() {
    }

    public static void register(Registrar r, Platform platform) {
        for (String sound : SOUND_IDS) {
            SOUNDS.put(sound, r.register(Registries.SOUND_EVENT, sound, SoundEvent::createVariableRangeEvent));
        }
        FLUX_PARTICLE = r.register(Registries.PARTICLE_TYPE, "flux", id -> new GlowParticleType());
        RIFT_PARTICLE = r.register(Registries.PARTICLE_TYPE, "rift", id -> new GlowParticleType());
        THRUSTER_PARTICLE = r.register(Registries.PARTICLE_TYPE, "thruster", id -> new GlowParticleType());
        LASER_PARTICLE = r.register(Registries.PARTICLE_TYPE, "laser", id -> new GlowParticleType());

        registerBlocks(r);
        registerItems(r);
        registerEntities(r);

        FABRICATOR_ENTITY = r.register(Registries.BLOCK_ENTITY_TYPE, "fabricator",
                id -> BlockEntityType.Builder.of(FabricatorBlockEntity::new, FABRICATOR.get()).build(null));
        FLUX_CHARGER_ENTITY = r.register(Registries.BLOCK_ENTITY_TYPE, "flux_charger",
                id -> BlockEntityType.Builder.of(FluxChargerBlockEntity::new, FLUX_CHARGER.get()).build(null));
        RIFT_GATE_ENTITY = r.register(Registries.BLOCK_ENTITY_TYPE, "rift_gate",
                id -> BlockEntityType.Builder.of(RiftGateBlockEntity::new, RIFT_GATE.get()).build(null));
        RIFT_BEACON_ENTITY = r.register(Registries.BLOCK_ENTITY_TYPE, "rift_beacon",
                id -> BlockEntityType.Builder.of(RiftBeaconBlockEntity::new, RIFT_BEACON.get()).build(null));

        FABRICATOR_MENU = r.register(Registries.MENU, "fabricator", id -> platform.menuType(FabricatorMenu::new));
        FLUX_CHARGER_MENU = r.register(Registries.MENU, "flux_charger", id -> platform.menuType(FluxChargerMenu::new));

        TAB = r.register(Registries.CREATIVE_MODE_TAB, "chronofall", id -> platform.tabBuilder()
                .title(Component.translatable("itemGroup.chronofall"))
                .icon(() -> new ItemStack(ITEMS.get("jetpack").get()))
                .displayItems((params, output) -> tabItems().forEach(output::accept))
                .build());
    }

    private static void registerBlocks(Registrar r) {
        Supplier<BlockBehaviour.Properties> metal = () -> BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 6.0F)
                .sound(SoundType.METAL).requiresCorrectToolForDrops();
        block(r, "scrap_block", id -> new Block(metal.get().mapColor(MapColor.COLOR_BROWN)));
        block(r, "scrap_slab", id -> new SlabBlock(metal.get().mapColor(MapColor.COLOR_BROWN)));
        block(r, "scrap_stairs_block", id -> new tn.nightbeam.chronofall.block.ScrapStairsBlock(BLOCKS.get("scrap_block").get().defaultBlockState(), metal.get().mapColor(MapColor.COLOR_BROWN)));
        Supplier<BlockBehaviour.Properties> ore = () -> BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F)
                .sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();
        SCRAP_DEBRIS = block(r, "scrap_debris", id -> new DropExperienceBlock(ore.get().mapColor(MapColor.COLOR_BROWN)
                .sound(SoundType.ANCIENT_DEBRIS), UniformInt.of(1, 3)));
        block(r, "titanium_ore", id -> new DropExperienceBlock(ore.get(), UniformInt.of(0, 0)));
        block(r, "cobalt_ore", id -> new DropExperienceBlock(ore.get(), UniformInt.of(0, 0)));
        block(r, "orichalcum_ore", id -> new DropExperienceBlock(ore.get().strength(5.0F, 6.0F), UniformInt.of(0, 0)));
        block(r, "titanium_block", id -> new Block(metal.get().strength(5.0F, 6.0F).mapColor(MapColor.TERRACOTTA_WHITE)));
        block(r, "cobalt_block", id -> new Block(metal.get().strength(5.0F, 6.0F).mapColor(MapColor.COLOR_BLUE)));
        block(r, "orichalcum_block", id -> new Block(metal.get().strength(6.0F, 8.0F).mapColor(MapColor.COLOR_ORANGE)));
        RIFT_GLASS = block(r, "rift_glass", id -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1.5F, 6.0F)
                .sound(SoundType.GLASS).lightLevel(state -> 7).emissiveRendering((state, level, pos) -> true)));
        FABRICATOR = block(r, "fabricator", id -> new FabricatorBlock(metal.get().strength(3.5F, 6.0F).noOcclusion()));
        FLUX_CHARGER = block(r, "flux_charger", id -> new FluxChargerBlock(metal.get().strength(3.5F, 6.0F)
                .lightLevel(state -> state.getValue(FluxChargerBlock.LIT) ? 10 : 2)));
        RIFT_GATE = block(r, "rift_gate", id -> new RiftGateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(3.0F, 6.0F)
                .sound(SoundType.AMETHYST).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(RiftGateBlock.LINKED) ? 11 : 4)
                .noOcclusion()));
        RIFT_CACHE = block(r, "rift_cache", id -> new RiftCacheBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
                .strength(-1.0F, 3_600_000.0F).sound(SoundType.METAL).lightLevel(state -> 9).noLootTable().pushReaction(PushReaction.BLOCK)
                .noOcclusion()));
        RIFT_BEACON = block(r, "rift_beacon", id -> new RiftBeaconBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                .strength(5.0F, 1200.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().lightLevel(state -> 12).noOcclusion()));
        for (String path : Catalog.BLOCKS) {
            Supplier<Block> block = BLOCKS.get(path);
            Rarity rarity = path.startsWith("rift_") && !path.equals("rift_glass") ? Rarity.RARE : Rarity.COMMON;
            ITEMS.put(path, r.register(Registries.ITEM, path, id -> new BlockItem(block.get(), new Item.Properties().rarity(rarity))));
        }
    }

    private static void registerItems(Registrar r) {
        for (String path : List.of("scrap", "raw_titanium", "titanium_ingot", "raw_cobalt", "cobalt_ingot", "raw_orichalcum", "orichalcum_ingot",
                "electrical_conductors", "blank_printed_circuit_board", "printed_circuit_board", "central_processing_unit", "blank_card")) {
            item(r, path, id -> new Item(new Item.Properties()));
        }
        item(r, "rift_shard", id -> new DescribedItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
        item(r, "flux_cell", id -> new DescribedItem(new Item.Properties()));
        for (String card : List.of("repair_card", "bulk_upgrade_card", "damage_upgrade_card", "defense_upgrade_card", "speed_upgrade_card",
                "collector_chip", "guard_chip", "lamp_chip")) {
            item(r, card, id -> new DescribedItem(new Item.Properties().stacksTo(16)));
        }
        item(r, "jetpack", id -> new JetpackItem(ModMaterials.JETPACK, new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        item(r, "plasma_cutter", id -> new PlasmaCutterItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        item(r, "pulse_rifle", id -> new PulseRifleItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        item(r, "rift_tuner", id -> new RiftTunerItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
        item(r, "drone", id -> new RobotItem(() -> DRONE.get(), Flux.DRONE_CAPACITY, new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        item(r, "mech", id -> new RobotItem(() -> MECH.get(), Flux.MECH_CAPACITY, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
        item(r, "skiff", id -> new RobotItem(() -> SKIFF.get(), Flux.SKIFF_CAPACITY, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
        item(r, "sentinel_spawn_egg", id -> new tn.nightbeam.chronofall.item.SentinelEggItem(new Item.Properties()));

        for (String metal : Catalog.METALS) {
            ModMaterials.Metal m = ModMaterials.metal(metal);
            item(r, metal + "_sword", id -> new SwordItem(m.tier(), 3, -2.4F, new Item.Properties()));
            item(r, metal + "_pickaxe", id -> new PickaxeItem(m.tier(), 1, -2.8F, new Item.Properties()) { });
            item(r, metal + "_axe", id -> new AxeItem(m.tier(), m.axeDamage(), m.axeSpeed(), new Item.Properties()) { });
            item(r, metal + "_shovel", id -> new ShovelItem(m.tier(), 1.5F, -3.0F, new Item.Properties()));
            item(r, metal + "_hoe", id -> new HoeItem(m.tier(), m.hoeDamage(), m.hoeSpeed(), new Item.Properties()) { });
            for (ArmorItem.Type type : List.of(ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS)) {
                String piece = type.getName();
                item(r, metal + "_armor_" + piece, id -> new ArmorItem(m.armor(), type, new Item.Properties()));
            }
        }
    }

    private static void registerEntities(Registrar r) {
        SENTINEL = r.register(Registries.ENTITY_TYPE, "sentinel", id -> EntityType.Builder.of(SentinelEntity::new, MobCategory.MONSTER)
                .sized(0.8F, 2.3F).clientTrackingRange(10).build(id.toString()));
        DRONE = r.register(Registries.ENTITY_TYPE, "drone", id -> EntityType.Builder.of(DroneEntity::new, MobCategory.MISC)
                .sized(0.6F, 0.6F).clientTrackingRange(10).build(id.toString()));
        MECH = r.register(Registries.ENTITY_TYPE, "mech", id -> EntityType.Builder.of(MechEntity::new, MobCategory.MISC)
                .sized(1.8F, 2.9F).clientTrackingRange(10).build(id.toString()));
        SKIFF = r.register(Registries.ENTITY_TYPE, "skiff", id -> EntityType.Builder.<SkiffEntity>of(SkiffEntity::new, MobCategory.MISC)
                .sized(2.0F, 1.0F).clientTrackingRange(10).build(id.toString()));
        CHRONO_POD = r.register(Registries.ENTITY_TYPE, "chrono_pod", id -> EntityType.Builder.<ChronoPodEntity>of(ChronoPodEntity::new, MobCategory.MISC)
                .sized(1.2F, 1.6F).clientTrackingRange(16).updateInterval(1).fireImmune().build(id.toString()));
        LASER_BOLT = r.register(Registries.ENTITY_TYPE, "laser_bolt", id -> EntityType.Builder.<LaserBoltEntity>of(LaserBoltEntity::new, MobCategory.MISC)
                .sized(0.25F, 0.25F).clientTrackingRange(8).updateInterval(1).fireImmune().build(id.toString()));
    }

    private static Supplier<Block> block(Registrar r, String path, Function<ResourceLocation, Block> factory) {
        Supplier<Block> block = r.register(Registries.BLOCK, path, factory);
        BLOCKS.put(path, block);
        return block;
    }

    private static Supplier<Item> item(Registrar r, String path, Function<ResourceLocation, Item> factory) {
        Supplier<Item> item = r.register(Registries.ITEM, path, factory);
        ITEMS.put(path, item);
        return item;
    }

    public static Item item(String path) {
        return ITEMS.get(path).get();
    }

    public static Block block(String path) {
        return BLOCKS.get(path).get();
    }

    public static SoundEvent sound(String path) {
        return SOUNDS.get(path).get();
    }

    /** The items in creative-tab order: devices first, then robots, rift blocks, machines, parts, materials, gear. */
    public static List<ItemStack> tabItems() {
        List<String> order = new ArrayList<>(List.of("jetpack", "plasma_cutter", "pulse_rifle", "drone", "mech", "skiff", "rift_tuner",
                "fabricator", "flux_charger", "rift_gate", "rift_beacon", "rift_cache", "rift_glass",
                "collector_chip", "guard_chip", "lamp_chip", "repair_card", "bulk_upgrade_card", "damage_upgrade_card", "defense_upgrade_card",
                "speed_upgrade_card", "blank_card", "flux_cell", "central_processing_unit", "printed_circuit_board", "blank_printed_circuit_board",
                "electrical_conductors", "rift_shard", "scrap", "raw_titanium", "titanium_ingot", "raw_cobalt", "cobalt_ingot", "raw_orichalcum",
                "orichalcum_ingot", "scrap_debris", "titanium_ore", "cobalt_ore", "orichalcum_ore", "scrap_block", "scrap_slab", "scrap_stairs_block",
                "titanium_block", "cobalt_block", "orichalcum_block"));
        for (String metal : Catalog.METALS) {
            for (String tool : Catalog.TOOLS) {
                order.add(metal + "_" + tool);
            }
            for (String piece : Catalog.ARMOR) {
                order.add(metal + "_armor_" + piece);
            }
        }
        order.add("sentinel_spawn_egg");
        List<ItemStack> stacks = new ArrayList<>();
        for (String path : order) {
            Item item = item(path);
            stacks.add(item instanceof RobotItem robot ? robot.charged() : item instanceof tn.nightbeam.chronofall.item.FluxItem flux
                    ? flux.charged(new ItemStack(item)) : new ItemStack(item));
        }
        return stacks;
    }

    /** A particle type that ignores the particle limiter, so beams and rifts show from far away. */
    private static final class GlowParticleType extends SimpleParticleType {
        GlowParticleType() {
            super(true);
        }
    }
}
