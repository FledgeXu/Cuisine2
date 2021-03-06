package snownee.cuisine.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;

public class CuisineRegistries {
    public static final ForgeRegistry<Cookware> COOKWARES = makeRegistry("cookware", Cookware.class, false);
    public static final ForgeRegistry<Material> MATERIALS = makeRegistry("material", Material.class, true);
    public static final ForgeRegistry<Spice> SPICES = makeRegistry("spice", Spice.class, true);
    public static final ForgeRegistry<CuisineFood> FOODS = makeRegistry("food", CuisineFood.class, true);
    public static final ForgeRegistry<CuisineRecipe> RECIPES = makeRegistry("recipe", CuisineRecipe.class, true);

    private static <T extends IForgeRegistryEntry<T>> ForgeRegistry<T> makeRegistry(String name, Class<T> clazz, boolean data) {
        RegistryBuilder<T> builder = new RegistryBuilder<T>().setType(clazz).setName(new ResourceLocation("cuisine", name)).allowModification();
        if (data) {
            builder.disableSaving();
            builder.disableSync();
        }
        return (ForgeRegistry<T>) builder.create();
    }
}
