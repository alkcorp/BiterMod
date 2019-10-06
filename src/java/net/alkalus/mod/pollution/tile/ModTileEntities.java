package net.alkalus.mod.pollution.tile;

import cpw.mods.fml.common.registry.GameRegistry;
import net.alkalus.api.objects.Logger;
import net.alkalus.mod.pollution.BiterMod;

public class ModTileEntities {

	public static void init() {
		Logger.INFO("Registering "+BiterMod.NAME+" Tile Entities.");		
		GameRegistry.registerTileEntity(TileBiterHiveBase.class, "TileBiterHiveBase");
	}

}
