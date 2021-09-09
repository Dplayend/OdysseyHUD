package com.dplayend.odysseyhud;

import com.dplayend.odysseyhud.proxy.ProxyCommon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "odysseyhud", name = "Odyssey HUD", version = "1.1", clientSideOnly = true, acceptedMinecraftVersions = "[1.12.2]")
public class OdysseyHUD {
    @Mod.Instance("odysseyhud")
    public static OdysseyHUD instance;
    @SidedProxy( clientSide = "com.dplayend.odysseyhud.proxy.ProxyClient", serverSide = "com.dplayend.odysseyhud.proxy.ProxyCommon")
    public static ProxyCommon proxy;

    public OdysseyHUD() {}

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