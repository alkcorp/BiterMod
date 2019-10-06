package net.alkalus.mod.pollution.block;

import net.minecraft.block.Block;

public class ModBlocks {

	public static Block BLOCK_BITER_NEST;
	
	public static void init() {		
		BLOCK_BITER_NEST = new BlockBiterHive("Generic");
	}

}