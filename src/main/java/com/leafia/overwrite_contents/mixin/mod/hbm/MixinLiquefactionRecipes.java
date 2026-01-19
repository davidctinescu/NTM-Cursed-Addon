package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.inventory.recipes.LiquefactionRecipes;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.RecipesCommon;
import com.leafia.contents.AddonFluids;
import com.leafia.contents.AddonItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.init.Items;

import java.util.HashMap;

@Mixin(LiquefactionRecipes.class)
public class MixinLiquefactionRecipes {

    @Inject(
            method = "registerDefaults()V",
            at = @At("TAIL"),
            remap = false
    )
    private void injectAdditionalRecipes(CallbackInfo ci) {
        try {
            java.lang.reflect.Field recipesField = LiquefactionRecipes.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);
            HashMap<Object, FluidStack> recipes = (HashMap<Object, FluidStack>) recipesField.get(null);

//            recipes.put(new RecipesCommon.ComparableStack(Items.APPLE), new FluidStack(100, Fluids.WATER));
            recipes.put(new RecipesCommon.ComparableStack(AddonItems.anitrate_solid), new FluidStack(10, AddonFluids.ANITRATE));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}