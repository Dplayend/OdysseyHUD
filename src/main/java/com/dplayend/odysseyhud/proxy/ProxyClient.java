package com.dplayend.odysseyhud.proxy;

import com.dplayend.odysseyhud.gui.GuiHUDClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyClient extends ProxyCommon {
    public ProxyClient() {}

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiHUDClient());
        super.postInit(event);
    }
}