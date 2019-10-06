package net.alkalus.mod.pollution.util;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import net.alkalus.api.objects.Logger;
import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.data.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class Utils {

	public static final int WILDCARD_VALUE = Short.MAX_VALUE;

	public static final boolean isServer() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}
	
	public static final boolean isClient() {
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

	static class ShortTimerTask extends TimerTask {
		@Override
		public void run() {
			Logger.WARNING("Timer expired.");
		}
	}

	public static boolean containsMatch(final boolean strict, final ItemStack[] inputs, final ItemStack... targets) {
		for (final ItemStack input : inputs) {
			for (final ItemStack target : targets) {
				if (itemMatches(target, input, strict)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean itemMatches(final ItemStack target, final ItemStack input, final boolean strict) {
		if ((input == null) || (target == null)) {
			return false;
		}
		return ((target.getItem() == input.getItem()) && (((target.getItemDamage() == WILDCARD_VALUE) && !strict)
				|| (target.getItemDamage() == input.getItemDamage())));
	}

	//Register an event to both busses.
	public static void registerEvent(Object o){
		MinecraftForge.EVENT_BUS.register(o);
		FMLCommonHandler.instance().bus().register(o);
	}

	public static void paintBox(final Graphics g, final int MinA, final int MinB, final int MaxA, final int MaxB) {
		g.drawRect(MinA, MinB, MaxA, MaxB);
	}

	// Send a message to all players on the server
	public static void sendServerMessage(final String translationKey) {
		sendServerMessage(new ChatComponentText(translationKey));
	}

	// Send a message to all players on the server
	public static void sendServerMessage(final IChatComponent chatComponent) {
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chatComponent);
	}

	/**
	 *
	 * @param colourStr
	 *            e.g. "#FFFFFF"
	 * @return String - formatted "rgb(0,0,0)"
	 */
	public static String hex2RgbFormatted(final String hexString) {
		final Color c = new Color(Integer.valueOf(hexString.substring(1, 3), 16),
				Integer.valueOf(hexString.substring(3, 5), 16), Integer.valueOf(hexString.substring(5, 7), 16));

		final StringBuffer sb = new StringBuffer();
		sb.append("rgb(");
		sb.append(c.getRed());
		sb.append(",");
		sb.append(c.getGreen());
		sb.append(",");
		sb.append(c.getBlue());
		sb.append(")");
		return sb.toString();
	}

	/**
	 *
	 * @param colourStr
	 *            e.g. "#FFFFFF"
	 * @return
	 */
	public static Color hex2Rgb(final String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	/**
	 *
	 * @param colourInt
	 *            e.g. 0XFFFFFF
	 * @return Colour
	 */
	public static Color hex2Rgb(final int colourInt) {
		return Color.decode(String.valueOf(colourInt));
	}

	/**
	 *
	 * @param colourInt
	 *            e.g. 0XFFFFFF
	 * @return short[]
	 */
	public static short[] hex2RgbShort(final int colourInt) {
		final Color rgb = Color.decode(String.valueOf(colourInt));
		final short[] rgba = { (short) rgb.getRed(), (short) rgb.getGreen(), (short) rgb.getBlue(),
				(short) rgb.getAlpha() };
		return rgba;
	}

	public static Timer ShortTimer(final int seconds) {
		Timer timer;
		timer = new Timer();
		timer.schedule(new ShortTimerTask(), seconds * 1000);
		return timer;
	}

	public static String byteToHex(final byte b) {
		final int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	public static Object[] convertListToArray(final List<Object> sourceList) {
		final Object[] targetArray = sourceList.toArray(new Object[sourceList.size()]);
		return targetArray;
	}

	public static List<Object> convertArrayToFixedSizeList(final Object[] sourceArray) {
		final List<Object> targetList = Arrays.asList(sourceArray);
		return targetList;
	}

	public static List<Object> convertArrayToList(final Object[] sourceArray) {
		final List<Object> targetList = new ArrayList<>(Arrays.asList(sourceArray));
		return targetList;
	}

	public static List<Object> convertArrayListToList(final ArrayList<Object> sourceArray) {
		final List<Object> targetList = new ArrayList<Object>(Arrays.asList(sourceArray));
		return targetList;
	}

	public static int rgbtoHexValue(final int r, final int g, final int b) {
		if ((r > 255) || (g > 255) || (b > 255) || (r < 0) || (g < 0) || (b < 0)) {
			return 0;
		}
		final Color c = new Color(r, g, b);
		String temp = Integer.toHexString(c.getRGB() & 0xFFFFFF).toUpperCase();

		// System.out.println( "hex: " + Integer.toHexString( c.getRGB() &
		// 0xFFFFFF ) + " hex value:"+temp);
		temp = Utils.appenedHexNotationToString(String.valueOf(temp));
		Logger.WARNING("Made " + temp + " - Hopefully it's not a mess.");
		Logger.WARNING("It will decode into " + Integer.decode(temp) + ".");
		return Integer.decode(temp);
	}

	/*
	 * http://javadevnotes.com/java-left-pad-string-with-zeros-examples
	 */
	public static String padWithZerosLefts(final String originalString, final int length) {
		final StringBuilder sb = new StringBuilder();
		while ((sb.length() + originalString.length()) < length) {
			sb.append('0');
		}
		sb.append(originalString);
		final String paddedString = sb.toString();
		return paddedString;
	}
	
	public static String padWithZerosRight(final int value, final int length) {
		String originalString = String.valueOf(value);
		final StringBuilder sb = new StringBuilder();
		while ((sb.length() + originalString.length()) < length) {
			sb.append('0');
		}
		//sb.append(originalString);
		if (sb.length() > 0)
		originalString = (originalString + sb.toString());
		final String paddedString = sb.toString();
		return originalString;
	}

	/*
	 * Original Code by Chandana Napagoda -
	 * https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.
	 * html
	 */
	public static Map<Integer, String> hexColourGenerator(final int colorCount) {
		final int maxColorValue = 16777215;
		// this is decimal value of the "FFFFFF"
		final int devidedvalue = maxColorValue / colorCount;
		int countValue = 0;
		final HashMap<Integer, String> hexColorMap = new HashMap<>();
		for (int a = 0; (a < colorCount) && (maxColorValue >= countValue); a++) {
			if (a != 0) {
				countValue += devidedvalue;
				hexColorMap.put(a, Integer.toHexString(0x10000 | countValue).substring(1).toUpperCase());
			} else {
				hexColorMap.put(a, Integer.toHexString(0x10000 | countValue).substring(1).toUpperCase());
			}
		}
		return hexColorMap;
	}

	/*
	 * Original Code by Chandana Napagoda -
	 * https://cnapagoda.blogspot.com.au/2011/03/java-hex-color-code-generator.
	 * html
	 */
	public static Map<Integer, String> hexColourGeneratorRandom(final int colorCount) {
		final HashMap<Integer, String> hexColorMap = new HashMap<>();
		for (int a = 0; a < colorCount; a++) {
			String code = "" + (int) (Math.random() * 256);
			code = code + code + code;
			final int i = Integer.parseInt(code);
			hexColorMap.put(a, Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
			Logger.WARNING("" + Integer.toHexString(0x1000000 | i).substring(1).toUpperCase());
		}
		return hexColorMap;
	}

	public static String appenedHexNotationToString(final Object hexAsStringOrInt) {
		final String hexChar = "0x";
		String result;
		if (hexAsStringOrInt.getClass() == String.class) {

			if (((String) hexAsStringOrInt).length() != 6) {
				final String temp = padWithZerosLefts((String) hexAsStringOrInt, 6);
				result = temp;
			}
			result = hexChar + hexAsStringOrInt;
			return result;
		} else if (hexAsStringOrInt.getClass() == Integer.class || hexAsStringOrInt.getClass() == int.class) {
			String aa = String.valueOf(hexAsStringOrInt);
			if (aa.length() != 6) {
				final String temp = padWithZerosLefts(aa, 6);
				result = temp;
			}
			else {
				result = hexChar + String.valueOf(hexAsStringOrInt);				
			}
			return result;
		} else {
			return null;
		}
	}

	public static Integer appenedHexNotationToInteger(final int hexAsStringOrInt) {
		final String hexChar = "0x";
		String result;
		Logger.WARNING(String.valueOf(hexAsStringOrInt));
		result = hexChar + String.valueOf(hexAsStringOrInt);
		return Integer.getInteger(result);
	}

	public static boolean doesEntryExistAlreadyInOreDictionary(final String OreDictName) {
		if (OreDictionary.getOres(OreDictName).size() != 0) {
			return true;
		}
		return false;
	}

	public static boolean invertBoolean(final boolean booleans) {
		if (booleans == true) {
			return false;
		}
		return true;
	}

	public static File getMcDir() {		
		if (Minecraft.getMinecraft() == null) {
			return new File("testdir");
		}		
		if ((MinecraftServer.getServer() != null) && MinecraftServer.getServer().isDedicatedServer()) {
			return new File(".");
		}
		return Minecraft.getMinecraft().mcDataDir;
	}
	
	public static String sanitizeString(final String input, final char[] aDontRemove) {
		
		String output;
		AutoMap<String> aToRemoveMap = new AutoMap<String>();

		aToRemoveMap.put(" ");
		aToRemoveMap.put("-");
		aToRemoveMap.put("_");
		aToRemoveMap.put("~");
		aToRemoveMap.put("?");
		aToRemoveMap.put("!");
		aToRemoveMap.put("@");
		aToRemoveMap.put("#");
		aToRemoveMap.put("$");
		aToRemoveMap.put("%");
		aToRemoveMap.put("^");
		aToRemoveMap.put("&");
		aToRemoveMap.put("*");
		aToRemoveMap.put("(");
		aToRemoveMap.put(")");
		aToRemoveMap.put("{");
		aToRemoveMap.put("}");
		aToRemoveMap.put("[");
		aToRemoveMap.put("]");
		aToRemoveMap.put(" ");
		
		for (String s : aToRemoveMap) {
			for (char e : aDontRemove) {
			if (s.charAt(0) == e) {
				aToRemoveMap.remove("s");
			}
			}
		}		
		output = input;
		for (String A : aToRemoveMap) {
			output = output.replace(A, "");
		}		
		return output;

	}

	public static String sanitizeString(final String input) {
		String temp;
		String output;

		temp = input.replace(" ", "");
		temp = temp.replace("-", "");
		temp = temp.replace("_", "");
		temp = temp.replace("?", "");
		temp = temp.replace("!", "");
		temp = temp.replace("@", "");
		temp = temp.replace("#", "");
		temp = temp.replace("(", "");
		temp = temp.replace(")", "");
		temp = temp.replace("{", "");
		temp = temp.replace("}", "");
		temp = temp.replace("[", "");
		temp = temp.replace("]", "");
		temp = temp.replace(" ", "");
		output = temp;
		return output;

	}

	public static String sanitizeStringKeepBrackets(final String input) {
		String temp;
		String output;

		temp = input.replace(" ", "");
		temp = temp.replace("-", "");
		temp = temp.replace("_", "");
		temp = temp.replace("?", "");
		temp = temp.replace("!", "");
		temp = temp.replace("@", "");
		temp = temp.replace("#", "");
		temp = temp.replace(" ", "");
		output = temp;
		return output;

	}	

	@SuppressWarnings({ "unused", "unchecked" })
	public static Pair<Integer, Integer> getGregtechVersion(){
		Pair<Integer, Integer> version;
		if (GT_Mod.VERSION == 509){
			Class<GT_Mod> clazz;
			try {				
				clazz = (Class<GT_Mod>) ReflectionUtils.getClass("gregtech.GT_Mod");
				Field mSubversion = ReflectionUtils.getField(clazz, "SUBVERSION");
				if (mSubversion != null){
					int mSub = 0;
					mSub = mSubversion.getInt(clazz);
					if (mSub != 0){
						version = new Pair<Integer, Integer>(9, mSub);
						return version;
					}
				}
			}
			catch (Throwable t){
				
			}
		}
		//5.08.33
		else if (GT_Mod.VERSION == 508){
			version = new Pair<Integer, Integer>(8, 33);
			return version;

		}
		//5.07.07
		else if (GT_Mod.VERSION == 507){
			version = new Pair<Integer, Integer>(7, 7);
			return version;

		}
		//Returb Bad Value
		version = new Pair<Integer, Integer>(0, 0);
		return version;	
	}
	
	public static int getGregtechVersionAsInt(){
		Pair<Integer, Integer> ver = getGregtechVersion();
		return 50000+(ver.getKey()*100)+(ver.getValue());
	}
	
	public static String getGregtechVersionAsString(){
		Pair<Integer, Integer> ver = getGregtechVersion();
		return "5."+ver.getKey()+"."+ver.getValue();		
	}
	
	public static int getGregtechSubVersion(){
		Pair<Integer, Integer> ver = getGregtechVersion();		
		return ver.getValue();		
	}

	public static ItemList getValueOfItemList(String string, ItemList aOther) {		
		ItemList[] aListValues = ItemList.class.getEnumConstants();		
		for (ItemList aItem : aListValues) {
			if (aItem != null) {
				if (aItem.name().equals(string) || aItem.name().toLowerCase().equals(string.toLowerCase())) {
					return aItem;
				}
			}
		}
		Logger.INFO("Tried to obtain '"+string+"' from the GT ItemList, however it does not exist.");
		if (aOther != null) {
			Logger.INFO("Using fallback option instead - "+aOther.name());
		}
		return aOther;
	}

	public static long getMillisSince(long aStartTime, long aCurrentTime) {		
		return (aCurrentTime - aStartTime);
	}
	
	public static long getSecondsFromMillis(long aMillis) {
		return (aMillis/1000);
	}
	
	public static long getTicksFromSeconds(long aSeconds) {
		return (aSeconds*20);
	}

}
