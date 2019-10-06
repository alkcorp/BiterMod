package net.alkalus.mod.pollution.tile;

import java.util.List;

import net.alkalus.api.objects.Logger;
import net.alkalus.mod.logic.Hive;
import net.alkalus.mod.pollution.entity.base.BaseEntityBiter;
import net.alkalus.mod.pollution.util.BiterUtils;
import net.alkalus.mod.pollution.util.PollutionUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileBiterHiveBase extends TileEntity {

	private Hive hive;

	private long mTotalTickTime = 0;

	private byte level = 0;
	private int spawned = 0;
	private boolean active = false;

	public TileBiterHiveBase() {
		//Logger.WARNING("Creating Hive-Entity");
		//Logger.WARNING("Finished creating Hive-Entity");
	}

	@Override
	public void readFromNBT(NBTTagCompound aNBT) {
		super.readFromNBT(aNBT);
		if (aNBT.hasKey("level")) {
			level = aNBT.getByte("level");
		}
		if (aNBT.hasKey("spawned")) {
			spawned = aNBT.getInteger("spawned");
		}
		if (aNBT.hasKey("active")) {
			active = aNBT.getBoolean("active");
		}
		else {
			active = false;
		}
		if (hive != null) {
			hive.readFromNBT(aNBT);			
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound aNBT) {
		super.writeToNBT(aNBT);
		if (level > 0) {
			aNBT.setByte("level", level);
		}
		aNBT.setInteger("spawned", spawned);
		aNBT.setBoolean("active", active);
		hive.writeToNBT(aNBT);
	}

	private byte aTier = 0;

	public byte getTier() {
		return level > 0 ? level : 1;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.worldObj != null && !this.worldObj.isRemote) {
			
			// Tick Time
			mTotalTickTime++;
			
			// Set Hive
			if (hive == null) {
				hive = new Hive(this);
			}	
			
			//Set Tier
			if (mTotalTickTime % 5 == 0) {
				//Logger.WARNING("Ticking Hive 20%0");
				if (getLevel() < 1) {
					int pollution = PollutionUtils.getPollution(this);
					//if (pollution > 0) {
					byte aLevel = BiterUtils.getTierForPollution(pollution);
					if (aLevel > 0) {
						Logger.WARNING("Setting hive level: "+aLevel);
						setLevel(aLevel);
						level = aLevel;
					}
					//}
				}
			}	





			
			/**
			 * Main Logic Chunk
			 */

			if (mTotalTickTime % 5 == 0 && getLevel() > 0) {
				boolean active = hive.isActive();
				byte tier = hive.getTier();
				int divisor = active ? 50 : 10;
				if (hive != null) {
					if (level != tier) {
						this.setLevel(tier);
					}
					if (mTotalTickTime % divisor == 0 && getLevel() > 0) {
						if (level > 0) {				
							if (active) {
								hive.updateSwarm();
							}
							else {
								hive.growSwarm();
							}					
						}
					}
				}
				else {
					Logger.WARNING("Bad Hive, Level not set - "+(hive != null ? hive.getTier() : 0));
				}
			}
			
			/**
			 * Try re-populate hive with un-parented biters
			 */

			if (hive.size() <= 0) {
				List<?> g = this.worldObj.loadedEntityList;
				for (Object e : g) {
					if (e instanceof Entity) {
						if (e instanceof EntityLivingBase) {
							if (e instanceof BaseEntityBiter) {
								BaseEntityBiter b = (BaseEntityBiter) e;
								b.setHive(hive);							
							}
						}
					}
				}
			}
			
			//Logger.WARNING("Swarm Size: "+hive.size());
			//Logger.WARNING("Hive has block target? "+(this.hive.getTarget() != null));	
			//Logger.WARNING("Hive has entity target? "+(this.hive.getEntityTarget() != null));	
			if (this.hive.getEntityTarget() != null) {
				//Logger.WARNING("Found: "+(this.hive.getEntityTarget().getCommandSenderName()));	
			}
		}
	
}

private void setLevel(byte aLevel) {
	level = aLevel;

}

private byte getLevel() {
	return level;
}

@Override
public int getBlockMetadata() {
	return getLevel();
}

@Override
public Block getBlockType() {
	return super.getBlockType();
}

@Override
public boolean canUpdate() {
	return true;
}

public boolean isActive() {
	return active;
}

public void setActive(boolean b) {
	active = b;		
}

public Hive getHive() {
	return hive;
}

}
