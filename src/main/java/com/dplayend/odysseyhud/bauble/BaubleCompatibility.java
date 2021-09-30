package com.dplayend.odysseyhud.bauble;

import baubles.api.BaubleType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

public class BaubleCompatibility {
    public BaubleCompatibility() {}

    public static Item CLOCK;
    public static boolean isModLoaded(String modID) { return Loader.isModLoaded(modID); }
    public static boolean isBaublesLoaded() { return isModLoaded("baubles"); }

    public static Object hasBaubles() {
        if(isBaublesLoaded()) {
            return new BaubleClock(BaubleType.TRINKET);
        }
        return false;
    }
}
