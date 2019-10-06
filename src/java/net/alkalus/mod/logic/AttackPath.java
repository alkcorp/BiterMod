package net.alkalus.mod.logic;

import java.lang.reflect.Field;

import net.alkalus.mod.pollution.util.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;

public class AttackPath extends PathNavigate {

	private final PathNavigate mNavigatorBackup;
	private final EntityLiving mEntity;
	
	public AttackPath(EntityLiving aEntity) {	
		super(aEntity, aEntity.worldObj);
		mEntity = aEntity;
		mNavigatorBackup = aEntity.getNavigator();	
		setNavigator(mEntity);
	}
	
	private boolean setNavigator(EntityLiving aEntity) {
		Field navman = ReflectionUtils.getField(EntityLiving.class, "navigator");
		if (navman != null) {
			ReflectionUtils.setField(aEntity, navman, this);
			return true;
		}		
		return false;
	}

	@Override
	public PathEntity getPathToXYZ(double p_75488_1_, double p_75488_3_, double p_75488_5_) {
		return mNavigatorBackup.getPathToXYZ(p_75488_1_, p_75488_3_, p_75488_5_);
	}

	@Override
	public boolean tryMoveToXYZ(double p_75492_1_, double p_75492_3_, double p_75492_5_, double p_75492_7_) {
		return mNavigatorBackup.tryMoveToXYZ(p_75492_1_, p_75492_3_, p_75492_5_, p_75492_7_);
	}

	@Override
	public PathEntity getPathToEntityLiving(Entity aEntity) {		
		return AttackPathEntity.createEntityPath(mEntity, aEntity);
	}

	@Override
	public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
		return mNavigatorBackup.tryMoveToEntityLiving(p_75497_1_, p_75497_2_);
	}

	@Override
	public PathEntity getPath() {
		return mNavigatorBackup.getPath();
	}

	@Override
	public boolean noPath() {
		// TODO Auto-generated method stub
		return mNavigatorBackup.noPath();
	}
	
}
