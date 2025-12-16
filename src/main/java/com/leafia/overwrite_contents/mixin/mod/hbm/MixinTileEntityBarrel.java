package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.api.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.lib.DirPos;
import com.hbm.lib.ForgeDirection;
import com.hbm.lib.Library;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.tileentity.machine.TileEntityBarrel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(value = TileEntityBarrel.class)
public abstract class MixinTileEntityBarrel extends TileEntityMachineBase implements IFluidStandardTransceiverMK2 {
	public MixinTileEntityBarrel(int scount) {
		super(scount);
	}
	/**
	 * @author ntmleafia
	 * @reason only accept top and bottom
	 */
	@Overwrite(remap = false)
	public boolean hasCapability(Capability<?> capability,@Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (facing != null) {
				if (!facing.equals(EnumFacing.UP) && !facing.equals(EnumFacing.DOWN))
					return false;
			}
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	/**
	 * @author ntmleafia
	 * @reason only accept top and bottom
	 */
	@Overwrite(remap = false)
	protected DirPos[] getConPos() {
		return new DirPos[]{
				//new DirPos(pos.getX() + 1, pos.getY(), pos.getZ(), Library.POS_X),
				//new DirPos(pos.getX() - 1, pos.getY(), pos.getZ(), Library.NEG_X),
				new DirPos(pos.getX(), pos.getY() + 1, pos.getZ(), Library.POS_Y),
				new DirPos(pos.getX(), pos.getY() - 1, pos.getZ(), Library.NEG_Y)
				//new DirPos(pos.getX(), pos.getY(), pos.getZ() + 1, Library.POS_Z),
				//new DirPos(pos.getX(), pos.getY(), pos.getZ() - 1, Library.NEG_Z)
		};
	}

	@Override
	public boolean canConnect(FluidType type,ForgeDirection dir) {
		if (dir != ForgeDirection.UP && dir != ForgeDirection.DOWN)
			return false;
		return IFluidStandardTransceiverMK2.super.canConnect(type,dir);
	}
}
