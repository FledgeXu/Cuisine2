package snownee.cuisine;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import snownee.cuisine.api.CuisineAPI;
import snownee.cuisine.api.CuisineRegistries;
import snownee.cuisine.api.config.CuisineClientConfig;
import snownee.cuisine.api.config.CuisineCommonConfig;
import snownee.cuisine.api.registry.CuisineFood;
import snownee.cuisine.api.registry.Material;
import snownee.cuisine.api.registry.Spice;
import snownee.cuisine.cap.CuisineCapabilitiesInternal;
import snownee.cuisine.data.CuisineDataManager;
import snownee.cuisine.data.CuisineRecipeManager;
import snownee.cuisine.data.DeferredReloadListener;
import snownee.cuisine.data.DeferredReloadListener.LoadingStage;
import snownee.cuisine.data.research.ResearchData;
import snownee.cuisine.data.tag.CuisineNetworkTagManager;
import snownee.cuisine.impl.bonus.EffectsBonus;
import snownee.cuisine.impl.bonus.NewMaterialBonus;
import snownee.cuisine.impl.rule.CountRegistryRecipeRule;
import snownee.kiwi.AbstractModule;
import snownee.kiwi.Kiwi;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.util.Util;

@KiwiModule(name = "core")
@KiwiModule.Subscriber
public final class CoreModule extends AbstractModule {

    static CuisineDataManager<Material> materialManager;
    static CuisineDataManager<Spice> spiceManager;
    static CuisineDataManager<CuisineFood> foodManager;
    static CuisineRecipeManager recipeManager;

    static CuisineNetworkTagManager cuisineNetworkTagManager;

    static ResearchData researchData = new ResearchData();

    public CoreModule() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CuisineCommonConfig.spec);
        modEventBus.register(CuisineCommonConfig.class);
        if (FMLEnvironment.dist.isClient()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CuisineClientConfig.spec);
            modEventBus.register(CuisineClientConfig.class);
        }
    }

    @Override
    protected void preInit() {
        Cuisine.debug = Kiwi.isLoaded(Util.RL("kiwi:test"));

        CuisineRegistries.class.hashCode();
        cuisineNetworkTagManager = new CuisineNetworkTagManager();

        CuisineAPI.registerBonusAdapter("effect", new EffectsBonus.Adapter());
        CuisineAPI.registerBonusAdapter("new_material", new NewMaterialBonus.Adapter());

        CuisineAPI.registerRecipeRuleAdapter("material", new CountRegistryRecipeRule.Adapter<>(cuisineNetworkTagManager.getMaterials(), CuisineRegistries.MATERIALS));
        CuisineAPI.registerRecipeRuleAdapter("spice", new CountRegistryRecipeRule.Adapter<>(cuisineNetworkTagManager.getSpices(), CuisineRegistries.SPICES));
    }

    @Override
    protected void init(FMLCommonSetupEvent event) {
        CuisineCommonConfig.refresh();
        CuisineCapabilitiesInternal.register();
    }

    @Override
    protected void clientInit(FMLClientSetupEvent event) {
        CuisineClientConfig.refresh();
    }

    @Override
    protected void serverInit(FMLServerStartingEvent event) {
        event.getServer().getWorld(DimensionType.OVERWORLD).getSavedData().getOrCreate(() -> researchData, researchData.getName());

        LiteralArgumentBuilder<CommandSource> builder = CuisineCommand.init(event.getCommandDispatcher());
        event.getCommandDispatcher().register(builder);
    }

    @SubscribeEvent
    protected void serverInit(FMLServerAboutToStartEvent event) {
        if (materialManager == null) {
            materialManager = new CuisineDataManager("cuisine_material", CuisineRegistries.MATERIALS).setCallback(CoreModule::buildMaterialMap);
            spiceManager = new CuisineDataManager("cuisine_spice", CuisineRegistries.SPICES).setCallback(CoreModule::buildSpiceMap);
            foodManager = new CuisineDataManager("cuisine_food", CuisineRegistries.FOODS).setCallback(CoreModule::buildFoodMap);
            recipeManager = new CuisineRecipeManager("cuisine_recipe", CuisineRegistries.RECIPES);
        }
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.REGISTRY, materialManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.REGISTRY, spiceManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.REGISTRY, foodManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.TAG, cuisineNetworkTagManager);
        DeferredReloadListener.INSTANCE.listeners.put(LoadingStage.RECIPE, recipeManager);
        manager.addReloadListener(DeferredReloadListener.INSTANCE);
    }

    static Map<Item, Material> item2Material = Maps.newHashMap();
    static Map<Item, Spice> item2Spice = Maps.newHashMap();
    static Map<Fluid, Spice> fluid2Spice = Maps.newHashMap();
    static BiMap<Item, CuisineFood> item2Food = HashBiMap.create();
    static BiMap<Block, CuisineFood> block2Food = HashBiMap.create();

    private static void buildMaterialMap() {
        item2Material.clear();
        for (Material material : CuisineRegistries.MATERIALS.getValues()) {
            for (Item item : material.getItems()) {
                item2Material.put(item, material);
            }
            for (Tag<Item> tag : material.getItemTags()) {
                for (Item item : tag.getAllElements()) {
                    item2Material.put(item, material);
                }
            }
        }
        DeferredReloadListener.INSTANCE.complete(CuisineRegistries.MATERIALS);
    }

    private static void buildSpiceMap() {
        item2Spice.clear();
        fluid2Spice.clear();
        for (Spice spice : CuisineRegistries.SPICES.getValues()) {
            for (Item item : spice.getItems()) {
                item2Spice.put(item, spice);
            }
            for (Tag<Item> tag : spice.getItemTags()) {
                for (Item item : tag.getAllElements()) {
                    item2Spice.put(item, spice);
                }
            }
            for (Fluid fluid : spice.getFluids()) {
                fluid2Spice.put(fluid, spice);
            }
            for (Tag<Fluid> tag : spice.getFluidTags()) {
                for (Fluid fluid : tag.getAllElements()) {
                    fluid2Spice.put(fluid, spice);
                }
            }
        }
        DeferredReloadListener.INSTANCE.complete(CuisineRegistries.SPICES);
    }

    private static void buildFoodMap() {
        item2Food.clear();
        block2Food.clear();
        for (CuisineFood food : CuisineRegistries.FOODS.getValues()) {
            if (food.getItem() != null) {
                item2Food.put(food.getItem(), food);
            }
            if (food.getBlock() != null) {
                block2Food.put(food.getBlock(), food);
            }
        }
    }
}
