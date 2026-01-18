package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.inventory.control_panel.ControlPanel;
import com.hbm.tileentity.IPersistentNBT;
import com.hbm.tileentity.machine.TileEntityControlPanel;
import com.leafia.dev.container_utility.LeafiaPacket;
import com.leafia.dev.container_utility.LeafiaPacketReceiver;
import com.leafia.overwrite_contents.interfaces.IMixinTileEntityControlPanel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityControlPanel.class)
public abstract class MixinTileEntityControlPanel extends TileEntity implements IMixinTileEntityControlPanel, LeafiaPacketReceiver, IPersistentNBT {
	@Shadow
	public abstract void readFromNBT(NBTTagCompound compound);

	@Shadow(remap = false)
	public ControlPanel panel;

	@Unique
	private String skinIdentifier = null;  // Changed from Block to String

	// Keep old method for backward compatibility
	@Override
	public Block getSkin() {
		return getSkinAsBlock();
	}

	// Keep old method for backward compatibility
	@Override
	public void setSkin(Block b) {
		setSkin(b.getRegistryName().toString() + ":0");
	}

	@Unique
	private Block getSkinAsBlock() {
		if (skinIdentifier == null) return null;

		try {
			String[] parts = skinIdentifier.split(":");
			if (parts.length >= 2) {
				String modId = parts[0];
				String blockName = parts[1];
				ResourceLocation rl = new ResourceLocation(modId, blockName);
				return Block.REGISTRY.getObject(rl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Unique
	@Override
	public IBlockState getSkinAsBlockState() {
		if (skinIdentifier == null) return null;

		try {
			String[] parts = skinIdentifier.split(":");
			if (parts.length >= 2) {
				String modId = parts[0];
				String blockName = parts[1];
				int meta = 0;

				if (parts.length >= 3) {
					try {
						meta = Integer.parseInt(parts[2]);
					} catch (NumberFormatException e) {
						meta = 0;
					}
				}

				ResourceLocation rl = new ResourceLocation(modId, blockName);
				Block block = Block.REGISTRY.getObject(rl);
				if (block != null) {
					// Get the proper IBlockState with metadata
					IBlockState state = block.getStateFromMeta(meta);
					return state;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Unique
	public void setSkin(String identifier) {
		this.skinIdentifier = identifier;
	}

	@Unique
	public String getSkinIdentifier() {
		return skinIdentifier;
	}

	@Override
	public String getPacketIdentifier() {
		return "CTRL_PNL";
	}

	@Override
	public void sendSkinPackets() {
		writeSkin(LeafiaPacket._start(this)).__sendToAffectedClients();
	}

	@Override
	public double affectionRange() {
		return 256;
	}

	@Override
	public void onReceivePacketLocal(byte key,Object value) {
		if (key == 0) {
			skinIdentifier = (String)value;
		}
	}

	@Override
	public void onReceivePacketServer(byte key,Object value,EntityPlayer plr) { }

	@Unique
	public LeafiaPacket writeSkin(LeafiaPacket packet) {
		packet.__write(0, skinIdentifier);
		return packet;
	}

	@Override
	public void onPlayerValidate(EntityPlayer plr) {
		writeSkin(LeafiaPacket._start(this)).__sendToClient(plr);
	}

	@Inject(method = "readFromNBT",at = @At(value = "HEAD"),require = 1)
	public void onReadFromNBT(NBTTagCompound compound,CallbackInfo ci) {
		skinIdentifier = null;
		if (compound.hasKey("skin")) {
			String skinData = compound.getString("skin");

			// Handle backward compatibility - old format was just block registry name
			if (!skinData.contains(":")) {
				// Old format, assume metadata 0
				skinIdentifier = skinData + ":0";
			} else {
				// New format with metadata
				skinIdentifier = skinData;
			}
		}
	}

	@Inject(method = "writeToNBT",at = @At(value = "HEAD"),require = 1)
	public void onWriteToNBT(NBTTagCompound compound,CallbackInfoReturnable<NBTTagCompound> cir) {
		if (skinIdentifier != null)
			compound.setString("skin", skinIdentifier);
	}

	@Unique
	private boolean destroyedByCreativePlayer = false;

	@Override
	public void setDestroyedByCreativePlayer() {
		destroyedByCreativePlayer = true;
	}

	@Override
	public boolean isDestroyedByCreativePlayer() {
		return destroyedByCreativePlayer;
	}

	@Override
	public void writeNBT(NBTTagCompound nbtTagCompound) {
		if (skinIdentifier != null)
			nbtTagCompound.setString("skin", skinIdentifier);
		nbtTagCompound.setTag("panel", panel.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void readNBT(NBTTagCompound nbtTagCompound) {
		skinIdentifier = null;
		if (nbtTagCompound.hasKey("skin")) {
			String skinData = nbtTagCompound.getString("skin");

			// Handle backward compatibility
			if (!skinData.contains(":")) {
				// Old format, assume metadata 0
				skinIdentifier = skinData + ":0";
			} else {
				// New format with metadata
				skinIdentifier = skinData;
			}
		}
		panel.readFromNBT(nbtTagCompound.getCompoundTag("panel"));
	}
}