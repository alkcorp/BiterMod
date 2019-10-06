package net.alkalus.mod.pollution.event;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.alkalus.api.objects.Logger;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

public class BlockEventHandler {
	public static ArrayList<ItemStack> oreLimestone;
	public static ArrayList<ItemStack> blockLimestone;
	public static ItemStack fluoriteOre;

	@SubscribeEvent
	public void onBlockLeftClicked(final PlayerInteractEvent event) {
		/*
		 * if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) return;
		 * 
		 * ItemStack heldItem = event.entityPlayer.getHeldItem(); Block block =
		 * event.world.getBlock(event.x, event.y, event.z);
		 * 
		 * // If the block clicked was Stone, the player was holding an Iron Pickaxe and
		 * a random integer from 0 (inclusive) to 2 (exclusive) is 0 (50% chance) if
		 * (block == Blocks.stone && heldItem != null && heldItem.getItem() ==
		 * Items.iron_pickaxe && random.nextInt(2) == 0) { ForgeDirection direction =
		 * ForgeDirection.getOrientation(event.face); // Convert the numeric face to a
		 * ForgeDirection int fireX = event.x + direction.offsetX, fireY = event.y +
		 * direction.offsetY, fireZ = event.z + direction.offsetZ; // Offset the block's
		 * coordinates according to the direction
		 * 
		 * if (event.world.isAirBlock(fireX, fireY, fireZ)) { // If the block at the new
		 * coordinates is Air event.world.setBlock(fireX, fireY, fireZ, Blocks.fire); //
		 * Replace it with Fire event.useBlock = Event.Result.DENY; // Prevent the Fire
		 * from being extinguished (also prevents Block#onBlockClicked from being
		 * called) } }
		 */
	}

	@SubscribeEvent
	public void onEntityDrop(final LivingDropsEvent event) {
		/*
		 * if (event.entityLiving instanceof EntityPig && event.source instanceof
		 * EntityDamageSource) { // getEntity will return the Entity that caused the
		 * damage,even for indirect damage sources like arrows/fireballs // (where it
		 * will return the Entity that shot the projectile rather than the projectile
		 * itself) Entity sourceEntity = event.source.getEntity(); ItemStack heldItem =
		 * sourceEntity instanceof EntityLiving ? ((EntityLiving)
		 * sourceEntity).getHeldItem() : sourceEntity instanceof EntityPlayer ?
		 * ((EntityPlayer) sourceEntity).getHeldItem() : null;
		 * 
		 * if (heldItem != null && heldItem.getItem() == Items.iron_pickaxe) {
		 * System.out.println("EntityPig drops event"); event.drops.clear();
		 * event.entityLiving.dropItem(Items.diamond, 64); } }
		 */
	}

	@SubscribeEvent
	public void onBlockBreak(final BlockEvent.BreakEvent event) {

	}

	public static final Map<String, EntityPlayer> mCachedFakePlayers = new WeakHashMap<String, EntityPlayer>();

	// Used to handle Thaumcraft Shards when TC is not installed.
	@SubscribeEvent
	public void harvestDrops(final BlockEvent.HarvestDropsEvent event) {
		try {
			
		} catch (Throwable r) {
			Logger.INFO("Block Event Handler Failed. Please Report this to Alkalus.");
			r.printStackTrace();
		}
	}

	@SubscribeEvent
	public void logsHarvest(final BlockEvent.HarvestDropsEvent event) {
		/*
		 * if (event.block instanceof BlockLog) { //
		 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/
		 * modification-development/2444501-harvestdropevent-changing-drops-of-vanilla-
		 * blocks
		 * 
		 * // Utils.sendServerMessage("Logs! Harvester: %s Drops: %s", event.harvester
		 * != null ? event.harvester.getCommandSenderName() : "<none>",
		 * event.drops.stream().map(ItemStack.toString()).collect(Collectors.
		 * joining(", "))); if (event.harvester != null) { ItemStack heldItem =
		 * event.harvester.getHeldItem(); if (heldItem == null ||
		 * heldItem.getItem().getHarvestLevel(heldItem, "axe") < 1) {
		 * event.drops.clear();
		 * Utils.sendServerMessage("Harvester had wrong tool, clearing drops"); } else {
		 * Utils.sendServerMessage("Harvester had correct tool, not clearing drops"); }
		 * } else { event.drops.clear();
		 * Utils.sendServerMessage("No harvester, clearing drops"); } }
		 */
	}
}
