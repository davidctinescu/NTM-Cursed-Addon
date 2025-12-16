package com.custom_hbm.effectNT;

import com.leafia.unsorted.ParticleRedstoneLight;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Random;

public class EffectNT {
	public static void effectNT(NBTTagCompound data) {
		World world = Minecraft.getMinecraft().world;
		if(world == null)
			return;
		EntityPlayer player = Minecraft.getMinecraft().player;
		Random rand = world.rand;
		String type = data.getString("type");
		double x = data.getDouble("posX");
		double y = data.getDouble("posY");
		double z = data.getDouble("posZ");

		if("rslight".equals(type)) {
			float size = data.getFloat("scale");
			ParticleRedstoneLight fx = new ParticleRedstoneLight(
					world,x,y,z,(size == 0) ? 1 : size,
					data.getDouble("mX"),
					data.getDouble("mY"),
					data.getDouble("mZ"),
					data.getFloat("red"),
					data.getFloat("green"),
					data.getFloat("blue")
			);
			Minecraft.getMinecraft().effectRenderer.addEffect(fx);
			return;
		}

		if("vanillaExt".equals(type)) {

			double mX = data.getDouble("mX");
			double mY = data.getDouble("mY");
			double mZ = data.getDouble("mZ");

			Particle fx = null;

			if("flame".equals(data.getString("mode"))) {
				fx = new ParticleFlame.Factory().createParticle(-1, world, x, y, z, mX, mY, mZ);
			}
			if("lava".equals(data.getString("mode"))) {
				world.spawnParticle(EnumParticleTypes.LAVA,x,y,z,mX,mY,mZ);
			}

			if("smoke".equals(data.getString("mode"))) {
				fx = new ParticleSmokeNormal.Factory().createParticle(-1, world, x, y, z, mX, mY, mZ);
			}

			if("volcano".equals(data.getString("mode"))) {
				fx = new ParticleSmokeNormal.Factory().createParticle(-1, world, x, y, z, mX, mY, mZ);
				float scale = 100;
				LCEParticleUtility.setSmokeScale((ParticleSmokeNormal)fx, scale);
				LCEParticleUtility.setMaxAge(fx, 200 + rand.nextInt(50));
				LCEParticleUtility.setNoClip(fx);
				LCEParticleUtility.setMotion(fx, rand.nextGaussian() * 0.2, 2.5 + rand.nextDouble(), rand.nextGaussian() * 0.2);
			}

			if("cloud".equals(data.getString("mode"))) {
				fx = new ParticleCloud.Factory().createParticle(-1, world, x, y, z, mX, mY, mZ);
			}

			if("reddust".equals(data.getString("mode"))) {
				fx = new ParticleRedstone.Factory().createParticle(-1, world, x, y, z, (float)mX, (float)mY, (float)mZ);
			}

			if("bluedust".equals(data.getString("mode"))) {
				fx = new ParticleRedstone.Factory().createParticle(-1, world, x, y, z, 0.01F, 0.01F, 1F);
			}

			if("greendust".equals(data.getString("mode"))) {
				fx = new ParticleRedstone.Factory().createParticle(-1, world, x, y, z, 0.01F, 0.5F, 0.1F);
			}

			if("largeexplode".equals(data.getString("mode"))) {


				fx = new ParticleExplosionLarge.Factory().createParticle(-1, world, x, y, z, data.getFloat("size"), 0.0F, 0.0F);
				float r = 1.0F - rand.nextFloat() * 0.2F;
				fx.setRBGColorF(1F * r, 0.9F * r, 0.5F * r);

				for(int i = 0; i < data.getByte("count"); i++) {
					ParticleExplosion sec = (ParticleExplosion)new ParticleExplosion.Factory().createParticle(-1, world, x, y, z, 0.0F, 0.0F, 0.0F);
					float r2 = 1.0F - rand.nextFloat() * 0.5F;
					sec.setRBGColorF(0.5F * r2, 0.5F * r2, 0.5F * r2);
					sec.multipleParticleScaleBy(i + 1);
					Minecraft.getMinecraft().effectRenderer.addEffect(sec);
				}
			}

			if("townaura".equals(data.getString("mode"))) {
				fx = new ParticleSuspendedTown.Factory().createParticle(-1, world, x, y, z, 0, 0, 0);
				float color = 0.5F + rand.nextFloat() * 0.5F;
				fx.setRBGColorF(0.8F * color, 0.9F * color, 1.0F * color);
				LCEParticleUtility.setMotion(fx, mX, mY, mZ);
			}

			if("blockdust".equals(data.getString("mode"))) {

				Block b = Block.getBlockById(data.getInteger("block"));
				int id = Block.getStateId(b.getDefaultState());
				fx = new ParticleBlockDust.Factory().createParticle(-1, world, x, y, z, mX, mY + 0.2, mZ, id);
				LCEParticleUtility.setMaxAge(fx, 10 + rand.nextInt(20));
			}

			if(fx != null)
				Minecraft.getMinecraft().effectRenderer.addEffect(fx);
			return;
		}

	}
}
