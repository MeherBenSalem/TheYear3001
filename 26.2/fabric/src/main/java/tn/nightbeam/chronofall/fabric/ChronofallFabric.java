package tn.nightbeam.chronofall.fabric;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.levelgen.GenerationStep;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.ServerEvents;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.entity.DroneEntity;
import tn.nightbeam.chronofall.entity.MechEntity;
import tn.nightbeam.chronofall.entity.SentinelEntity;
import tn.nightbeam.chronofall.network.Control;
import tn.nightbeam.chronofall.network.ControlPayload;
import tn.nightbeam.chronofall.platform.Platform;
import tn.nightbeam.chronofall.platform.Registrar;

public final class ChronofallFabric implements ModInitializer {
    /** Set by the client entrypoint; a dedicated server never sends control packets. */
    static IntConsumer controlSender = flags -> {
    };

    public static final List<String> ORES = List.of("ore_titanium", "ore_cobalt", "ore_orichalcum", "ore_scrap_debris");

    @Override
    public void onInitialize() {
        Chronofall.init(new FabricPlatform(), new FabricRegistrar());
        FabricDefaultAttributeRegistry.register(ModContent.SENTINEL.get(), SentinelEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModContent.DRONE.get(), DroneEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModContent.MECH.get(), MechEntity.createAttributes());
        for (String ore : ORES) {
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES,
                    ResourceKey.create(Registries.PLACED_FEATURE, Chronofall.id(ore)));
        }
        PayloadTypeRegistry.serverboundPlay().register(ControlPayload.TYPE, ControlPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ControlPayload.TYPE, (payload, context) ->
                context.server().execute(() -> Control.receive(context.player(), payload.flags())));
        ServerTickEvents.END_SERVER_TICK.register(ServerEvents::serverTick);
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) -> ServerEvents.registerCommands(dispatcher));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> ServerEvents.logout(handler.player));
        CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Chronofall.id("chronofall")))
                .register(output -> ModContent.tabItems().forEach(output::accept));
        AttackEntityCallback.EVENT.register((player, level, hand, target, hit) ->
                ServerEvents.allowAttack(player, target) ? InteractionResult.PASS : InteractionResult.FAIL);
    }

    private static final class FabricRegistrar implements Registrar {
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public <R, T extends R> Supplier<T> register(ResourceKey<? extends Registry<R>> registry, String path, Function<Identifier, T> factory) {
            Identifier id = Chronofall.id(path);
            Registry<R> target = (Registry<R>) BuiltInRegistries.REGISTRY.getValueOrThrow((ResourceKey) registry);
            T value = factory.apply(id);
            Registry.register(target, id, value);
            return () -> value;
        }
    }

    private static final class FabricPlatform implements Platform {
        @Override
        public Path configDir() {
            return FabricLoader.getInstance().getConfigDir();
        }

        @Override
        public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuConstructor<T> constructor) {
            return new MenuType<>(constructor::create, FeatureFlags.VANILLA_SET);
        }

        @Override
        public CreativeModeTab.Builder tabBuilder() {
            return FabricCreativeModeTab.builder();
        }

        @Override
        public void sendControl(int flags) {
            controlSender.accept(flags);
        }
    }
}
