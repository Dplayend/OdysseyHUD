package com.dplayend.odysseyhud.gui;

import com.dplayend.odysseyhud.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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

public class GuiHUDClient {
    public static final ResourceLocation Widgets = new ResourceLocation("odysseyhud","textures/gui/widgets.png");
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontrenderer = mc.fontRenderer;
    public GuiHUDClient() {}

    @SubscribeEvent
    public void renderOverlay(Pre event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        EntityPlayerSP player = mc.player;

        if (type != ElementType.TEXT) { return; }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        this.getClock(player);
        GL11.glPopMatrix();
    }

    private void getClock(EntityPlayerSP player) {
        boolean found = true;
        boolean mustClock = ModConfig.Clock;
        if (mustClock) {
            found = false;
            for (int n = 0; n <= 35; ++n) { if (mc.player.inventory.getStackInSlot(n).getItem().equals(Items.CLOCK)) { found = true; break; } }
            if (this.mc.player.getHeldItemOffhand().getItem().equals(Items.CLOCK)) { found = true; }
        }
        ScaledResolution scaled = new ScaledResolution(mc);
        Color sunColour = new Color(218, 165, 32, 255);
        Color moonColour = new Color(47, 65, 167, 255);
        Color getStringColor = sunColour;
        long time = (int)mc.world.getWorldTime();
        int hour = (int)((time / 1000L + 6L) % 24L);
        int minute = (int)(60L * (time % 1000L) / 1000L);
        String hourDisplay = "" + hour;
        String minuteDisplay = "" + minute;
        String stringClock;
        if (hour < 10) { hourDisplay = "0" + hour; }
        if (minute < 10) { minuteDisplay = "0" + minute; }
        if (!found) { return; } else { stringClock = hourDisplay + ":" + minuteDisplay; }

        int stringWidth = fontrenderer.getStringWidth(stringClock) / 2;
        int posX = 0;
        int posY = -2;

        int iconLength = 0;
        int weatherLength = 0;
        int rain = 0;
        World world = mc.world;
        if (world.isRaining()) { rain = 8; }
        if (world.isThundering()) { weatherLength = 12; rain = 8; }
        if (!world.isRaining() && !world.isThundering()) { weatherLength = 24; rain = 0; }
        if (hour >= 0) { getStringColor = moonColour; iconLength = (8+8*world.getMoonPhase()); }
        if (hour >= 6 && hour < 19) { getStringColor = sunColour; iconLength = 0; }

        if (ModConfig.hPos == ModConfig.posX.MIDDLE) { posX = (scaled.getScaledWidth() / 2) + ModConfig.xOff; }
        if (ModConfig.hPos == ModConfig.posX.LEFT) { posX = 23 + ModConfig.xOff; }
        if (ModConfig.hPos == ModConfig.posX.RIGHT) { posX = scaled.getScaledWidth() - 23 + ModConfig.xOff; }
        if (ModConfig.vPos == ModConfig.posY.TOP) { posY = -2 + ModConfig.yOff;}
        if (ModConfig.vPos == ModConfig.posY.BOTTOM) { posY = scaled.getScaledHeight() - 14 + ModConfig.yOff; }

        mc.renderEngine.bindTexture(Widgets);
        mc.ingameGUI.drawTexturedModalRect(posX - 25, posY, 0, 0, 50, 16);
        mc.ingameGUI.drawTexturedModalRect((float) (posX - 19), (float) (posY + 4), iconLength + 50, rain, 8, 8);
        mc.ingameGUI.drawTexturedModalRect((float) (posX - 21), (float) (posY + 3), weatherLength + 122, 0, 12, 12);
        fontrenderer.drawStringWithShadow(stringClock, posX - stringWidth + 7, posY + 4, getStringColor.getRGB());
    }
}
