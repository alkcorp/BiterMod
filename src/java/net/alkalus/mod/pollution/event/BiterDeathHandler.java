package net.alkalus.mod.pollution.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.alkalus.mod.pollution.util.ReflectionUtils;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class BiterDeathHandler {

	private static final String mDragonClassName = "chylex.hee.entity.boss.EntityBossDragon";
	private static final boolean mHEE;
	private static final Class mHardcoreDragonClass;
	
	static {
		mHEE = ReflectionUtils.doesClassExist(mDragonClassName);
		mHardcoreDragonClass = (mHEE ? ReflectionUtils.getClass(mDragonClassName) : null);
	}

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {		
		//HEE Dragon
		if (mHEE) {}
		//Vanilla Dragon or any other dragon that extends it
		else {
			if (event.entityLiving instanceof EntityDragon) {}
		}		
	}

}
