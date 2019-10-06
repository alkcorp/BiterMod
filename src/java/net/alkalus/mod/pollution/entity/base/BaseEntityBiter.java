package net.alkalus.mod.pollution.entity.base;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import net.alkalus.api.objects.Logger;
import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.minecraft.BlockPos;
import net.alkalus.mod.logic.AttackPath;
import net.alkalus.mod.logic.AttackPathEntity;
import net.alkalus.mod.logic.EntityAISwarm;
import net.alkalus.mod.logic.Hive;
import net.alkalus.mod.pollution.tile.TileBiterHiveBase;
import net.alkalus.mod.pollution.util.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.Chunk;

public class BaseEntityBiter extends EntityMob implements IRangedAttackMob {

	private float mEntityWidth = -1.0F;    
	private float mEntityHeight;    
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
	private EntityAISwarm aiSwarm = new EntityAISwarm(this);

	public BaseEntityBiter(World aWorld) {
		super(aWorld);      
		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(1, new EntityAISwimming(this));
		//this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		//this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		//this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
		//this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(1, aiSwarm);
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));

		this.setSize(0.6F, 1.8F);
		updateSize();
		if (aWorld != null && !aWorld.isRemote)
		{
			this.setCombatTask();
		}
	}

	public void setHive(Hive aHive) {
		if (mHive == null || !mHive.matches(aHive)) {
			this.mHive = aHive;
			aiSwarm.setHive(mHive);    		
		}
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue() * MathUtils.randFloat(1.2f, 1.8f));
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
	}

	protected void entityInit() {
		super.entityInit();		
		this.getDataWatcher().addObject(12, Byte.valueOf((byte) MathUtils.randInt(0, 1))); // Spitter
		this.getDataWatcher().addObject(13, Byte.valueOf((byte) ((MathUtils.randInt(0, 10) < 9) ? 0 : 1))); // Gigantic
		this.getDataWatcher().addObject(14, Byte.valueOf((byte) 0)); // Raiding
		this.getDataWatcher().addObject(15, Byte.valueOf((byte) 0)); // level
	}

	public boolean attackEntityAsSkeleton(Entity p_70652_1_) {
		if (super.attackEntityAsMob(p_70652_1_)) {
			if (this.getBiterLevel() >= 1 && p_70652_1_ instanceof EntityLivingBase) {
				((EntityLivingBase) p_70652_1_).addPotionEffect(
						new PotionEffect(Potion.wither.id, 200)
						);
			}

			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue() {
		int i = super.getTotalArmorValue() + 2 + this.getBiterTier();
		return i;
	}

	private int getBiterTier() {
		return mHive != null ? mHive.getTier() : 1;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled()
	{
		return true;
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild()
	{
		return false;
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer p_70693_1_)
	{
		if (this.isLarge())
		{
			this.experienceValue = (int)((float)this.experienceValue * 2.5F);
		}

		return super.getExperiencePoints(p_70693_1_);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		if (getBiterTarget() != null) {
			getNavigator().setPath(AttackPathEntity.createEntityPath(this, getBiterTarget()), 1.5D);
		}
		super.onLivingUpdate();
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
	{
		if (!super.attackEntityFrom(p_70097_1_, p_70097_2_))
		{
			return false;
		}
		else
		{
			EntityLivingBase entitylivingbase = this.getAttackTarget();

			if (entitylivingbase == null && this.getEntityToAttack() instanceof EntityLivingBase)
			{
				entitylivingbase = (EntityLivingBase)this.getEntityToAttack();
			}

			if (entitylivingbase == null && p_70097_1_.getEntity() instanceof EntityLivingBase)
			{
				entitylivingbase = (EntityLivingBase)p_70097_1_.getEntity();
			}      

			return true;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.getBiterTier() <= 0) {
			this.setBiterLevel(mHive.getTier());
		}
	}

	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		boolean flag = super.attackEntityAsMob(p_70652_1_);

		if (flag)
		{
			int i = this.worldObj.difficultySetting.getDifficultyId();

			if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3F)
			{
				p_70652_1_.setFire(2 * i);
			}
		}

		attackEntityAsSkeleton(p_70652_1_);       

		return flag;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.zombie.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.zombie.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.zombie.death";
	}

	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
	{
		this.playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	public void onKillEntity(EntityLivingBase p_70074_1_) {
		super.onKillEntity(p_70074_1_);        
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer p_70085_1_) {
		return false;
	}    

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_)
	{
		if (p_70103_1_ == 16)
		{
			this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
		}
		else
		{
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn()
	{
		return !this.isLeader();
	}

	public void setSizeBasedOnIsSpitter(boolean isSpitter)
	{
		this.setBiterSize(isSpitter ? 0.5F : 1.0F);
	}

	/**
	 * Sets the width and height of the entity. Args: width, height
	 */
	protected final void setSize(float aWidth, float aHeight)
	{
		boolean flag = this.mEntityWidth > 0.0F && this.mEntityHeight > 0.0F;
		this.mEntityWidth = aWidth;
		this.mEntityHeight = aHeight;
		if (!flag) {
			this.setBiterSize(1.0F);
		}
	}

	protected final void setBiterSize(float p_146069_1_)
	{
		super.setSize(this.mEntityWidth * p_146069_1_, this.mEntityHeight * p_146069_1_);
	}

	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask()
	{
		this.tasks.removeTask(this.aiAttackOnCollide);
		this.tasks.removeTask(this.aiArrowAttack);

		if (this.isSpitter()){
			this.tasks.addTask(4, this.aiArrowAttack);
		}
		else
		{
			this.tasks.addTask(4, this.aiAttackOnCollide);
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	{
		EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
		entityarrow.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

		if (i > 0)
		{
			entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
		}

		if (j > 0)
		{
			entityarrow.setKnockbackStrength(j);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.isSpitter())
		{
			entityarrow.setFire(100);
		}

		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entityarrow);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound aNBT) {
		super.readEntityFromNBT(aNBT);     
		if (aNBT.hasKey("isLeader")){
			if (aNBT.getBoolean("isLeader")) {
				this.mLeaderEntity = this;        		
			}
		}
		if (aNBT.hasKey("isGigantic")){
			this.setLarge(aNBT.getBoolean("isGigantic"));
		}
		if (aNBT.hasKey("isSpitter")){
			this.setSpitter(aNBT.getBoolean("isSpitter"));
		}
		if (aNBT.hasKey("isRaiding")){
			this.setRaiding(aNBT.getBoolean("isRaiding"));
		}        
		if (aNBT.hasKey("combatLevel")){
			this.setBiterLevel(aNBT.getInteger("combatLevel"));
		}        
		if (aNBT.hasKey("mLeaderEntityUUID")) {
			mLeaderEntityUUID = aNBT.getString("mLeaderEntityUUID");
		}        
		this.setCombatTask();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound aNBT) {
		super.writeEntityToNBT(aNBT);
		aNBT.setBoolean("isGigantic", this.isLarge());
		aNBT.setBoolean("isSpitter", this.isSpitter());
		aNBT.setBoolean("isRaiding", this.isRaiding());
		aNBT.setInteger("combatLevel", this.getBiterLevel());
		aNBT.setBoolean("isSpitter", this.isSpitter());
		aNBT.setBoolean("isLeader", this.isLeader());
		aNBT.setString("mLeaderEntityUUID", mLeaderEntityUUID);
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_)
	{
		super.setCurrentItemOrArmor(p_70062_1_, p_70062_2_);

		if (!this.worldObj.isRemote && p_70062_1_ == 0)
		{
			this.setCombatTask();
		}
	}

	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
	{
		float f = this.worldObj.func_147462_b(this.posX, this.posY, this.posZ);
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);

		if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0)
		{
			this.tasks.addTask(4, this.aiAttackOnCollide);
			//this.setBiterLevel(1);
			this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
		}
		else
		{
			this.tasks.addTask(4, this.aiArrowAttack);
			this.addRandomArmor();
			this.enchantEquipment();
		}

		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ));

		if (this.getEquipmentInSlot(4) == null)
		{
			Calendar calendar = this.worldObj.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
			{
				this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
				this.equipmentDropChances[4] = 0.0F;
			}
		}

		this.addRandomArmor();
		this.enchantEquipment();

		if (this.getEquipmentInSlot(4) == null)
		{
			Calendar calendar = this.worldObj.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
			{
				this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
				this.equipmentDropChances[4] = 0.0F;
			}
		}

		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
		double d0 = this.rand.nextDouble() * 1.5D * (double)this.worldObj.func_147462_b(this.posX, this.posY, this.posZ);

		if (d0 > 1.0D)
		{
			this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
		}

		if (this.rand.nextFloat() < f * 0.05F)
		{
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
		}

		return p_110161_1_;
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	protected void addRandomArmor(){
		super.addRandomArmor();
		if (this.rand.nextFloat() < (this.worldObj.difficultySetting == EnumDifficulty.HARD ? 0.05F : 0.01F))
		{
			int i = this.rand.nextInt(3);

			if (i == 0)
			{
				this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
			}
			else
			{
				this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
			}
		}
		else {
			this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));        	
		}

	}

	protected void dropRareDrop(int p_70600_1_){
		if (this.getBiterLevel() == 1)
		{
			this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
		}
	}

	protected Item getDropItem()
	{
		return Items.arrow;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j;
		int k;

		if (this.isSpitter())
		{
			j = this.rand.nextInt(3 + p_70628_2_) - 1;

			for (k = 0; k < j; ++k)
			{
				this.dropItem(Items.coal, 1);
			}
		}
		else
		{
			j = this.rand.nextInt(3 + p_70628_2_);

			for (k = 0; k < j; ++k)
			{
				this.dropItem(Items.arrow, 1);
			}
		}

		j = this.rand.nextInt(3 + p_70628_2_);

		for (k = 0; k < j; ++k)
		{
			this.dropItem(Items.bone, 1);
		}
	}


	public boolean isLeader() {
		if (mLeaderEntity != null) {
			if (this.equals(mLeaderEntity)) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}    	
	}

	/**
	 * Get whether this biter is a Spitter.
	 */
	public boolean isSpitter() {
		return this.getDataWatcher().getWatchableObjectByte(12) == 1;
	}

	/**
	 * Set whether this biter is a Spitter.
	 */
	public void setSpitter(boolean isSpitter) {
		this.getDataWatcher().updateObject(12, Byte.valueOf((byte)(isSpitter ? 1 : 0)));
		//this.setSizeBasedOnIsSpitter(isSpitter);
	}

	/**
	 * Set whether this biter is a Gigantic.
	 */
	public boolean isLarge() {
		return isLeader() ? true : this.getDataWatcher().getWatchableObjectByte(13) == 1;
	}

	/**
	 * Get whether this biter is a Gigantic.
	 */
	public void setLarge(boolean aLarge) {
		this.isImmuneToFire = aLarge;    
		byte value = isLeader() ? 1 : Byte.valueOf((byte)(aLarge ? 1 : 0));        
		this.getDataWatcher().updateObject(13, value);
	}

	/**
	 * Get whether this biter is raiding.
	 */
	public boolean isRaiding() {
		return this.getDataWatcher().getWatchableObjectByte(14) == 1;
	}

	/**
	 * Get whether this biter is raiding.
	 */
	public void setRaiding(boolean aRaiding) {
		//Logger.WARNING("Adjusting Raid state: "+aRaiding+" | Leader: "+isLeader());
		this.getDataWatcher().updateObject(14, Byte.valueOf((byte)(aRaiding ? 1 : 0)));
	}

	/**
	 * Return this biter's level.
	 */
	public int getBiterLevel()
	{
		return this.dataWatcher.getWatchableObjectByte(15);
	}

	/**
	 * Set this biter's level.
	 */
	public void setBiterLevel(int aLevel)
	{
		this.getDataWatcher().updateObject(15, Byte.valueOf((byte)aLevel));
	}

	private void updateSize(){

		float aLevelMod = 1 + (getBiterLevel() * 0.1f);

		if (isLarge()) {
			if (isSpitter()) {
				this.setSize(0.74F*aLevelMod, 2.24F*aLevelMod);
			}
			else {
				this.setSize(0.81F*aLevelMod, 2.54F*aLevelMod);    			
			}
		}
		else {
			if (isSpitter()) {
				this.setSize(0.55F*aLevelMod, 1.65F*aLevelMod);    			
			}
			else {
				this.setSize(0.6F*aLevelMod, 1.8F*aLevelMod);    			
			}        
		}
	}


	private BaseEntityBiter mLeaderEntity;
	private String mLeaderEntityUUID = "";
	private Hive mHive;    

	public boolean setLeader(BaseEntityBiter aEntity) {
		if (mLeaderEntity == null || mLeaderEntity.isDead) {
			mLeaderEntity = aEntity;
			mLeaderEntityUUID = mLeaderEntity.getPersistentID().toString();
			return true;
		}   	
		return false;
	}

	public void doSwarmLogic(Hive aHive, AutoMap<EntityLiving> aSwarm) {

		if (aHive == null) {
			Logger.WARNING("No Hive Set, finding");
			mHive = findHive();
		}
		else if ((mHive == null || !aHive.matches(mHive))) {
			Logger.WARNING("No Hive Set, setting");
			mHive = aHive;
		}	
		if (mHive == null) {
			Logger.WARNING("No Hive Set, doing nothing");
			return;
		}

		AutoMap<BaseEntityBiter> mSwarm = new AutoMap<BaseEntityBiter>();
		for (Entity y : aSwarm) {
			if (y instanceof BaseEntityBiter) {
				mSwarm.put((BaseEntityBiter) y);
			}
		}		
		if (!mSwarm.isEmpty()) {
			setLeader(mSwarm);

			if (isLeader()) {				
				Logger.INFO("Leader Entity located at "+this.getPosition().getLocationString()+" | Is Spitter: "+this.isSpitter() + " | Target: "+(this.getBiterTarget() != null ? this.getBiterTarget().getCommandSenderName() : "None")+" | Raiding: "+isRaiding());
				this.setBiterSize(3f);

				if (isRaiding()) {

					if (this.entityToAttack == null) {

					}

				}
				else {	
					if (mHive.getTarget() != null) {
						BlockPos aTargetPos = mHive.getTarget();
						if (aTargetPos == null) {
							aTargetPos = getTarget(this);
						}
						if (aTargetPos != null) {
							moveToTarget(aTargetPos);
							if (withinTargetRange(aTargetPos)) {
								setRaiding(true);
							}
							else {
								setRaiding(false);
							}
						}
						else {
							setRaiding(false);
						}	
					}
					else {
						if (mHive.getEntityTarget() != null) {
							setTarget(mHive.getEntityTarget());				
						}
						else {
							Logger.WARNING("Trying to set Target from Entity side, probably bad.");
							Entity throwTest = getBiterTarget();
							if (throwTest instanceof EntityLiving) {
								mHive.setEntityTarget((EntityLiving) getBiterTarget());
								Logger.WARNING("Set Target from Entity side, went ok.");								
							}
						}
					}														
				}				
			}

			// Not Leader
			else {
				setAttackState(mLeaderEntity);
				if (isRaiding()) {
					if (entityToAttack == null && mHive.getEntityTarget() != null || !entityToAttack.equals(mHive.getEntityTarget())) {
						setTarget(mHive.getEntityTarget());						
					}
					else {
						// Do Attack
					}
				}
				else {
					moveToLeader(this);		
				}				
			}	


			doAttack();
		}




	}

	private EntityLiving findTargetEntity() {
		Chunk c = new Chunk(this.worldObj, this.chunkCoordX, this.chunkCoordZ);
		if (c != null && c.isChunkLoaded) {							
			int aToSkipCounter = MathUtils.balance((int) ((this.posY/16)-1), 0, 15);
			int aCounter = 0;
			List[] lists = c.entityLists;
			for (List<?> g : lists) {
				if (aCounter < aToSkipCounter) {
					continue;
				}
				else {
					List<?> f = lists[aCounter++];
					if (f != null) {
						for (int e=0;e<f.size();e++) {
							Object o = f.get(e);
							if (o != null) {
								if (o instanceof EntityLiving) {
									return (EntityLiving) o;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}











	public Entity getBiterTarget() {
		if (findPlayerToAttack() != null) {
			return findPlayerToAttack();			
		}
		else if (this.attackingPlayer != null) {
			return this.attackingPlayer;
		}
		else if (this.entityToAttack != null) {
			return this.entityToAttack;
		}
		else if (this.getEntityToAttack() != null) {
			return this.getEntityToAttack();			
		}
		else if (this.getAttackTarget() != null) {
			return this.getAttackTarget();
		}
		else if (this.getLastAttacker() != null) {
			return this.getLastAttacker();			
		}
		else {
			Entity y = findTargetEntity();
			if (y != null) {
				return y;
			}			
			return null;			
		}		
	}

	public boolean setBiterTarget(EntityLivingBase a) {
		setRevengeTarget(a);		
		return false;
	}










	/**
	 * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
	 * (Animals, Spiders at day, peaceful PigZombies).
	 */
	protected Entity findPlayerToAttack()
	{
		EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);
		return entityplayer;
	}

	public void setTarget(EntityLiving aEntity) {
		this.entityToAttack = (Entity) aEntity;
		this.setAttackTarget((EntityLivingBase) aEntity);
	}

	private void setAttackState(BaseEntityBiter mLeader) {
		if (mLeader.isRaiding()) {
			setRaiding(true);
			return;
		}
		setRaiding(false);	
	}

	private void doAttack() {
		if (isRaiding()) {
			//Logger.INFO("Does Hive have valid target? "+(mHive.getEntityTarget() != null));
			if (isLeader()) {
				if (this.getBiterTarget() != null) {
					if (this.getBiterTarget() instanceof EntityPlayer) {
						// Player is best
					}
					else {
						this.setTarget(getBiterTarget());
					}
				}
			}
			else {
				this.setTarget(this.mLeaderEntity.getBiterTarget());

			}			
			//this.attackEntityAsSkeleton(getAttackTarget());
		}		
	}

	private boolean withinTargetRange(BlockPos aPos) {
		if (aPos.isWithinRange(getPosition(), 16)) {
			return true;
		}
		return false;
	}

	public BlockPos getPosition() {
		return new BlockPos(this);
	}

	private Hive findHive() {
		if (worldObj != null) {
			if (!worldObj.isRemote) {				
				for (int x=-2;x<3;x++) {
					for (int z=-2;z<3;z++) {
						Chunk c = worldObj.getChunkFromChunkCoords(chunkCoordX+x, chunkCoordZ+z);
						if (c != null && c.isChunkLoaded) {
							Map y = c.chunkTileEntityMap;
							if (y != null && !y.isEmpty()) {
								for (Object o : y.values()) {
									if (o != null && o instanceof TileEntity) {
										if (o instanceof TileBiterHiveBase) {
											TileBiterHiveBase h = (TileBiterHiveBase) o;
											if (h != null) {
												Hive HH = h.getHive();
												if (HH != null) {
													return HH;
												}
											}
										}
									}
								}
							}
						}
					}	
				}				
			}
		}
		return null;
	}

	private void setLeader(AutoMap<BaseEntityBiter> mSwarm) {
		if (mLeaderEntity == null || mLeaderEntityUUID == null || mLeaderEntityUUID.length() <= 0) {
			if (mLeaderEntityUUID != null && mLeaderEntityUUID.length() > 0) {
				for (BaseEntityBiter y : mSwarm) {
					if (y.getPersistentID().toString().equals(mLeaderEntityUUID)) {
						setLeader(y);
						return;						
					}
				}
			}
			else {
				if (isLeader()) {
					setLeader(this);
				}
				else {
					for (BaseEntityBiter y : mSwarm) {
						if (y.isLeader()) {
							setLeader(y);
							break;					
						}
					}
					if (mLeaderEntity == null) {
						setLeader(this);
					}
				}
			}
		}	

	}

	private BlockPos getTarget(BaseEntityBiter aEntity) {

		AutoMap<Chunk> aChunks = new AutoMap<Chunk>();
		for (int x=-1;x<2;x++) {
			for (int z=-1;z<2;z++) {
				Chunk aCurrent = new Chunk(worldObj, aEntity.chunkCoordX+x, aEntity.chunkCoordZ+z);
				if (aCurrent != null && aCurrent.isChunkLoaded) {
					aChunks.add(aCurrent);
				}
			}
		}
		if (!aChunks.isEmpty()) {

			AutoMap<Block> targets = new AutoMap<Block>();
			targets.put(Blocks.log);
			targets.put(Blocks.log2);
			targets.put(Blocks.iron_door);
			targets.put(Blocks.trapdoor);
			targets.put(Blocks.wooden_door);
			targets.put(Blocks.bookshelf);
			targets.put(Blocks.bed);
			targets.put(Blocks.brewing_stand);
			targets.put(Blocks.enchanting_table);
			targets.put(Blocks.crafting_table);
			targets.put(Blocks.chest);
			targets.put(GregTech_API.sBlockMachines);
			targets.put(GregTech_API.sBlockCasings1);
			targets.put(GregTech_API.sBlockCasings2);
			targets.put(GregTech_API.sBlockCasings3);
			targets.put(GregTech_API.sBlockCasings4);
			targets.put(GregTech_API.sBlockOres1);

			for (Chunk c : aChunks) {
				int chunkX = c.xPosition << 4;
				int chunkZ = c.zPosition << 4;				
				for (int y=0;y<256;y++) {					
					for (int x=0;x<16;x++) {
						for (int z=0;z<16;z++) {
							BlockPos p = new BlockPos(chunkX+x, y, chunkZ+z);
							if (p != null && !p.isAir()) {
								Block b = p.getBlockAtPos();
								for (Block u : targets) {
									if (u.getClass().isInstance(b)) {
										return p;
									}																
								}
							}							
						}
					}

				}

			}
		}
		return this.getPosition();
	}

	private boolean moveToLeader(BaseEntityBiter aEntity) {
		if (aEntity != null) {
			//this.move
		}
		return false;
	}

	private boolean moveToTarget(BlockPos aPos) {
		AttackPath aPath = getPathToPosition(aPos);
		if (aPath != null) {

		}		
		return false;
	}

	private AttackPath getPathToPosition(BlockPos aPos) {
		return null;
	}

	public static String getUnlocalName() {
		return "BaseBiterMobEntity";
	}

	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	@Override
	public Entity getEntityToAttack() {
		return this.mHive != null ? this.mHive.getEntityTarget() : super.getEntityToAttack();		
	}

	@Override
	public void setTarget(Entity p_70784_1_) {
		try {
			this.setBiterTarget((EntityLivingBase) p_70784_1_);
		}
		catch (Throwable t) {

		}
		super.setTarget(p_70784_1_);
	}

	@Override
	public EntityLivingBase getAttackTarget() {
		return this.mHive != null ? this.mHive.getEntityTarget() : super.getAttackTarget();	
	}

	@Override
	public void onEntityUpdate() {
		if (this.getHive() == null) {
			this.setHive(findHive());
		}
		super.onEntityUpdate();
	}

	private Hive getHive() {
		return this.mHive;
	}

	@Override
	protected void despawnEntity() {
		if (this.mHive != null) {
			this.mHive.death(this);
		}
		super.despawnEntity();
	}

	@Override
	public String getCustomNameTag() {
		String name = "Champion ";
		if (isLeader()) {
			name += "";
		}
		else {
			if (isLarge()) {
				name += "Gigantic ";			
			}			
		}
		if (isSpitter()) {
			name += "Spitter";			
		}
		else {
			name += "Biter";			
		}
		name += " ["+this.getBiterLevel()+"]";
		return name;
	}

	@Override
	public boolean hasCustomNameTag() {
		return true;
	}

	@Override
	public boolean getAlwaysRenderNameTag() {
		return true;
	}

	@Override
	public void setRevengeTarget(EntityLivingBase aEntity) {			
		super.setRevengeTarget(this.mHive != null ? this.mHive.getEntityTarget() : aEntity);
	}

	@Override
	public EntityLivingBase getLastAttacker() {
		EntityLivingBase e = super.getLastAttacker();
		if (e instanceof EntityPlayer) {
			this.setBiterTarget(e);
			return e;
		}
		else {
			return this.mHive != null ? this.mHive.getEntityTarget() : e;
		}
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		if (this.mHive != null) {
			this.mHive.death(this);
		}
		super.onDeath(p_70645_1_);
	}

	@Override
	protected void kill() {
		if (this.mHive != null) {
			this.mHive.death(this);
		}
		super.kill();
	}

	@Override
	public void setDead() {
		if (this.mHive != null) {
			this.mHive.death(this);
		}
		super.setDead();
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer aPlayer) {
		aPlayer.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 20, 2));
		this.setBiterTarget(aPlayer);
		super.onCollideWithPlayer(aPlayer);
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		super.onStruckByLightning(p_70077_1_);
		EntityTNTPrimed g = new EntityTNTPrimed(worldObj);
		if (g != null) {
			g.copyLocationAndAnglesFrom(this);
			worldObj.spawnEntityInWorld(g);
			for (int i=0;i<MathUtils.randInt(10, 20);i++) {
				this.spawnExplosionParticle();
			}
			this.setDead();
		}
	}

}