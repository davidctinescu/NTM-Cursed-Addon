package com.leafia.contents.machines.reactors.lftr.processing.separator;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.leafia.contents.machines.reactors.lftr.processing.separator.container.SaltSeparatorContainer;
import com.leafia.contents.machines.reactors.lftr.processing.separator.container.SaltSeparatorGUI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SaltSeparatorTE extends TileEntityMachineBase implements ITickable, IEnergyReceiverMK2, IControlReceiver, IGUIProvider {
	public SaltSeparatorModule module;
	public static long maxPower = 100_000;
	public long power = 0;
	public boolean didProcess = false;
	public FluidTank saltTank = new FluidTank(12000);
	public FluidTankNTM[] inputTanks = new FluidTankNTM[]{
			new FluidTankNTM(Fluids.NONE,12000),
			new FluidTankNTM(Fluids.NONE,12000)
	};
	public FluidTankNTM[] outputTanks = new FluidTankNTM[]{
			new FluidTankNTM(Fluids.NONE,12000),
			new FluidTankNTM(Fluids.NONE,12000),
			new FluidTankNTM(Fluids.NONE,12000)
	};
	public SaltSeparatorTE() {
		super(14);
		module = new SaltSeparatorModule(0,this,inventory)
				.fluidInput(inputTanks[0],inputTanks[1])
				.fluidOutput(outputTanks[0],outputTanks[1],outputTanks[2]);
				//.itemOutput(12,13,14,15);
	}

	@Override
	public void update() {

	}

	@Override
	public String getDefaultName() {
		return "tile.salt_separator.name";
	}
	AxisAlignedBB bb = null;
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		if(bb == null) {
			bb = new AxisAlignedBB(
					pos.getX() - 1,
					pos.getY(),
					pos.getZ() - 1,
					pos.getX() + 2,
					pos.getY() + 6,
					pos.getZ() + 2
			);
		}
		return bb;
	}

	@Override
	public long getPower() {
		return power;
	}

	@Override
	public void setPower(long l) {
		power = l;
	}

	@Override
	public long getMaxPower() {
		return maxPower;
	}

	@Override public boolean hasPermission(EntityPlayer player) { return this.isUseableByPlayer(player); }

	@Override
	public void receiveControl(NBTTagCompound data) {
		if(data.hasKey("index") && data.hasKey("selection")) {
			int index = data.getInteger("index");
			String selection = data.getString("selection");
			if(index == 0) {
				this.module.recipe = selection;
				this.markChanged();
			}
		}
	}

	@Override
	public Container provideContainer(int i,EntityPlayer entityPlayer,World world,int i1,int i2,int i3) {
		return new SaltSeparatorContainer(entityPlayer.inventory,getCheckedInventory());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen provideGUI(int i,EntityPlayer entityPlayer,World world,int i1,int i2,int i3) {
		return new SaltSeparatorGUI(entityPlayer.inventory,this);
	}
}
