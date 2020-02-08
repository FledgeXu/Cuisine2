package snownee.cuisine.api.registry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import snownee.cuisine.Cuisine;
import snownee.cuisine.api.Bonus;

public class MaterialInstance extends Pair<Material, Integer> {

    public final Material material;
    public int star;

    public MaterialInstance(Material material, int star) {
        this.material = material;
        this.star = star;
    }

    @Override
    public Integer setValue(Integer star) {
        return this.star = star;
    }

    @Override
    public Material getLeft() {
        return material;
    }

    @Override
    public Integer getRight() {
        return star;
    }

    public List<Bonus> getBonus() {
        return material.getBonus(star);
    }

    public Collection<EffectInstance> getEffects() {
        Map<Effect, EffectInstance> map = Maps.newHashMap();
        for (Bonus bonus : getBonus()) {
            for (Pair<EffectInstance, Float> pair : bonus.addEffects()) {
                if (Cuisine.RAND.nextFloat() < pair.getRight()) {
                    EffectInstance effect = pair.getLeft();
                    if (map.containsKey(effect.getPotion())) {
                        map.get(effect.getPotion()).combine(effect);
                    } else {
                        map.put(effect.getPotion(), effect);
                    }
                }
            }
        }
        return map.values();
    }
}
