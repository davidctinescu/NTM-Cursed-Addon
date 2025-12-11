package com.leafia.contents;

import com.hbm.handler.pollution.PollutionHandler;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FT_Polluting;
import com.hbm.render.misc.EnumSymbol;
import com.leafia.contents.fluids.AddonFluidType;
import com.leafia.contents.fluids.FluorideFluid;
import com.llib.exceptions.LeafiaDevFlaw;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.lang.reflect.Field;
import java.util.List;

import static com.hbm.inventory.fluid.Fluids.*;

public class AddonFluids {
	public static final List<FluidType> metaOrderPointer;
	static {
		Field metaField = null;
		try {
			metaField = Fluids.class.getDeclaredField("metaOrder");
			metaField.setAccessible(true);
			metaOrderPointer = (List<FluidType>)metaField.get(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new LeafiaDevFlaw(e);
		}
	}
	public static class AddonFF {
		public static Fluid fluoride = new FluorideFluid("fluoride").setDensity(1000);
		public static void init() {
			registerFluid(fluoride);
		}
		private static void registerFluid(Fluid fluid) {
			FluidRegistry.registerFluid(fluid);
			FluidRegistry.addBucketForFluid(fluid);
		}
		public static void setFromRegistry() {
			fluoride = FluidRegistry.getFluid("fluoride");
		}
	}
	public static FluidType FLUORIDE;
	//public static FluidType FLUORINE; oh boy fluorine already exists
	public static FluidType UF6_233;
	public static FluidType UF6_235;
	public static FluidType HOT_WATER;
	public static FluidType HOT_AIR;
	public static void init() {
		FLUORIDE = new AddonFluidType("FLUORIDE",0xd3d8b9,5,0,0,EnumSymbol.NONE).setTemp(500).addTraits(LIQUID,new FT_Polluting().release(PollutionHandler.PollutionType.POISON, POISON_EXTREME)).setFFNameOverride("fluoride");
		//FLUORINE = new FluidType("FLUORINE",0xc5b055,4,0,4,EnumSymbol.NOWATER).addTraits(GASEOUS);
		UF6_233 = new AddonFluidType("UF6_233",UF6);
		UF6_235 = new AddonFluidType("UF6_235",UF6);
		HOT_WATER = new AddonFluidType("HOT_WATER",WATER).setTemp(70);
		HOT_AIR = new AddonFluidType("HOT_AIR",AIR).setTemp(50);
	}
}
