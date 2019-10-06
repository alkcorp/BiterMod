package net.alkalus.mod.pollution.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.alkalus.api.objects.Logger;
import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.mod.pollution.BiterMod;
import net.alkalus.mod.pollution.item.ItemBlockBasicTile;
import net.alkalus.mod.pollution.tile.TileBiterHiveBase;
import net.alkalus.mod.pollution.util.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBiterHive extends BlockContainer implements ITileTooltip
{
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureFront;
	
	private final static ConcurrentHashMap<Integer, String> mLocalizedNameMap = new ConcurrentHashMap<Integer, String>();

	/**
	 * Determines which tooltip is displayed within the itemblock.
	 */
	private final AutoMap<String> mTooltip = new AutoMap<String>();

	@Override
	public AutoMap<String> getTooltip() {
		return this.mTooltip;
	}

	@SuppressWarnings("deprecation")
	public BlockBiterHive(String aType)	{
		super(Material.wood);
		this.setBlockName("blockBiterHive"+aType);
		this.setHardness(5f);
		this.setResistance(1f);
		GameRegistry.registerBlock(this, ItemBlockBasicTile.class, "blockBiterHive"+aType);
		LanguageRegistry.addName(this, "Hive");
		mTooltip.put("Generates Biters");
		mTooltip.put("Higher levels of pollution create stronger hives");
		mTooltip.put("Hives are incredibly hard to destroy");
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int aSide, final int p_149691_2_)
	{
		return aSide == 1 ? this.blockIcon : (aSide == 0 ? this.blockIcon : this.blockIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_)
	{
		this.blockIcon = p_149651_1_.registerIcon(BiterMod.MODID + ":" + "hive");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float lx, final float ly, final float lz)
	{
		if (world.isRemote) {
			return true;
		}
		return true;
	}

	@Override
	public int getRenderBlockPass() {
		return 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		Logger.WARNING("Creating Hive.");
		TileBiterHiveBase hive = new TileBiterHiveBase();		
		return hive;
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int number) {
		//InventoryUtils.dropInventoryItems(world, x, y, z, block);
		super.breakBlock(world, x, y, z, block, number);
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack stack) {
		
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return true;
	}

	@Override
	public int getLightValue() {
		return 8;
	}

	@Override
	public float getBlockHardness(World p_149712_1_, int p_149712_2_, int p_149712_3_, int p_149712_4_) {
		return 5f;
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer p_149737_1_, World p_149737_2_, int p_149737_3_, int p_149737_4_, int p_149737_5_) {
		if (p_149737_1_.capabilities.isCreativeMode) {
			return 1f;
		}
		return 8f;
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		
	}

	@Override
	protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_, ItemStack p_149642_5_) {
		
	}

	@Override
	public void dropXpOnBlockBreak(World p_149657_1_, int p_149657_2_, int p_149657_3_, int p_149657_4_, int aXP) {
		super.dropXpOnBlockBreak(p_149657_1_, p_149657_2_, p_149657_3_, p_149657_4_, aXP * 500);
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return super.damageDropped(0);
	}

	@Override
	public int getBlockColor() {
		return super.getBlockColor();
	}

	@Override
	public int getRenderColor(int p_149741_1_) {
		return super.getRenderColor(p_149741_1_);
	}

	@Override
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
		return super.colorMultiplier(p_149720_1_, p_149720_2_, p_149720_3_, p_149720_4_);
	}

	@Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
		return 0;
	}

	@Override
	public String getLocalizedName() {
		return super.getLocalizedName();
	}

	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return super.getItem(p_149694_1_, p_149694_2_, p_149694_3_, p_149694_4_);
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		for (int i=0;i<256;i++) {
			p_149666_3_.add(new ItemStack(this, 1, i));
			mLocalizedNameMap.put(i, super.getLocalizedName()+" ["+i+"]");
		}
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return CreativeTabs.tabCombat;
	}

	@Override
	public boolean canDropFromExplosion(Explosion p_149659_1_) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		if (player.capabilities.isCreativeMode) {
			return true;
		}		
		return super.canHarvestBlock(player, meta);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList<ItemStack>();
	}

	@Override
	public String getHarvestTool(int metadata) {
		return "pickaxe";
	}

	@Override
	public int getHarvestLevel(int metadata) {
		return MathUtils.balance(metadata/25, 1, 10);
	}

	@Override
	public boolean isToolEffective(String type, int metadata) {
		if (type.toLowerCase().equals("pickaxe")) {
			return true;
		}
		return super.isToolEffective(type, metadata);
	}

}