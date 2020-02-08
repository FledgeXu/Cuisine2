package snownee.cuisine.api;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import snownee.cuisine.api.registry.Cookware;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.CuisineFoodInstance;
import snownee.cuisine.api.registry.CuisineRecipe;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.MaterialInstance;
import snownee.cuisine.api.registry.Spice;

public interface FoodBuilder<C> {

    void add(MaterialInstance materialInstance);

    void add(Material material);

    void add(CuisineFoodInstance cuisineFoodInstance);

    void add(CuisineFood cuisineFood);

    void add(Spice spice, int incr);

    void add(Spice spice);

    boolean has(Object o);

    int count(Object o);

    List<MaterialInstance> getMaterials();

    List<CuisineFoodInstance> getFoods();

    Object2IntMap<Spice> getSpices();

    Cookware getCookware();

    C getContext();

    @Nullable
    Entity getCook();

    default Optional<CuisineRecipe> findRecipe() {
        return CuisineAPI.findRecipe(this);
    }

    ItemStack build(CuisineRecipe recipe);

}
