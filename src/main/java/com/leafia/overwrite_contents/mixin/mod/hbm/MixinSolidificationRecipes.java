package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.inventory.recipes.SolidificationRecipes;
import com.leafia.contents.AddonFluids;
import com.leafia.contents.AddonItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SolidificationRecipes.class)
public class MixinSolidificationRecipes {

    @Inject(
            method = "registerDefaults()V",
            at = @At("TAIL"),
            remap = false
    )
    private void injectAdditionalRecipes(CallbackInfo ci) {
        try {
            java.lang.reflect.Method registerRecipeMethod = SolidificationRecipes.class.getDeclaredMethod(
                    "registerRecipe",
                    com.hbm.inventory.fluid.FluidType.class,
                    Integer.TYPE,
                    net.minecraft.item.ItemStack.class
            );
            registerRecipeMethod.setAccessible(true);

            net.minecraft.item.ItemStack anitrateSolid = new net.minecraft.item.ItemStack(AddonItems.anitrate_solid, 1);
            registerRecipeMethod.invoke(null, AddonFluids.ANITRATE, 100, anitrateSolid);

            registerRecipeMethod.setAccessible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}