package com.leafia.init;

import com.leafia.contents.machines.reactors.lftr.processing.separator.recipes.SaltSeparatorRecipes;

import static com.hbm.inventory.recipes.loader.SerializableRecipe.recipeHandlers;

public class AddonSerializableRecipe {
	public static void onRegisterAllHandlers() {
		recipeHandlers.add(SaltSeparatorRecipes.INSTANCE);
	}
}
