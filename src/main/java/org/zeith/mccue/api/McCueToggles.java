package org.zeith.mccue.api;

public class McCueToggles
{
	/**
	 * Prevents McCue from adding block into the game, triggers and other things.
	 * This is useful for activating a "library-only" mode.
	 * Mods like TerrariaCraft use this toggle.
	 */
	public static boolean mcCUEAsLibrary = false;
	
	public static boolean initLogiLEDSdk = true;
	public static boolean initICUESdk = true;
	public static boolean initRazerChromaSdk = true;
}