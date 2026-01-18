package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.blocks.generic.BlockControlPanel;
import com.hbm.handler.threading.PacketThreading;
import com.hbm.inventory.control_panel.Control;
import com.hbm.inventory.control_panel.ControlEvent;
import com.hbm.inventory.control_panel.DataValueFloat;
import com.hbm.items.ModItems;
import com.hbm.main.MainRegistry;
import com.hbm.packet.toserver.NBTControlPacket;
import com.hbm.tileentity.IPersistentNBT;
import com.hbm.tileentity.machine.TileEntityControlPanel;
import com.leafia.overwrite_contents.interfaces.IMixinTileEntityControlPanel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(value = BlockControlPanel.class)
public abstract class MixinBlockControlPanel extends BlockContainer {
	protected MixinBlockControlPanel(Material materialIn) {
		super(materialIn);
	}


    @Override
	public void breakBlock(World worldIn,BlockPos pos,IBlockState state) {
		IPersistentNBT.breakBlock(worldIn,pos,state);
		super.breakBlock(worldIn,pos,state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer,ItemStack stack) {
		IPersistentNBT.onBlockPlacedBy(worldIn,pos,stack);
		super.onBlockPlacedBy(worldIn,pos,state,placer,stack);
	}

	@Override
	public void onBlockHarvested(World worldIn,BlockPos pos,IBlockState state,EntityPlayer player) {
		IPersistentNBT.onBlockHarvested(worldIn,pos,player);
		super.onBlockHarvested(worldIn,pos,state,player);
	}

	/**
	 * @author ntmleafia
	 * @reason just to make sure
	 */
	@Overwrite
	public Item getItemDropped(IBlockState state,Random rand,int fortune) {
		return Items.AIR;
	}

	/**
	 * @author ntmleafia
	 * @reason the laziness
	 * @author windowsbuild3r too
	 */
	@Overwrite
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			// without this attempting to press buttons will open the damned GUI
			if (playerIn.getHeldItem(hand).getItem() == ModItems.screwdriver || playerIn.getHeldItem(hand).getItem() == ModItems.screwdriver_desh) {
				ItemStack lol = playerIn.getHeldItem(hand.equals(EnumHand.MAIN_HAND) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
				if (lol.getItem() instanceof ItemBlock) {
					ItemBlock itemBlock = (ItemBlock) lol.getItem();
					Block block = itemBlock.getBlock();
					int meta = lol.getMetadata();
					IBlockState blockState = block.getStateFromMeta(meta);

					boolean nevermind = false;
					if (block instanceof IBlockProperties) {
						IBlockProperties properties = (IBlockProperties) block;
						if (!properties.getRenderType().equals(EnumBlockRenderType.MODEL))
							nevermind = true;
					}
					if (!nevermind) {
						if (worldIn.getTileEntity(pos) instanceof TileEntityControlPanel control) {
							IMixinTileEntityControlPanel mixin = (IMixinTileEntityControlPanel) control;

							// Get registry name and metadata
							ResourceLocation blockRegistryName = block.getRegistryName();
							if (blockRegistryName != null) {
								// Format: "modid:blockname:meta"
								String skinIdentifier = blockRegistryName.toString() + ":" + meta;
								mixin.setSkin(skinIdentifier);
								mixin.sendSkinPackets();

								// Get the correct display name for this specific variant
								String displayName = block.getLocalizedName();
								// For blocks with subtypes, try to get the unlocalized name
								String unlocalizedName = block.getTranslationKey();

								// Try to get variant-specific name
								try {
									// Some blocks have custom names based on metadata
									ItemStack displayStack = new ItemStack(block, 1, meta);
									if (displayStack.getItem() != null) {
										displayName = displayStack.getDisplayName();
									}
								} catch (Exception e) {
									// Fall back to default
								}

								// Notify player
//								if (playerIn instanceof EntityPlayerMP) {
//									ITextComponent message = new TextComponentString("Applied skin: " +
//											displayName +
//											(meta > 0 ? " (meta: " + meta + ")" : ""));
//									((EntityPlayerMP) playerIn).sendMessage(message);
//								}

								return true;
							}
						}
					}
				}
				FMLNetworkHandler.openGui(playerIn, MainRegistry.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		} else {
			if (worldIn.getTileEntity(pos) instanceof TileEntityControlPanel control) {
				Control ctrl = control.panel.getSelectedControl(playerIn.getPositionEyes(1), playerIn.getLook(1));
				if (ctrl != null) {
					ControlEvent evt = ControlEvent.newEvent("ctrl_press");
					evt.setVar("isSneaking", new DataValueFloat(playerIn.isSneaking()));
					NBTTagCompound dat = evt.writeToNBT(new NBTTagCompound());
					dat.setInteger("click_control", ctrl.panel.controls.indexOf(ctrl));
					PacketThreading.createSendToServerThreadedPacket(new NBTControlPacket(dat, pos));
					return true;
				}
			}
		}
		return false;
	}
}
