package net.alkalus.mod.pollution;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.alkalus.api.objects.Logger;
import net.alkalus.mod.pollution.proxy.CommonProxy;

@Mod(name = BiterMod.NAME, modid = BiterMod.MODID, version = BiterMod.VERSION, dependencies = "required-after:Forge; after:gregtech;")
public class BiterMod {

	private static boolean USED_INTERNAL_STATE = false;
	
	public static enum INIT_PHASE {
		SUPER(null), PRE_INIT(SUPER), INIT(PRE_INIT), POST_INIT(INIT), SERVER_START(POST_INIT), STARTED(SERVER_START);
		protected boolean mIsPhaseActive = false;
		private final INIT_PHASE mPrev;

		private INIT_PHASE(INIT_PHASE aPreviousPhase) {
			mPrev = aPreviousPhase;
		}

		public synchronized final boolean isPhaseActive() {
			return mIsPhaseActive;
		}
		public synchronized final void setPhaseActive(boolean aIsPhaseActive) {
			if (mPrev != null && mPrev.isPhaseActive()) {
				mPrev.setPhaseActive(false);
			}
			mIsPhaseActive = aIsPhaseActive;
				if (PRIVATE_INSTANCE.CURRENT_LOAD_PHASE != this) {
					PRIVATE_INSTANCE.CURRENT_LOAD_PHASE = this;
					INSTANCE.CURRENT_LOAD_PHASE = this;
				}				
			
			Logger.INFO("Changed state to " + PRIVATE_INSTANCE.CURRENT_LOAD_PHASE);
		}
	}

	public INIT_PHASE CURRENT_LOAD_PHASE;

	// Mod Instance
	@Mod.Instance(BiterMod.MODID)
	public static BiterMod INSTANCE;
	
	private static BiterMod PRIVATE_INSTANCE;

	public static final String NAME = "Alk's Biter Mod";
	public static final String MODID = "bitermod";
	public static final String VERSION = "0.0.1";

	// GT++ Proxy Instances
	@SidedProxy(clientSide = "net.alkalus.mod.pollution.proxy.ClientProxy", serverSide = "net.alkalus.mod.pollution.proxy.ServerProxy")
	public static CommonProxy proxy;

	public BiterMod() {
		super();
		CURRENT_LOAD_PHASE = INIT_PHASE.SUPER;
		PRIVATE_INSTANCE = this;
		INIT_PHASE.SUPER.setPhaseActive(true);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		INIT_PHASE.PRE_INIT.setPhaseActive(true);
		proxy.preInit(event);

	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		INIT_PHASE.INIT.setPhaseActive(true);
		proxy.init(event);
		proxy.registerNetworkStuff();

	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		INIT_PHASE.POST_INIT.setPhaseActive(true);
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		INIT_PHASE.SERVER_START.setPhaseActive(true);
		proxy.onLoadComplete(event);
	}

	@EventHandler
	public synchronized void serverStarting(final FMLServerStartingEvent event) {
		INIT_PHASE.STARTED.setPhaseActive(true);
		proxy.serverStarting(event);
	}

	@Mod.EventHandler
	public synchronized void serverStopping(final FMLServerStoppingEvent event) {

	}
}
