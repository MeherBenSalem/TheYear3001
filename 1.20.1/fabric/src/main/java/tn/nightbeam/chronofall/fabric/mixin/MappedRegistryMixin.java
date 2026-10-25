package tn.nightbeam.chronofall.fabric.mixin;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tn.nightbeam.chronofall.core.LegacyIds;

/**
 * Old worlds name Chronofall content {@code robotica:...} (1.0) or {@code theyear3001:...} (0.x). Every registry
 * lookup by id goes through these methods (block states in chunks, item stacks, entities and block entities), so an
 * old id is turned into its current id before the lookup.
 */
@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> {
    @ModifyVariable(method = {"get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;",
            "containsKey(Lnet/minecraft/resources/ResourceLocation;)Z"}, at = @At("HEAD"), argsOnly = true)
    private ResourceLocation chronofall$legacyId(ResourceLocation id) {
        return remap(id);
    }

    @ModifyVariable(method = {"get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;",
            "getHolder(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;",
            "containsKey(Lnet/minecraft/resources/ResourceKey;)Z"}, at = @At("HEAD"), argsOnly = true)
    private ResourceKey<T> chronofall$legacyKey(ResourceKey<T> key) {
        if (key == null) {
            return null;
        }
        ResourceLocation id = remap(key.location());
        return id == key.location() ? key : ResourceKey.create(ResourceKey.createRegistryKey(key.registry()), id);
    }

    private static ResourceLocation remap(ResourceLocation id) {
        if (id == null || !LegacyIds.OLD_NAMESPACES.contains(id.getNamespace())) {
            return id;
        }
        String current = LegacyIds.remap(id.toString());
        return current == null ? id : new ResourceLocation(current);
    }
}
