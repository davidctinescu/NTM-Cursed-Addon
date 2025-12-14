package com.leafia.contents.machines.reactors.lftr.components.control;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockPipe;
import com.leafia.contents.AddonBlocks;
import net.minecraft.block.material.Material;

public class MSRControlExtension extends BlockPipe {
	public MSRControlExtension(Material mat,String s) {
		super(mat,s);
		ModBlocks.ALL_BLOCKS.remove(this);
		AddonBlocks.ALL_BLOCKS.add(this);
	}
}
