package net.alkalus.mod.pollution.util;

public class BiterUtils {
	
	public static byte getTierForPollution(int pollution) {
		int aTier = pollution / 2000000;
		if (aTier <= 1) {
			aTier = 1;
		}
		return (byte) MathUtils.balance(aTier, 1, Byte.MAX_VALUE);
	}
	
	public static class BiterTemplate {
		
	}
	
}
