package net.alkalus.mod.logic;

import java.util.Collections;
import java.util.List;

import net.alkalus.mod.pollution.entity.base.BaseEntityBiter;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAISwarm extends EntityAITarget
{
	/** Instance of EntityAINearestAttackableTargetSorter. */
	private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	/**
	 * This filter is applied to the Entity search.  Only matching entities will be targetted.  (null -> no
	 * restrictions)
	 */
	private final IEntitySelector targetEntitySelector;
	private EntityLivingBase targetEntity;
	private Hive mHive;

	public void setHive(Hive aHive) {
		mHive = aHive;
	}

	public EntityAISwarm(EntityCreature aAIUser)
	{
		this(aAIUser, null, 100, false, false);
	}

	public EntityAISwarm(EntityCreature aAIUser, Class<?> aTargetClass, int aTargetChance, boolean aShouldCheckSight)
	{
		this(aAIUser, aTargetClass, aTargetChance, aShouldCheckSight, false);
	}

	public EntityAISwarm(EntityCreature aAIUser, Class<?> aTargetClass, int aTargetChance, boolean aShouldCheckSight, boolean aNearbyOnly)
	{
		this(aAIUser, aTargetClass, aTargetChance, aShouldCheckSight, aNearbyOnly, (IEntitySelector)null);
	}

	public EntityAISwarm(EntityCreature aAIUser, Class<?> aTargetClass, int aTargetChance, boolean aShouldCheckSight, boolean aNearbyOnly, final IEntitySelector aEntitySelector)
	{
		super(aAIUser, aShouldCheckSight, aNearbyOnly);
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(aAIUser);
		this.setMutexBits(1);
		this.targetEntitySelector = new IEntitySelector()   {
			/**
			 * Return whether the specified entity is applicable to this filter.
			 */
			public boolean isEntityApplicable(Entity p_82704_1_)
			{
				return !(p_82704_1_ instanceof EntityLivingBase) ? false : (aEntitySelector != null && !aEntitySelector.isEntityApplicable(p_82704_1_) ? false : EntityAISwarm.this.isSuitableTarget((EntityLivingBase)p_82704_1_, false));
			}
		};
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (mHive != null) {
			//Logger.WARNING("Trying to set Hive Target Via AI task [1]");
			if (mHive.getEntityTarget() == null) {
				//Logger.WARNING("Trying to set Hive Target Via AI task [2]");
				double d0 = this.getTargetDistance();				
				List<?> list2 = this.taskOwner.worldObj.getEntitiesWithinAABBExcludingEntity(null, this.taskOwner.boundingBox.expand(d0, 16.0D, d0), this.targetEntitySelector);
				//List<?> list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand(d0, 16.0D, d0), this.targetEntitySelector);            
				Collections.sort(list2, this.theNearestAttackableTargetSorter);
				if (!list2.isEmpty()){
					for (int i=0;i<list2.size();i++) {
						EntityLiving g = (EntityLiving) list2.get(i);
						if (g != null && !BaseEntityBiter.class.isInstance(g) && !EntityPlayer.class.isInstance(g)) {
							//Logger.WARNING("Setting Target");
							mHive.setEntityTarget(g);							
						}
					}					
				}
			}	
			EntityLivingBase g = mHive.getEntityTarget();
			if (g != null) {
				//Logger.WARNING("Valid Target "+g.getCommandSenderName());
				return true;				
			}					
		}
		//Logger.WARNING("Failed trying to set Hive Target Via AI task");
		return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(mHive.getEntityTarget());
		super.startExecuting();
	}
}