package com.leafia.init.recipes;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.fluid.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.PUREXRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.ModItems;
import com.leafia.contents.AddonFluids;
import com.leafia.contents.AddonItems;
import net.minecraft.item.ItemStack;

public class AddonPUREXRecipes {
    public static final PUREXRecipes INSTANCE = PUREXRecipes.INSTANCE;
    public static void register() {
        long woahPower = 1000L;
        String autoWoah = "autoswitch.woah";
        INSTANCE.register(
                (new GenericRecipe("purex.woah"))
                        .setup(100,woahPower)
                        .setNameWrapper("purex.recycle").setGroup(autoWoah, INSTANCE)
                        .inputItems(new RecipesCommon.AStack[]{new RecipesCommon.ComparableStack(ModItems.powder_fertilizer)})
                        .inputFluids(new FluidStack[]{
                                new FluidStack(Fluids.WATER, 450),
                                new FluidStack(AddonFluids.N2O, 550)
                        })
                        .outputItems(new ItemStack[]{
                                new ItemStack(AddonItems.anitrate_solid, 1),
                                null,
                                null,
                                null
                        })
                        .setIconToFirstIngredient()
        );
        //just follow this and you'll be goooood
    }
}
