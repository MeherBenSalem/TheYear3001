package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.jei_recipes.ExtendedCraftingRecipe;
import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraft.world.item.crafting.RecipeSerializer;

@Mod.EventBusSubscriber(modid = Theyear3001Mod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Theyear3001ModRecipeTypes {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "theyear3001");

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		event.enqueueWork(() -> {
			SERIALIZERS.register(bus);
			SERIALIZERS.register("extended_crafting", () -> ExtendedCraftingRecipe.Serializer.INSTANCE);
		});
	}
}
