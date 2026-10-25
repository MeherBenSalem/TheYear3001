package tn.nightbeam.chronofall.forge;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
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
public final class ChronofallForge {
    static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ControlPayload.ID, () -> "1", "1"::equals, "1"::equals);
    /** Set by the client setup; a dedicated server never sends control packets. */
    static IntConsumer controlSender = flags -> {
    };
    /** SRG name of {@code ServerGamePacketListenerImpl.aboveGroundTickCount} (private in 1.20.1). */
    private static final String ABOVE_GROUND_TICKS = "f_9737_";

    private final Map<ResourceKey<?>, DeferredRegister<?>> registers = new HashMap<>();

    public ChronofallForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Chronofall.init(new ForgePlatform(), new Registrar() {
            @Override
            public <R, T extends R> Supplier<T> register(ResourceKey<? extends Registry<R>> registry, String path, Function<ResourceLocation, T> factory) {
                ResourceLocation id = Chronofall.id(path);
                return deferred(registry).register(path, () -> factory.apply(id));
            }
        });
        registers.values().forEach(deferred -> deferred.register(modBus));
        modBus.addListener((RegisterEvent event) -> {
            for (ResourceKey<? extends Registry<?>> registry : List.of(Registries.BLOCK, Registries.ITEM, Registries.ENTITY_TYPE,
                    Registries.BLOCK_ENTITY_TYPE)) {
                if (event.getRegistryKey().equals(registry) && event.getForgeRegistry() instanceof ForgeRegistry<?> forge) {
                    LegacyAliases.each(registry, forge::addAlias);
                }
            }
        });
        modBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(ModContent.SENTINEL.get(), SentinelEntity.createAttributes().build());
            event.put(ModContent.DRONE.get(), DroneEntity.createAttributes().build());
            event.put(ModContent.MECH.get(), MechEntity.createAttributes().build());
        });
        CHANNEL.messageBuilder(ControlPayload.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ControlPayload::write)
                .decoder(ControlPayload::read)
                .consumerMainThread((payload, context) -> {
                    ServerPlayer player = context.get().getSender();
                    if (player != null) {
                        Control.receive(player, payload.flags());
                    }
                })
                .add();
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                ServerEvents.serverTick(event.getServer());
            }
        });
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> ServerEvents.registerCommands(event.getDispatcher()));
        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                ServerEvents.logout(player);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((AttackEntityEvent event) -> {
            if (!ServerEvents.allowAttack(event.getEntity(), event.getTarget())) {
                event.setCanceled(true);
            }
        });
        DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT, () -> () -> ChronofallForgeClient.init(modBus));
    }

    @SuppressWarnings("unchecked")
    private <R> DeferredRegister<R> deferred(ResourceKey<? extends Registry<R>> registry) {
        return (DeferredRegister<R>) registers.computeIfAbsent(registry,
                key -> DeferredRegister.create((ResourceKey<? extends Registry<R>>) key, Constants.MOD_ID));
    }

    private static final class ForgePlatform implements Platform {
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
        public int burnTicks(ItemStack stack) {
            return stack.isEmpty() ? 0 : ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        }

        @Override
        public void resetFloating(ServerPlayer player) {
            ObfuscationReflectionHelper.setPrivateValue(ServerGamePacketListenerImpl.class, player.connection, 0, ABOVE_GROUND_TICKS);
        }

        @Override
        public Attribute entityReach() {
            return ForgeMod.ENTITY_REACH.get();
        }

        @Override
        public void sendControl(int flags) {
            controlSender.accept(flags);
        }
    }
}
