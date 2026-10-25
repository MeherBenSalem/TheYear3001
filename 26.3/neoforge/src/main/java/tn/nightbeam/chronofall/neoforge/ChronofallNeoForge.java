package tn.nightbeam.chronofall.neoforge;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.Constants;
import tn.nightbeam.chronofall.ServerEvents;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.entity.DroneEntity;
import tn.nightbeam.chronofall.entity.MechEntity;
import tn.nightbeam.chronofall.entity.SentinelEntity;
import tn.nightbeam.chronofall.legacy.LegacyAliases;
import tn.nightbeam.chronofall.network.Control;
import tn.nightbeam.chronofall.network.ControlPayload;
import tn.nightbeam.chronofall.platform.Platform;
import tn.nightbeam.chronofall.platform.Registrar;

@Mod(Constants.MOD_ID)
public final class ChronofallNeoForge {
    /** Set by the client entrypoint; a dedicated server never sends control packets. */
    static java.util.function.IntConsumer controlSender = flags -> {
    };
    private final Map<ResourceKey<?>, DeferredRegister<?>> registers = new HashMap<>();

    public ChronofallNeoForge(IEventBus modBus) {
        Chronofall.init(new NeoPlatform(), new Registrar() {
            @Override
            public <R, T extends R> Supplier<T> register(ResourceKey<? extends Registry<R>> registry, String path, Function<Identifier, T> factory) {
                return deferred(registry).register(path, factory);
            }
        });
        for (ResourceKey<? extends Registry<?>> registry : List.of(Registries.BLOCK, Registries.ITEM, Registries.ENTITY_TYPE, Registries.BLOCK_ENTITY_TYPE)) {
            DeferredRegister<?> deferred = registers.get(registry);
            LegacyAliases.each(registry, deferred::addAlias);
        }
        registers.values().forEach(deferred -> deferred.register(modBus));
        modBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(ModContent.SENTINEL.get(), SentinelEntity.createAttributes().build());
            event.put(ModContent.DRONE.get(), DroneEntity.createAttributes().build());
            event.put(ModContent.MECH.get(), MechEntity.createAttributes().build());
        });
        modBus.addListener((RegisterCapabilitiesEvent event) -> {
            for (BlockEntityType<?> type : List.of(ModContent.FABRICATOR_ENTITY.get(), ModContent.FLUX_CHARGER_ENTITY.get())) {
                event.registerBlockEntity(Capabilities.Item.BLOCK, type,
                        (be, side) -> new WorldlyContainerWrapper((net.minecraft.world.WorldlyContainer) be, side == null ? net.minecraft.core.Direction.UP : side));
            }
        });
        ResourceKey<CreativeModeTab> tab = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Chronofall.id("chronofall"));
        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTabKey().equals(tab)) {
                ModContent.tabItems().forEach(stack -> event.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
            }
        });
        modBus.addListener((RegisterPayloadHandlersEvent event) -> event.registrar("1").playToServer(ControlPayload.TYPE, ControlPayload.CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player) {
                        Control.receive(player, payload.flags());
                    }
                })));
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) -> ServerEvents.serverTick(event.getServer()));
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> ServerEvents.registerCommands(event.getDispatcher()));
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                ServerEvents.logout(player);
            }
        });
        NeoForge.EVENT_BUS.addListener((AttackEntityEvent event) -> {
            if (!ServerEvents.allowAttack(event.getEntity(), event.getTarget())) {
                event.setCanceled(true);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <R> DeferredRegister<R> deferred(ResourceKey<? extends Registry<R>> registry) {
        return (DeferredRegister<R>) registers.computeIfAbsent(registry,
                key -> DeferredRegister.create((ResourceKey<? extends Registry<R>>) key, Constants.MOD_ID));
    }

    private static final class NeoPlatform implements Platform {
        @Override
        public Path configDir() {
            return FMLPaths.CONFIGDIR.get();
        }

        @Override
        public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuConstructor<T> constructor) {
            return new MenuType<>(constructor::create, FeatureFlags.VANILLA_SET);
        }

        @Override
        public CreativeModeTab.Builder tabBuilder() {
            return CreativeModeTab.builder();
        }

        @Override
        public void sendControl(int flags) {
            controlSender.accept(flags);
        }
    }
}
