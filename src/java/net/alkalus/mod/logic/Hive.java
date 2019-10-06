package net.alkalus.mod.logic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.alkalus.api.objects.Logger;
import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.minecraft.BlockPos;
import net.alkalus.mod.pollution.entity.base.BaseEntityBiter;
import net.alkalus.mod.pollution.tile.TileBiterHiveBase;
import net.alkalus.mod.pollution.util.MathUtils;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Hive {

	private final AutoMap<EntityLiving> mSwarm = new AutoMap<EntityLiving>();
	
	private final TileBiterHiveBase mTile;
	private final BlockPos mPos;	
	private BlockPos mTarget;
	
	private EntityLiving mEntityTarget;
	
	private final int mSwarmMaxSize;

	public Hive(TileBiterHiveBase tileBiterHiveBase) {
		Logger.WARNING("Creating Hive-Logic");
		this.mTile = tileBiterHiveBase;
		this.mPos = new BlockPos(mTile);
		Logger.WARNING("Location: "+mPos.getLocationString());
		this.mSwarmMaxSize = MathUtils.randInt(getTier()*MathUtils.randInt(30, 50), (getTier()/2)*MathUtils.randInt(100, 200));
		Logger.WARNING("Max swarm size: "+mSwarmMaxSize);
        this.theNearestAttackableTargetSorter = new Sorter(mPos);
		this.entityPicker = new Selector();
	}

	public void readFromNBT(NBTTagCompound aNBT) {
		
	}

	public void writeToNBT(NBTTagCompound aNBT) {
		
	}
	

	public boolean isActive() {
		return mTile != null ? mTile.isActive() : false;
	}
	
	public void updateSwarm() {
		//Logger.WARNING("Updating aggressive Swarm!");		
		for (EntityLiving e : mSwarm) {			
			if (e instanceof BaseEntityBiter) {
				BaseEntityBiter t = (BaseEntityBiter) e;
				t.doSwarmLogic(this, mSwarm);
				if (t.isLeader()) {
					if (mSwarm.size() >= (this.mSwarmMaxSize * 0.4f)) {
						t.setRaiding(true);							
					}		
					else {
						t.setRaiding(false);	
					}
				}
			}			
		}
		
		
		if (this.getLeader() != null && this.getLeader().isRaiding()) {
			Logger.WARNING("Leader is not null and we are raiding.");
			if (this.getEntityTarget() == null) {
				Logger.WARNING("Target entity is null, trying to set.");
				EntityLivingBase e = getValidEntityTarget();
				Logger.WARNING("Valid target found? "+(e != null));
				this.setEntityTarget((EntityLiving) e);
			}

			if (this.getEntityTarget() != null) {
				Logger.WARNING("HAS VALID TARGET | "+this.getEntityTarget().getCommandSenderName());	
				if (!(this.getEntityTarget() instanceof EntityPlayer)) {
					
				}
			}
			else if (this.getEntityTarget() == null) {
				Logger.WARNING("HAS NO VALID ENTITY AS TARGET");
				BaseEntityBiter e = this.getLeader();
				if (e != null) {
					if (e.getBiterTarget() != null) {						
						Logger.WARNING("Trying to set Target from Entity side, probably bad.");
						Entity throwTest = e.getBiterTarget();
						if (throwTest instanceof EntityLiving) {
							setEntityTarget((EntityLiving) throwTest);
							Logger.WARNING("Set Target from Entity side, went ok.");								
						}					
					}
				}
				// Try get Block Target
			}
		}
		
		if (mSwarm.size() <= (this.mSwarmMaxSize * 0.6f)) {
			growSwarm();
		}
		
	}

	public void growSwarm() {
		//Logger.WARNING("Growing Swarm!");
		if (mSwarm.isEmpty() || mSwarm.size() < mSwarmMaxSize) {
			int getRaidSize = (int) (mSwarmMaxSize * MathUtils.randFloat(0.8f, 1f));
			Logger.WARNING("Raid Size: "+getRaidSize);
			if (mSwarm.size() <= getRaidSize) {
				if (spawnBiter(this)) {
					//Logger.WARNING("Created Biter for swarm.");
				}				
			}
			if (mSwarm.size() >= getRaidSize) {
				//Logger.WARNING("Setting hive active! [1]");
				mTile.setActive(true);
				return;
			}
		}
		if (mSwarm.size() >= (mSwarmMaxSize)) {
			//Logger.WARNING("Setting hive active! [2]");
			mTile.setActive(true);
			return;
		}
	}

	public boolean matches(Hive mHive) {		
		return mPos.getUniqueIdentifier().equals(mHive.mPos.getUniqueIdentifier());
	}
	
	public void setTarget(BlockPos aPos) {
		mTarget = aPos;
	}
	
	public void setEntityTarget(EntityLiving aEntity) {
		mEntityTarget = aEntity;
	}
	
	public BlockPos getTarget() {
		return mTarget;
	}
	
	public EntityLivingBase getEntityTarget() {
		return mEntityTarget;
	}

	public byte getTier() {
		return mTile.getTier();
	}
	
	private static boolean registerBiterForSwarm(Hive aHive, EntityLiving y) {
		int aSize = aHive.mSwarm.size();
		int bSize = aSize;
		aHive.mSwarm.add(y);
		aSize = aHive.mSwarm.size();
		return aSize > bSize;
	}
	
	public static boolean spawnBiter(Hive aHive) {
		World world = aHive.mTile.getWorldObj();
		if(!world.isRemote){
			//Logger.WARNING("Trying to spawn biter. Swarm Size: "+aHive.mSwarm.size());
			BaseEntityBiter biter = (BaseEntityBiter) spawnBiterEntity(aHive);
			boolean didRegister = false;
			if (biter != null) {
				biter.setHive(aHive);
				didRegister = registerBiterForSwarm(aHive, biter);			
			}		
			//Logger.WARNING("Valid? "+(biter != null)+", did register? "+didRegister);
			if (didRegister) {
				//Logger.WARNING("Added biter to swarm, trying to add to world.");
				boolean didSpawn = world.spawnEntityInWorld(biter);	
				//Logger.WARNING("Success? "+didSpawn);	
				return didSpawn;
			}
			else {
				Logger.WARNING("Failed to generate biter.");
				return false;				
			}
		}
		return false;
	}
	
	private static EntityLiving spawnBiterEntity(Hive aHive) {
		return spawnEntity(aHive, BaseEntityBiter.getUnlocalName());
	}
	
	private static EntityLiving spawnEntity(Hive aHive, String aEntityName) {
		World world = aHive.mPos.world;
		BlockPos aPos = aHive.mPos;
		if (!world.isRemote) {
			//Logger.WARNING("Spawning on server side");
		    String fullEntityName = aEntityName; // put string for your entity's name here
		    if (EntityList.stringToClassMapping.containsKey(fullEntityName)) {
				double x = aPos.xPos;
				double y = aPos.yPos + 1;
				double z = aPos.zPos;
				x = x + MathUtils.randInt(-2, 2);
				y = y + MathUtils.randInt(0, 1);
				z = z + MathUtils.randInt(-2, 2);				
	            Entity conjuredEntity = createEntityByName(fullEntityName, world, aHive);
	            if (conjuredEntity != null) {
	            	conjuredEntity.setPosition(x, y, z); // put the location here that you want
		    		if (world.spawnEntityInWorld(conjuredEntity)) {
				    	//Logger.WARNING("Entity created");
				    	if (conjuredEntity instanceof EntityLiving) {
			    			return (EntityLiving) conjuredEntity;			    		
				    	}
		    		}
		    		else {
				    	//Logger.WARNING("Entity not created");
		    		}
	            }	    		
		    }
		    else {
		    	//Logger.WARNING("Entity not found");
		    	//for (Object i : EntityList.stringToClassMapping.keySet()) {
			    	//Logger.WARNING("Found: "+i.toString());		    		
		    	//}
		    } 
		}
		return null;
	}
	
	/**
     * Create a new instance of an entity in the world by using the entity name.
     */
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public static Entity createEntityByName(
			String aEntityName, World aWorld, Hive aHive
	) {
		Entity entity = null;
		try {
			Class oclass = (Class) EntityList.stringToClassMapping.get(
					aEntityName
			);
			if (oclass != null) {
				entity = (Entity) oclass.getConstructor(new Class[]{
						World.class
				}).newInstance(new Object[]{
						aWorld
				});
			}
		}
		catch (Exception exception) {
			try {
				Class oclass = (Class) EntityList.stringToClassMapping.get(
						aEntityName
				);
				if (oclass != null) {
					entity = (Entity) oclass.getConstructor(new Class[]{
							World.class, Hive.class
					}).newInstance(new Object[]{
							aWorld, aHive
					});
				}
			}
			catch (Exception t) {

			}
		}

		return entity;
	}
	
	public BaseEntityBiter getLeader() {
		AutoMap<BaseEntityBiter> aBiters = new AutoMap<BaseEntityBiter>();
		for (EntityLiving g : mSwarm) {
			if (g instanceof BaseEntityBiter) {
				if (((BaseEntityBiter) g).isLeader()) {
					return (BaseEntityBiter) g;
				}
				else {
					aBiters.put((BaseEntityBiter) g);
				}
			}
		}
		if (!aBiters.isEmpty()) {
			BaseEntityBiter b = aBiters.get(MathUtils.randInt(0, (aBiters.size()-1)));
			b.setLeader(b);
			return getLeader();
		}
		return null;
		
	}
	
	public EntityLivingBase getValidEntityTarget() {
		EntityLivingBase g = getEntityTarget();
		if (g == null) {
			Logger.WARNING("Trying to set Hive Target Via AI task [2]");
			double d0 = 64;
			List<?> list2 = getLeader().worldObj.getEntitiesWithinAABBExcludingEntity(null, getLeader().boundingBox.expand(d0, 16.0D, d0), this.entityPicker);
			//List<?> list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand(d0, 16.0D, d0), this.targetEntitySelector);            
			Collections.sort(list2, this.theNearestAttackableTargetSorter);
			if (!list2.isEmpty()){
				for (int i=0;i<list2.size();i++) {
					EntityLiving g1 = (EntityLiving) list2.get(i);
					if (g1 != null && !BaseEntityBiter.class.isInstance(g1)) {
						Logger.WARNING("Setting Target: "+g1.toString());
						return g1;
					}
				}					
			}
		}	
		else if (g != null) {
			return g;
		}			
		return null;

	}
	
    private final Sorter theNearestAttackableTargetSorter;  
    private final Selector entityPicker;
   
    
    public static class Selector implements IEntitySelector {    	
    	@Override
    	public boolean isEntityApplicable(Entity aEntity)
        {
            return !(aEntity instanceof EntityLivingBase) ? false : true;
        }
    }
	
	public static class Sorter implements Comparator {
		private final BlockPos origin;

		public Sorter(BlockPos aOrigin) {
			this.origin = aOrigin;
		}

		public int compare(BlockPos p_compare_1_, BlockPos p_compare_2_) {
			double x0 = origin.distanceFrom(p_compare_1_);
			double x1 = origin.distanceFrom(p_compare_2_);

			return x0 < x1 ? -1 : (x0 > x1 ? 1 : 0);
		}

		public int compare(Object p_compare_1_, Object p_compare_2_) {
			return this.compare(
					(BlockPos) p_compare_1_, (BlockPos) p_compare_2_
			);
		}
	}

	public int size() {
		return mSwarm.size();
	}

	public boolean death(EntityLivingBase aEntity) {
		int aSize = mSwarm.size();
		int aSize2 = aSize;
		if (mSwarm.contains(aEntity)) {
			mSwarm.remove(aEntity);
			aSize = mSwarm.size();
		}
		return aSize > aSize2;
		
	}
	
	
}
