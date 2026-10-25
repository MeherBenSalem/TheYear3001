package tn.nightbeam.chronofall.platform;

import java.nio.file.Path;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/** What common code needs from the loader. Each loader passes an implementation to {@code Chronofall.init}. */
public interface Platform {
    Path configDir();

    /** Menu types; their constructor takes a package-private interface in vanilla, so each loader builds them. */
    <T extends AbstractContainerMenu> MenuType<T> menuType(MenuConstructor<T> constructor);

    /** A builder for the mod's creative tab, placed the loader's way. */
    CreativeModeTab.Builder tabBuilder();

    /** Furnace burn time of {@code stack} in ticks (0 if it is not fuel), including modded fuels. */
    int burnTicks(ItemStack stack);

    /**
     * Jetpack flight: resets the server's "floating too long" counter so a thrusting player is not kicked for flying
     * (1.20.1 keeps it private; each loader reaches it its own way).
     */
    void resetFloating(net.minecraft.server.level.ServerPlayer player);

    /** The entity reach attribute (Forge has one; Fabric 1.20.1 does not, so the mech rider's reach bonus is Forge-only there). */
    @org.jetbrains.annotations.Nullable
    net.minecraft.world.entity.ai.attributes.Attribute entityReach();

    /** Sends the control state (see {@code network.Control}) from the client to the server. */
    void sendControl(int flags);

    @FunctionalInterface
    interface MenuConstructor<T extends AbstractContainerMenu> {
        T create(int containerId, Inventory inventory);
    }
}
