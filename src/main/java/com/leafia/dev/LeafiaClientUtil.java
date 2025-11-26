package com.leafia.dev;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.util.RenderUtil;
import com.leafia.contents.gear.utility.ItemFuzzyIdentifier;
import com.leafia.contents.gear.utility.ItemFuzzyIdentifier.FuzzyIdentifierPacket;
import com.leafia.dev.custompacket.LeafiaCustomPacket;
import com.leafia.dev.gui.LCEGuiInfoContainer;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class LeafiaClientUtil {
	static boolean lastClicked = false;
	public static void renderTankInfo(@NotNull FluidTankNTM tank,@NotNull LCEGuiInfoContainer gui,int mouseX,int mouseY,int x,int y,int width,int height) {
		if (x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY) {
			List<String> list = new ArrayList();
			list.add(tank.getTankType().getLocalizedName());
			list.add(tank.getFill() + "/" + tank.getMaxFill() + "mB");
			if (tank.getPressure() != 0) {
				list.add(ChatFormatting.RED + "Pressure: " + tank.getPressure() + " PU");
			}

			if (Mouse.isButtonDown(0) && !lastClicked) {
				ItemStack item = Minecraft.getMinecraft().player.inventory.getItemStack();
				if (item != null && !item.isEmpty()) {
					if (item.getItem() instanceof ItemFuzzyIdentifier) {
						FuzzyIdentifierPacket packet = new FuzzyIdentifierPacket();
						packet.fluidRsc = tank.getTankType().getName();
						LeafiaCustomPacket.__start(packet).__sendToServer();
						Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("item.fuzzy_identifier.message",tank.getTankType().getLocalizedName()).setStyle(new Style().setColor(TextFormatting.YELLOW)));
					}
				}
			}
			lastClicked = Mouse.isButtonDown(0);
			tank.getTankType().addInfo(list);
			gui.drawFluidInfo((String[])list.toArray(new String[0]), mouseX, mouseY);
		}
	}
	public static void jeiFluidRenderInfo(FluidStack stack,List<String> info,int mx,int my,int x,int y,int width,int height) {
		mx--; my--;
		if (mx >= x && mx <= x+width && my >= y && my <= y+height) {
			info.add(stack.type.getLocalizedName());
			info.add(TextFormatting.GRAY+Integer.toString(stack.fill)+"mB");
			if (stack.pressure != 0) {
				info.add(ChatFormatting.RED + "Pressure: " + stack.pressure + " PU");
			}
			stack.type.addInfo(info);
		}
	}
	public static void jeiFluidRenderTank(List<FluidStack> stacks,FluidStack stack,int x,int y,int width,int height,boolean horizontal) {
		x++; y++;
		FluidType type = stack.type;
		int color = type.getTint();
		double r = ((color & 0xff0000) >> 16) / 255D;
		double g = ((color & 0x00ff00) >> 8) / 255D;
		double b = ((color & 0x0000ff) >> 0) / 255D;
		GL11.glColor3d(r, g, b);
		boolean wasBlendEnabled = RenderUtil.isBlendEnabled();
		if (!wasBlendEnabled) GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(type.getTexture());
		int maxFill = 0;
		for (FluidStack stack1 : stacks)
			maxFill = Math.max(maxFill,stack1.fill);
		int px = maxFill != 0 ? MathHelper.ceil(stack.fill * height / (float)maxFill) : 0;
		double minX = x;
		double maxX = x;
		double minY = y;
		double maxY = y;
		double minV = 1D - px / 16D;
		double maxV = 1D;
		double minU = 0D;
		double maxU = width / 16D;
		if (horizontal) {
			px = maxFill != 0 ? MathHelper.ceil(stack.fill * width / (float)maxFill) : 0;
			maxX += px;
			maxY += height;
			minV = 0D;
			maxV = height / 16D;
			minU = 1D;
			maxU = 1D - px / 16D;
		} else {
			maxX += width;
			minY += height - px;
			maxY += height;
		}
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(minX, maxY, 0).tex(minU, maxV).endVertex();
		bufferbuilder.pos(maxX, maxY, 0).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(maxX, minY, 0).tex(maxU, minV).endVertex();
		bufferbuilder.pos(minX, minY, 0).tex(minU, minV).endVertex();
		tessellator.draw();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (!wasBlendEnabled) GlStateManager.disableBlend();
	}
}
