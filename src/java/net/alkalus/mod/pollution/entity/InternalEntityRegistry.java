package net.alkalus.mod.pollution.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.alkalus.api.objects.Logger;
import net.alkalus.mod.pollution.BiterMod;
import net.alkalus.mod.pollution.entity.base.BaseEntityBiter;
import net.alkalus.mod.pollution.util.Utils;

public class InternalEntityRegistry {

	static int mEntityID = 0;
	
	public static void registerEntities(){	
		Logger.INFO("Registering "+BiterMod.NAME+" Entities.");       
        
        /**
         * Globals, which generate spawn eggs. (Currently required for Giant chicken spawning)
         */
        
        EntityRegistry.registerGlobalEntityID(BaseEntityBiter.class, "BaseBiterMobEntity", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));      
       
	}
	
}
