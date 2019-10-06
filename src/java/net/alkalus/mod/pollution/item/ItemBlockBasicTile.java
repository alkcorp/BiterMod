package net.alkalus.mod.pollution.item;

import java.util.List;

import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.mod.pollution.block.ITileTooltip;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBasicTile extends ItemBlock {

	private final AutoMap<String> mToolTip;

	public ItemBlockBasicTile(final Block block) {
		super(block);
		this.mToolTip = ((ITileTooltip) block).getTooltip();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (mToolTip != null && !mToolTip.isEmpty()) {
			for (String s : mToolTip) {
				list.add(s);
			}
		}
		super.addInformation(stack, aPlayer, list, bool);
	}
	
}
