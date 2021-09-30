package com.dplayend.odysseyhud.gui;

import baubles.api.BaublesApi;
import com.dplayend.odysseyhud.ModConfig;
import com.dplayend.odysseyhud.OdysseyHUD;
import com.dplayend.odysseyhud.bauble.BaubleCompatibility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiHUD {
    public static final ResourceLocation Widgets = new ResourceLocation("odysseyhud","textures/gui/widgets.png");
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontrenderer = mc.fontRenderer;
    Color getClockColor;
    int iconLength;
    int weatherLength;
    int index = 0;
    int xOff;
    int yOff;

    public GuiHUD() {}

    @SubscribeEvent
    public void renderOverlay(Pre event) {
        RenderGameOverlayEvent.ElementType type = event.getType();

        if (type != ElementType.TEXT) { return; }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        this.getClock();
        GL11.glPopMatrix();
    }

    private void getClock() {
        boolean found = true;
        boolean mustClock = ModConfig.Clock;
        if (mustClock) {
            for (int n = 0; n <= 35; ++n) {
                if (mc.player.inventory.getStackInSlot(n).getItem().equals(Items.CLOCK)) { found = true; break; } else
                if (mc.player.getHeldItemOffhand().getItem().equals(Items.CLOCK)) { found = true; } else
                if (BaubleCompatibility.isBaublesLoaded()) { if (BaublesApi.isBaubleEquipped(mc.player, Items.CLOCK) >= 0) { found = true; } else { found = false; } } else { found = false; }
            }
        }
        ScaledResolution scaled = new ScaledResolution(mc);
        Color sunColour = new Color(218, 165, 32, 255);
        Color moonColour = new Color(47, 65, 167, 255);
        long time = (int)mc.world.getWorldTime();
        int hour = (int)((time / 1000L + 6L) % 24L);
        int minute = (int)(60L * (time % 1000L) / 1000L);
        String hourDisplay = "" + hour;
        String minuteDisplay = "" + minute;
        String stringClock;
        if (hour < 10) { hourDisplay = "0" + hour; }
        if (minute < 10) { minuteDisplay = "0" + minute; }
        if (!found) { return; } else { stringClock = hourDisplay + ":" + minuteDisplay; }

        World world = mc.world;
        if (hour >= 0) { getClockColor = moonColour; iconLength = (8+8*world.getMoonPhase()); }
        if (hour >= 6 && hour < 19) { getClockColor = sunColour; iconLength = 0; }

        if (world.isRaining() || world.isThundering() || world.getBiome(mc.player.getPosition()).isSnowyBiome()) {
            index += 2;
            if (index >= 40) { weatherLength -= 1; index = 0; }
            if (weatherLength <= 0) { weatherLength = 32; }
            getClockColor = new Color(189, 189, 189);
        }

        if (world.isRaining() || world.isThundering()) { iconLength = 80; }
        if (world.getBiome(mc.player.getPosition()).isSnowyBiome()) { iconLength = 88; }
        if (!world.isRaining() && !world.isThundering()) { weatherLength = 0; }

        if (ModConfig.hPos == ModConfig.posX.MIDDLE) { xOff = (scaled.getScaledWidth() / 2) + ModConfig.xOff; }
        if (ModConfig.hPos == ModConfig.posX.LEFT) { xOff = ModConfig.xOff; }
        if (ModConfig.hPos == ModConfig.posX.RIGHT) { xOff = scaled.getScaledWidth() + ModConfig.xOff; }
        if (ModConfig.vPos == ModConfig.posY.TOP) { yOff = ModConfig.yOff;}
        if (ModConfig.vPos == ModConfig.posY.BOTTOM) { yOff = scaled.getScaledHeight() + ModConfig.yOff; }

        mc.renderEngine.bindTexture(Widgets);
        mc.ingameGUI.drawTexturedModalRect(xOff, yOff, 0, 0, 50, 16);
        if (world.isThundering()) { mc.ingameGUI.drawTexturedModalRect(xOff+6, yOff+4, 122,0,8,8); }
        mc.ingameGUI.drawTexturedModalRect(xOff+6, yOff+4, iconLength+50, weatherLength, 8, 8);
        fontrenderer.drawStringWithShadow(stringClock, xOff + 18, yOff + 4, getClockColor.getRGB());
    }
}
