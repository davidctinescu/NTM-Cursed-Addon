package com.leafia.contents.gear.ntmfbottle;

import com.google.common.collect.ImmutableMap;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.leafia.contents.building.mixed.TextureAtlasSpriteHalf;
import com.leafia.contents.building.mixed.TextureAtlasSpriteHalf.HalfDirection;
import com.leafia.contents.control.fuel.nuclearfuel.LeafiaRodItem;
import com.leafia.dev.items.itembase.AddonItemBaked;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ItemNTMFBottle extends AddonItemBaked {
	private final ResourceLocation baseTextureLocation = new ResourceLocation("minecraft","items/potion_bottle_drinkable");
	protected Map<Integer,ModelResourceLocation> models = new HashMap<>();
	public ItemNTMFBottle(String s) {
		super(s);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		FluidType[] order = Fluids.getInNiceOrder();
		for (FluidType fluidType : order) {
			ModelLoader.setCustomModelResourceLocation(this, fluidType.getID(), getModelLocation(fluidType.getID()));
		}
	}
	public ModelResourceLocation getModelLocation(int meta) {
		if (!models.containsKey(meta))
			models.put(meta,new ModelResourceLocation(new ResourceLocation("leafia","drinkable_bottle_"+meta), "inventory"));
		return models.get(meta);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void bakeModel(ModelBakeEvent event) {
		try {
			IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));

			FluidType[] order = Fluids.getInNiceOrder();
			for (FluidType fluidType : order) {
				ImmutableMap.Builder<String,String> textures = ImmutableMap.builder();
				textures.put("layer0","leafia:items/"+this.getRegistryName()+"_overlay_"+fluidType.getID());
				textures.put("layer1",this.baseTextureLocation.toString());

				IModel retexturedModel = baseModel.retexture(textures.build());
				IBakedModel bakedModel = retexturedModel.bake(ModelRotation.X0_Y0,DefaultVertexFormats.ITEM,ModelLoader.defaultTextureGetter());
				event.getModelRegistry().putObject(getModelLocation(fluidType.getID()),bakedModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		FluidType[] order = Fluids.getInNiceOrder();
		for (FluidType fluidType : order) {
			ModelLoader.setCustomModelResourceLocation(this, fluidType.getID(), getModelLocation(fluidType.getID()));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerSprite(TextureMap map) {
		map.registerSprite(this.baseTextureLocation);
		FluidType[] order = Fluids.getInNiceOrder();
		for (FluidType fluidType : order) {
			ResourceLocation spriteLoc = new ResourceLocation("leafia", "items/"+this.getRegistryName()+"_overlay_"+fluidType.getID());
			TextureAtlasSpriteMask maskedSpr = new TextureAtlasSpriteMask(
					spriteLoc.toString(),
					new ResourceLocation("minecraft","items/potion_overlay"),fluidType.getTexture()
			);
			map.setTextureEntry(maskedSpr);
		}
	}
}
