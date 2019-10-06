package net.alkalus.mod.pollution.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.alkalus.mod.pollution.BiterMod;
import net.alkalus.mod.pollution.BiterMod.INIT_PHASE;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class GeneralTooltipEventHandler {

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event){

		
		if (BiterMod.INSTANCE.CURRENT_LOAD_PHASE != INIT_PHASE.STARTED && BiterMod.INSTANCE.CURRENT_LOAD_PHASE != INIT_PHASE.SERVER_START) {
			return;
		}
		if (event.itemStack == null) {
			return;
		}

		//Material Collector Tooltips		
		//if (ModBlocks.blockPooCollector != null && Block.getBlockFromItem(event.itemStack.getItem()) == ModBlocks.blockPooCollector) {
			
		//}

	}

}
