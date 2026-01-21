package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.MixerRecipes;
import com.leafia.contents.AddonFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

//import com.leafia.contents.AddonFluids;

@Mixin(MixerRecipes.class)
public class MixinRecipesMixer {

    @Inject(
            method = "registerDefaults()V",
            at = @At("TAIL"),
            remap = false
    )
    private void injectAdditionalMixerRecipes(CallbackInfo ci) {
        try {
            // Access the static recipes HashMap using reflection
            Field recipesField = MixerRecipes.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            HashMap<FluidType, MixerRecipes.MixerRecipe[]> recipes =
                    (HashMap<FluidType, MixerRecipes.MixerRecipe[]>) recipesField.get(null);
            Class<?> mixerRecipeClass = Class.forName("com.hbm.inventory.recipes.MixerRecipes$MixerRecipe");
            Constructor<?> constructor = mixerRecipeClass.getDeclaredConstructor(int.class, int.class);
            constructor.setAccessible(true);
            Method setStack1Method = mixerRecipeClass.getDeclaredMethod("setStack1", FluidStack.class);
            Method setStack2Method = mixerRecipeClass.getDeclaredMethod("setStack2", FluidStack.class);
            Method setSolidMethod = mixerRecipeClass.getDeclaredMethod("setSolid", RecipesCommon.AStack.class);
            setStack1Method.setAccessible(true);
            setStack2Method.setAccessible(true);
            setSolidMethod.setAccessible(true);

            /*
            Object recipeObj = constructor.newInstance(1000, 50);

            setStack1Method.invoke(recipeObj, new FluidStack(Fluids.WATER, 500));
            setStack2Method.invoke(recipeObj, new FluidStack(Fluids.OIL, 500));
            setSolidMethod.invoke(recipeObj, new RecipesCommon.ComparableStack(Items.DIAMOND));

            // Cast to MixerRecipe and add to map
            MixerRecipe recipe = (MixerRecipe) recipeObj;
            recipes.put(Fluids.MY_CUSTOM_FLUID, new MixerRecipe[]{recipe});
            */

            Object cryolhe3 = constructor.newInstance(500,80);
            setStack1Method.invoke(cryolhe3, new FluidStack(Fluids.HELIUM4,500,5));
            setStack2Method.invoke(cryolhe3, new FluidStack(Fluids.CRYOGEL, 500));
            MixerRecipes.MixerRecipe cryohel3_r = (MixerRecipes.MixerRecipe) cryolhe3;
            recipes.put(AddonFluids.LIQUID_HE4,new MixerRecipes.MixerRecipe[]{cryohel3_r});

        } catch (Exception e) {
            System.err.println("[YourMod] Failed to inject mixer recipes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}