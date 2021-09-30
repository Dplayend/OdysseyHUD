package com.dplayend.odysseyhud.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class TrinketCompatibility {
    public static boolean isModLoaded(String modID) {
        return FabricLoader.getInstance().isModLoaded(modID);
    }

    public static boolean isTrinketsLoaded() {
        return isModLoaded("trinkets");
    }

    public static boolean hasTrinketsItem(Player player, Item item) {
        if(isTrinketsLoaded()) {
            return TrinketsApi.getTrinketComponent(player).get().isEquipped(item);
        }
        return false;
    }
}
