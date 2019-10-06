package net.alkalus.mod.logic;

import java.lang.reflect.Field;

import net.alkalus.mod.pollution.util.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;

public class AttackPathEntity extends PathEntity {	

	private AttackPathEntity(PathPoint[] aPathPointArray) {
		super(aPathPointArray);
	}
	
	private static Field mPrevious;
	
	public static AttackPathEntity createEntityPath(Entity aEntityActing, Entity aEntityTarget) {
		if (aEntityActing == null || aEntityTarget == null) {
			return null;
		}	
		int x1 = (int) aEntityActing.posX;
		int y1 = (int) aEntityActing.posY;
		int z1 = (int) aEntityActing.posZ;
		PathPoint aCurrentLocation = new PathPoint(x1, y1, z1);
		int x2 = (int) aEntityTarget.posX;
		int y2 = (int) aEntityTarget.posY;
		int z2 = (int) aEntityTarget.posZ;
		PathPoint aTargetLocation = new PathPoint(x2, y2, z2);
		return createEntityPath(aCurrentLocation, aTargetLocation);		
	}
	
	/**
     * Returns a new PathEntity for a given start and end point
     */
    public static AttackPathEntity createEntityPath(PathPoint p_75853_1_, PathPoint p_75853_2_) {
    	
    	if (mPrevious == null) {
    		mPrevious = ReflectionUtils.getField(PathPoint.class, "previous");
    	}
    	if (mPrevious != null) {
    		int i = 1;
            PathPoint pathpoint2;

            for (pathpoint2 = p_75853_2_; getPrevious(pathpoint2) != null; pathpoint2 = getPrevious(pathpoint2))
            {
                ++i;
            }

            PathPoint[] apathpoint = new PathPoint[i];
            pathpoint2 = p_75853_2_;
            --i;

            for (apathpoint[i] = p_75853_2_; getPrevious(pathpoint2) != null; apathpoint[i] = pathpoint2)
            {
                pathpoint2 = getPrevious(pathpoint2);
                --i;
            }
            return new AttackPathEntity(apathpoint);
    	}
    	return null;
    }
    
    private static PathPoint getPrevious(PathPoint aPoint) {
    	try {
			return (PathPoint) mPrevious.get(aPoint);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    

}
