package snownee.kiwi.ui;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ScreenHandler {
    //    public static final Map<ResourceLocation, Class<? extends Screen>> SCREENS = Maps.newHashMap();
    //
    //    public static synchronized void register(ResourceLocation id, Class<? extends Screen> screen)
    //    {
    //        SCREENS.put(id, screen);
    //    }
    //
    //    public static void drawScreen(GuiScreenEvent.DrawScreenEvent.Pre event)
    //    {
    //        SCREENS.containsValue(event.getGui().getClass());
    //    }
}
