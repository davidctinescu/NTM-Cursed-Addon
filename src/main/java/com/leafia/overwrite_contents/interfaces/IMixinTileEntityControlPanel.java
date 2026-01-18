package com.leafia.overwrite_contents.interfaces;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

//public interface IMixinTileEntityControlPanel {
//	Block getSkin();
//	void setSkin(Block b;
//	void sendSkinPackets();
//	void setSkin(String skinIdentifier);
//}

public interface IMixinTileEntityControlPanel {
	// Old methods for backward compatibility
	Block getSkin();
	void setSkin(Block b);

	// New methods for registry name support
	default void setSkin(String identifier) {
		// Default implementation for backward compatibility
	}

	default String getSkinIdentifier() {
		// Default implementation for backward compatibility
		return null;
	}

	default IBlockState getSkinAsBlockState() {
		// Default implementation - must be implemented in the mixin
		return null;
	}

	void sendSkinPackets();
}