package com.leafia.init.recipes;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.GasCentrifugeRecipes;
import com.hbm.inventory.recipes.GasCentrifugeRecipes.PseudoFluidType;
import com.hbm.inventory.recipes.LeafiaPseudoFluidType;
import com.hbm.items.ModItems;
import com.leafia.contents.AddonFluids;
import com.llib.exceptions.LeafiaDevFlaw;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;

import static com.hbm.inventory.recipes.GasCentrifugeRecipes.PseudoFluidType.*;
import static com.hbm.inventory.recipes.GasCentrifugeRecipes.*;

public class AddonGasCentRecipes {
	public static class AddonPseudoFluidTypes {
		public static PseudoFluidType HEU233F6 = new LeafiaPseudoFluidType("HEU233F6",300,0,NONE,true,new ItemStack(ModItems.nugget_u233,2),new ItemStack(ModItems.nugget_u235,1),new ItemStack(ModItems.fluorite,1));
		public static PseudoFluidType MEU233F6 = new LeafiaPseudoFluidType("MEU233F6",200,100,HEU233F6,false,new ItemStack(ModItems.nugget_u233,1));
		public static PseudoFluidType LEU233F6 = new LeafiaPseudoFluidType("LEU233F6",300,200,MEU233F6,false,new ItemStack(ModItems.nugget_u233,1),new ItemStack(ModItems.fluorite,1));
		public static PseudoFluidType U233F6 = new LeafiaPseudoFluidType("U233F6",400,300,LEU233F6,false,new ItemStack(ModItems.nugget_u233,1));
	}
	public static Map<FluidStack, Object[]> gasCent;
	static {
		try {
			Field gasCentField = GasCentrifugeRecipes.class.getDeclaredField("gasCent");
			gasCentField.setAccessible(true);
			gasCent = (Map<FluidStack, Object[]>)gasCentField.get(null);
		} catch (Exception e) {
			throw new LeafiaDevFlaw(e);
		}
	}
	public static void register() {
		fluidConversions.put(AddonFluids.UF6_233, AddonPseudoFluidTypes.U233F6);

		gasCent.put(new FluidStack(1200, AddonFluids.UF6_233), new Object[] { new ItemStack[] {new ItemStack(ModItems.nugget_u233, 11), new ItemStack(ModItems.nugget_u235, 1), new ItemStack(ModItems.fluorite, 4)}, true, 4 });
	}
}
