package com.dplayend.odysseyhud.mixin;

import com.dplayend.odysseyhud.trinkets.TrinketCompatibility;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Gui.class)
public abstract class MixinGui {
    private static final ResourceLocation TEXTURE = new ResourceLocation("odysseyhud", "textures/gui/widgets.png");
    @Shadow private int screenWidth;
    @Shadow private int screenHeight;

    @Shadow public abstract int getGuiTicks();

    private String timeString = "";
    Minecraft mc = Minecraft.getInstance();
    Color getClockColor;
    int iconLength;
    int weatherLength;
    int index = 0;

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void renderClock(PoseStack poseStack, float f, CallbackInfo info) {
        boolean found = true;
        int xOff = screenWidth/2 + 93;
        int yOff = screenHeight - 18;

        for (int n = 0; n <= 35; ++n) {
            if (mc.player.getInventory().getItem(n).is(Items.CLOCK)) { found = true; break; } else
            if (mc.player.getOffhandItem().getItem().equals(Items.CLOCK)) { found = true; } else
            if (TrinketCompatibility.hasTrinketsItem(mc.player, Items.CLOCK)) { found = true; } else { found = false; }
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, TEXTURE);
        if (found) {
            timeString = getClock();
            mc.gui.blit(poseStack, xOff, yOff, 0, 0, 50, 16);
            if (mc.level.isThundering()) { mc.gui.blit(poseStack, xOff + 6, yOff + 4, 122, 0, 8, 8); }
            mc.gui.blit(poseStack, xOff + 6, yOff + 4, iconLength + 50, weatherLength, 8, 8);
            mc.gui.getFont().drawShadow(poseStack, timeString, xOff + 18, yOff + 4, getClockColor.getRGB());
        }
        RenderSystem.disableBlend();
    }

    private String getClock() {
        long time = (int)mc.level.getDayTime();
        int hour = (int)((time / 1000L + 6L) % 24L);
        int minute = (int)(60L * (time % 1000L) / 1000L);
        String hourDisplay = "" + hour;
        String minuteDisplay = "" + minute;
        if (hour < 10) { hourDisplay = "0" + hour; }
        if (minute < 10) { minuteDisplay = "0" + minute; }

        Color sunColor = new Color(218, 165, 32, 255);
        Color moonColor = new Color(47, 65, 167, 255);
        if (hour >= 0) { getClockColor = moonColor; iconLength = (8+8*mc.level.getMoonPhase()); }
        if (hour >= 6 && hour < 19) { getClockColor = sunColor; iconLength = 0; }

        if (mc.level.isRaining() || mc.level.isThundering() || mc.level.getBiome(mc.player.getOnPos()).isColdEnoughToSnow(mc.player.getOnPos())) {
            index += 2*getGuiTicks();
            if (index >= 40*getGuiTicks()) { weatherLength -= 1; index = 0; }
            if (weatherLength <= 0) { weatherLength = 32; }
            getClockColor = new Color(189, 189, 189);
        }

        if (mc.level.isRaining() || mc.level.isThundering()) { iconLength = 80; }
        if (mc.level.getBiome(mc.player.getOnPos()).isColdEnoughToSnow(mc.player.getOnPos())) { iconLength = 88; }
        if (!mc.level.isRaining() && !mc.level.isThundering()) { weatherLength = 0; }

        return hourDisplay + ":" + minuteDisplay;
    }
}
