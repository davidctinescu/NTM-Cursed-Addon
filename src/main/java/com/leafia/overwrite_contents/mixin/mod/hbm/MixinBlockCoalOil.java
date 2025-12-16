package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.blocks.generic.BlockCoalOil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockCoalOil.class)
public class MixinBlockCoalOil {
	@Inject(method = "onBlockHarvested",at = @At(value = "HEAD"),require = 1,cancellable = true)
	void onOnBlockHarvested(World world,BlockPos pos,IBlockState state,EntityPlayer player,CallbackInfo ci) {
		ci.cancel();
	}
}
