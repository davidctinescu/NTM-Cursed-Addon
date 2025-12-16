package com.hbm.inventory.recipes;

import com.hbm.inventory.recipes.GasCentrifugeRecipes.PseudoFluidType;
import net.minecraft.item.ItemStack;

//Stupid ntm pseudo fluid types has their constructor package-private. Solution? Make a class in the same package!
public class LeafiaPseudoFluidType extends PseudoFluidType {
	public LeafiaPseudoFluidType(String name,int fluidConsumed,int fluidProduced,PseudoFluidType outputFluid,boolean isHighSpeed,ItemStack... output) {
		super(name,fluidConsumed,fluidProduced,outputFluid,isHighSpeed,output);
	}
}
