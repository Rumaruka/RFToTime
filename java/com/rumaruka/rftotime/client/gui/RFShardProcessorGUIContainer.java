package com.rumaruka.rftotime.client.gui;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.gui.ClientPresenceTimeRenderer;
import com.cwelth.intimepresence.gui.CommonContainer;
import com.cwelth.intimepresence.network.SyncGUIOpened;
import com.cwelth.intimepresence.tileentities.CommonTE;
import com.rumaruka.rftotime.RFTTConfig;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RFShardProcessorGUIContainer<TE extends CommonTE, CNT extends CommonContainer> extends GuiContainer {

    public static final int WIDTH = 174;
    public static final int HEIGHT = 186;
    private RFShardProcessorTE te;
    private EntityPlayer player;

    private static ResourceLocation background;
    private static ResourceLocation tempBar;

    public RFShardProcessorGUIContainer(TE tileEntity, CNT container, String bg, EntityPlayer player) {
        super(container);
        this.player = player;
        te = (RFShardProcessorTE) tileEntity;
        background = new ResourceLocation(ModMain.MODID, bg);
        tempBar = new ResourceLocation(ModMain.MODID, "textures/gui/steamhammertemperature.png");
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    private String getTimeString(int presenceTime)
    {
        //60 * 60 * 20 = 72000 - 1 hour

        int timeLeft = presenceTime;
        //hours

        int hours = (int)(timeLeft / 72000);

        //minutes
        timeLeft = (timeLeft % 72000);
        int minutes = (int)(timeLeft / 1200);

        //seconds
        timeLeft = (timeLeft % 1200);
        int seconds = (int)(timeLeft / 20);

        return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, (timeLeft % 20));
    }

    public void drawDigitalString(String In)
    {
        GlStateManager.enableAlpha();
        Minecraft thisMc = Minecraft.getMinecraft();
        thisMc.getTextureManager().bindTexture(ClientPresenceTimeRenderer.DIGITAL_TEXTURE);

        int curX = guiLeft + WIDTH / 2 - 49;

        for(int i=0; i < In.length(); i++)
        {
            if(In.charAt(i) >= '0' && In.charAt(i) <= '9')
                this.drawTexturedModalRect(curX + 9*i, guiTop + 34, (In.charAt(i) - '0')*8, 0, 8, 16);
            else
                this.drawTexturedModalRect(curX + 9*i, guiTop + 34, 80, 0, 8, 16);
        }
        GlStateManager.disableAlpha();
    }

    protected void drawBurnTime(int x, int y, int burnTime, int maxTime)
    {
        if(maxTime == 0 || burnTime == 0)return;
        mc.getTextureManager().bindTexture(tempBar);
        int posY = burnTime * 16 / maxTime;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        drawTexturedModalRect(guiLeft + x, guiTop + y + 16 - posY, 0, 93 - posY, 16, posY);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawDigitalString(getTimeString(te.timeStored));
        drawBurnTime(61, 74, te.burnTime, te.burnTimeInitial);
        drawBurnTime(79, 74, te.pearlTime, 1000);
        drawBurnTime(97, 74, te.shardTime, 600);
        int energy = te.getEnergy();
        drawEnergyBar(energy);
        if(te.attachedTE == null)drawCenteredString(Minecraft.getMinecraft().fontRenderer, I18n.format("shardprocessor.notm"),guiLeft + WIDTH/2, guiTop + 58, 0xFFFFFFFF);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    private void drawEnergyBar(int energy){
        drawRect(guiLeft + 10, guiTop + 5, guiLeft + 112, guiTop + 15, 0xff555555);
        int percentage = energy * 100 / RFTTConfig.MAX_POWER_SHARDPROCESSOR;
        for (int i = 0 ; i < percentage ; i++) {
            drawVerticalLine(guiLeft + 10 + 1 + i, guiTop + 5, guiTop + 14, i % 2 == 0 ? 0xffff0000 : 0xff000000);
        }
    }


    @Override
    public void onGuiClosed() {
        ModMain.network.sendToServer(new SyncGUIOpened(te));
    }
}
