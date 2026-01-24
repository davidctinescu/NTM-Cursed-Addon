package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.CompressorRecipes;
import com.hbm.util.Tuple;
//import com.leafia.contents.AddonFluids;
import com.leafia.contents.AddonFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.HashMap;


/*
public void registerDefaults() {
        recipes.put(new Tuple.Pair(Fluids.PETROLEUM, 0), new CompressorRecipe(2000, new FluidStack(Fluids.PETROLEUM, 2000, 1), 20));
        recipes.put(new Tuple.Pair(Fluids.PETROLEUM, 1), new CompressorRecipe(2000, new FluidStack(Fluids.LPG, 1000, 0), 20));
        recipes.put(new Tuple.Pair(Fluids.BLOOD, 3), new CompressorRecipe(1000, new FluidStack(Fluids.HEAVYOIL, 250, 0), 200));
        recipes.put(new Tuple.Pair(Fluids.PERFLUOROMETHYL, 0), new CompressorRecipe(1000, new FluidStack(Fluids.PERFLUOROMETHYL, 1000, 1), 50));
        recipes.put(new Tuple.Pair(Fluids.PERFLUOROMETHYL, 1), new CompressorRecipe(1000, new FluidStack(Fluids.PERFLUOROMETHYL_COLD, 1000, 0), 50));
    }
 */

@Mixin(CompressorRecipes.class)
public class MixinRecipesCompressor {

    @Inject(
            method = "registerDefaults()V",
            at = @At("TAIL"),
            remap = false
    )
    private void injectAdditionalCompressorRecipes(CallbackInfo ci) {
        try {
            Field recipesField = CompressorRecipes.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            HashMap<Tuple.Pair<FluidType, Integer>, CompressorRecipes.CompressorRecipe> recipes =
                    (HashMap<Tuple.Pair<FluidType, Integer>, CompressorRecipes.CompressorRecipe>) recipesField.get(null);

            // recipe format
            // Format: Tuple.Pair(fluidType, pressureLevel) -> CompressorRecipe
            /* recipes.put(
                    new Tuple.Pair<>(Fluids.WATER, 0),              // Input (pressure level 0)
                    new CompressorRecipes.CompressorRecipe(
                            1000,                                   // Input amount (mb)
                            new FluidStack(Fluids.WATER, 1000, 1),  // Output (pressure level 1)
                            100                                     // Duration (ticks)
                    )
            ); */

            recipes.put(
                    new Tuple.Pair(Fluids.HELIUM4, 0),
                    new CompressorRecipes.CompressorRecipe(
                            1000,
                            new FluidStack(Fluids.HELIUM4, 100,1),
                            20
                    )
            );
            recipes.put(
                    new Tuple.Pair(Fluids.HELIUM4, 1),
                    new CompressorRecipes.CompressorRecipe(
                            1000,
                            new FluidStack(Fluids.HELIUM4, 1001,2),
                            40
                    )
            );
            recipes.put(
                    new Tuple.Pair(Fluids.HELIUM4, 2),
                    new CompressorRecipes.CompressorRecipe(
                            1000,
                            new FluidStack(Fluids.HELIUM4, 100,3),
                            60
                    )
            );
            recipes.put(
                    new Tuple.Pair(Fluids.HELIUM4, 3),
                    new CompressorRecipes.CompressorRecipe(
                            1000,
                            new FluidStack(Fluids.HELIUM4, 100,4),
                            80
                    )
            );
            recipes.put(
                    new Tuple.Pair(Fluids.HELIUM4, 4),
                    new CompressorRecipes.CompressorRecipe(
                            1000,
                            new FluidStack(Fluids.HELIUM4, 100,5),
                            100
                    )
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("[hbm] Failed to inject compressor recipes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}