package com.leafia.contents.machines.reactors.lftr.processing.separator;

import com.hbm.api.energymk2.IEnergyHandlerMK2;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.modules.machine.ModuleMachineBase;
import com.leafia.contents.machines.reactors.lftr.processing.separator.recipes.SaltSeparatorRecipes;
import net.minecraftforge.items.ItemStackHandler;

public class SaltSeparatorModule extends ModuleMachineBase {
	public SaltSeparatorModule(int index,IEnergyHandlerMK2 battery,ItemStackHandler inventory) {
		super(index,battery,inventory);
		this.inputSlots = new int[0];
		this.outputSlots = new int[0];
		this.inputTanks = new FluidTankNTM[2];
		this.outputTanks = new FluidTankNTM[3];
	}
	@Override
	public SaltSeparatorRecipes getRecipeSet() {
		return SaltSeparatorRecipes.INSTANCE;
	}
	public SaltSeparatorModule fluidInput(FluidTankNTM a,FluidTankNTM b) { inputTanks[0] = a; inputTanks[1] = b; return this; }
	public SaltSeparatorModule fluidOutput(FluidTankNTM a, FluidTankNTM b, FluidTankNTM c) { outputTanks[0] = a; outputTanks[1] = b; outputTanks[2] = c; return this; }
}
