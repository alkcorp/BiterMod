package net.alkalus.mod.pollution.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.alkalus.api.objects.Logger;
import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.data.Pair;
import net.alkalus.mod.pollution.BiterMod;
import net.alkalus.mod.pollution.block.ModBlocks;
import net.alkalus.mod.pollution.entity.InternalEntityRegistry;
import net.alkalus.mod.pollution.event.BlockEventHandler;
import net.alkalus.mod.pollution.event.BiterDeathHandler;
import net.alkalus.mod.pollution.event.GeneralTooltipEventHandler;
import net.alkalus.mod.pollution.item.ModItems;
import net.alkalus.mod.pollution.tile.ModTileEntities;
import net.alkalus.mod.pollution.util.Utils;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;

public class CommonProxy {

	public CommonProxy() {
		// Should Register Gregtech Materials I've Made
		Utils.registerEvent(this);
		Logger.INFO("We're using Gregtech " + Utils.getGregtechVersionAsString());		
	}

	public void preInit(final FMLPreInitializationEvent e) {
		Logger.INFO("Doing some house cleaning.");
		ModItems.init();
		ModBlocks.init();
		Logger.INFO("Making sure we're ready to party!");
		// Registration of entities and renderers
		Logger.INFO("[Proxy] Calling Entity registrator.");
		registerEntities();
		Logger.INFO("[Proxy] Calling Tile Entity registrator.");
		registerTileEntities();
		Logger.INFO("[Proxy] Calling Render registrator.");
		registerRenderThings();

	}

	public void init(final FMLInitializationEvent e) {
		

		/**
		 * Register the Event Handlers.
		 */
		// Block Handler for all events.
		Utils.registerEvent(new BlockEventHandler());
		Utils.registerEvent(new GeneralTooltipEventHandler());
		Utils.registerEvent(new BiterDeathHandler());
		//Utils.registerEvent(new EntityDeathHandler());
	}

	public void postInit(final FMLPostInitializationEvent e) {
		Logger.INFO("Cleaning up, doing postInit.");		
		Logger.INFO("Loading queued recipes.");
		//COMPAT_HANDLER.runQueuedRecipes();
		
	}

	public void serverStarting(final FMLServerStartingEvent e) {
		//COMPAT_HANDLER.InitialiseLateHandlerThenAddRecipes();
	}

	public void onLoadComplete(FMLLoadCompleteEvent event) {
		
	}

	public void registerNetworkStuff() {
		
	}

	public void registerEntities() {
		InternalEntityRegistry.registerEntities();
	}

	public void registerTileEntities() {
		ModTileEntities.init();
	}

	public void registerRenderThings() {

	}

	public int addArmor(final String armor) {
		return 0;
	}
	
	protected final AutoMap<Pair<Item, IItemRenderer>> mItemRenderMappings = new AutoMap<Pair<Item, IItemRenderer>>();
	

	public static void registerItemRendererGlobal(Item aItem, IItemRenderer aRenderer) {
		BiterMod.proxy.registerItemRenderer(aItem, aRenderer);
	}
	
	public void registerItemRenderer(Item aItem, IItemRenderer aRenderer) {
		if (Utils.isServer()) {
			return;
		}
		else {
			mItemRenderMappings.add(new Pair<Item, IItemRenderer>(aItem, aRenderer));
		}		
	}

}
