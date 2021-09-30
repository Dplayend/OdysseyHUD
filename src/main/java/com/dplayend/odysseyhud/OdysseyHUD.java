package com.dplayend.odysseyhud;

import baubles.api.BaubleType;
import com.dplayend.odysseyhud.bauble.BaubleClock;
import com.dplayend.odysseyhud.bauble.BaubleCompatibility;
import com.dplayend.odysseyhud.proxy.ProxyCommon;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "odysseyhud", name = "OdysseyHUD", version = "1.2", acceptedMinecraftVersions = "[1.12.2]")
@Mod.EventBusSubscriber(modid = "odysseyhud")
public class OdysseyHUD {
    @SidedProxy( clientSide = "com.dplayend.odysseyhud.proxy.ProxyClient", serverSide = "com.dplayend.odysseyhud.proxy.ProxyCommon")
    public static ProxyCommon proxy;

    public OdysseyHUD() {}

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        if (BaubleCompatibility.isBaublesLoaded()) {
            ResourceLocation id = new ResourceLocation("minecraft", "clock");
            ModContainer minecraft = Loader.instance().getMinecraftModContainer();
            ModContainer active = Loader.instance().activeModContainer();
            Item clock = (Item)BaubleCompatibility.hasBaubles();

            try {
                Loader.instance().setActiveModContainer(minecraft);
                clock = clock.setRegistryName(id);
            } finally {
                Loader.instance().setActiveModContainer(active);
            }

            event.getRegistry().register(clock);
        } else { return; }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
