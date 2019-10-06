package net.alkalus.mod.pollution.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.alkalus.api.objects.data.Pair;
import net.alkalus.mod.pollution.client.render.RenderBiterBase;
import net.alkalus.mod.pollution.entity.base.BaseEntityBiter;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy implements Runnable{

	@SideOnly(Side.CLIENT)
	public static boolean mFancyGraphics = false;

	public ClientProxy(){
		//Get Graphics Mode.
		mFancyGraphics = Minecraft.isFancyGraphicsEnabled();
	}

	@SideOnly(Side.CLIENT)
	public static String playerName = "";

	@Override
	public void preInit(final FMLPreInitializationEvent e) {
		super.preInit(e);
	}

	@Override
	public void init(final FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(final FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void registerRenderThings(){

		// Standard GT++

		/**
		 * Entities
		 */

		RenderingRegistry.registerEntityRenderingHandler(BaseEntityBiter.class, new RenderBiterBase(1f));
		/*RenderingRegistry.registerEntityRenderingHandler(EntityPrimedMiningExplosive.class, new RenderMiningExplosivesPrimed());
		RenderingRegistry.registerEntityRenderingHandler(EntitySickBlaze.class, new RenderSickBlaze());
		RenderingRegistry.registerEntityRenderingHandler(EntityStaballoyConstruct.class, new RenderStaballoyConstruct());
		RenderingRegistry.registerEntityRenderingHandler(EntityToxinballSmall.class, new RenderToxinball(1F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySulfuricAcidPotion.class, new RenderSnowball(ModItems.itemSulfuricPotion));
		RenderingRegistry.registerEntityRenderingHandler(EntityHydrofluoricAcidPotion.class, new RenderSnowball(ModItems.itemHydrofluoricPotion));
		RenderingRegistry.registerEntityRenderingHandler(EntityTeslaTowerLightning.class, new RenderPlasmaBolt());
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantChickenBase.class, new RenderGiantChicken(new ModelGiantChicken(), 1f));
		RenderingRegistry.registerEntityRenderingHandler(EntityBatKing.class, new RenderBatKing());
		RenderingRegistry.registerEntityRenderingHandler(EntityThrowableBomb.class, new RenderSnowball(ModItems.itemBomb, 1));
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningAttack.class, new RenderFireball(1F));
		*/
		/**
		 * Tiles
		 */

		//Logger.INFO("Registering Custom Renderer for the Fire Pit.");
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFirepit.class, new FirepitRender());
		//Logger.INFO("Registering Custom Renderer for the Lead Lined Chest.");
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecayablesChest.class, new RenderDecayChest());


		//GT++ Australia

		/**
		 * Entities
		 */

		//RenderingRegistry.registerEntityRenderingHandler(EntityAustralianSpiderBase.class, new RenderAustralianSpider());
		//RenderingRegistry.registerEntityRenderingHandler(EntityBoar.class, new RenderBoar(new ModelBoar(), new ModelBoar(0.5F), 0.7F));
		//RenderingRegistry.registerEntityRenderingHandler(EntityDingo.class, new RenderDingo(new ModelDingo(), new ModelDingo(), 0.5F));
		//RenderingRegistry.registerEntityRenderingHandler(EntityOctopus.class, new RenderOctopus(new ModelOctopus(), 0.7F));
		
		
		
		
		
		
		
		/**
		 * Items
		 */		
		for (Pair<Item, IItemRenderer> sItemRenderMappings : mItemRenderMappings) {
			MinecraftForgeClient.registerItemRenderer(sItemRenderMappings.getKey(), sItemRenderMappings.getValue());			
		}
		
	}

	@Override
	public int addArmor(final String armor){
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}

	@Override
	public void serverStarting(final FMLServerStartingEvent e)
	{

	}



	public void onPreLoad() {
		
	}

	@Override
	public void run() {
		
	}

	@Override
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		super.onLoadComplete(event);
	}

}
