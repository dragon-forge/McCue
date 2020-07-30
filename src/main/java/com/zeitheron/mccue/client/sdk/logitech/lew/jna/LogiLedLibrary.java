package com.zeitheron.mccue.client.sdk.logitech.lew.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.WString;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public interface LogiLedLibrary
		extends Library
{
	String JNA_LIBRARY_NAME = "LogitechLedEnginesWrapper";
	NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("LogitechLedEnginesWrapper");
	LogiLedLibrary INSTANCE = Native.loadLibrary("LogitechLedEnginesWrapper", LogiLedLibrary.class);

	int LOGI_DEVICETYPE_ALL = (int) ((1 << 0) | (1 << 1) | (1 << 2));
	int LOGI_LED_BITMAP_BYTES_PER_KEY = (int) 4;
	int LOGI_DEVICETYPE_MONOCHROME = (int) (1 << 0);
	int LOGI_LED_BITMAP_WIDTH = (int) 21;
	int LOGI_DEVICETYPE_PERKEY_RGB = (int) (1 << 2);
	int LOGI_LED_DURATION_INFINITE = (int) 0;
	int LOGI_LED_BITMAP_SIZE = (int) (21 * 6 * 4);
	int LOGI_DEVICETYPE_RGB_ORD = (int) 1;
	int LOGI_DEVICETYPE_PERKEY_RGB_ORD = (int) 2;
	int LOGI_LED_BITMAP_HEIGHT = (int) 6;
	int LOGI_DEVICETYPE_MONOCHROME_ORD = (int) 0;
	int LOGI_DEVICETYPE_RGB = (int) (1 << 1);

	boolean LogiLedInit();

	boolean LogiLedGetSdkVersion(IntBuffer majorNum, IntBuffer minorNum, IntBuffer buildNum);

	boolean LogiLedGetConfigOptionNumber(WString configPath, DoubleBuffer defaultValue);

	boolean LogiLedGetConfigOptionBool(WString configPath, ByteBuffer defaultValue);

	boolean LogiLedGetConfigOptionColor(WString configPath, IntBuffer defaultRed, IntBuffer defaultGreen, IntBuffer defaultBlue);

	boolean LogiLedGetConfigOptionRect(WString configPath, IntBuffer defaultX, IntBuffer defaultY, IntBuffer defaultWidth, IntBuffer defaultHeight);

	boolean LogiLedGetConfigOptionString(WString configPath, CharBuffer defaultValue, int bufferSize);

	boolean LogiLedGetConfigOptionKeyInput(WString configPath, CharBuffer defaultValue, int bufferSize);

	boolean LogiLedGetConfigOptionSelect(WString configPath, CharBuffer defaultValue, IntBuffer valueSize, WString values, int bufferSize);

	boolean LogiLedGetConfigOptionRange(WString configPath, IntBuffer defaultValue, int min, int max);

	boolean LogiLedSetConfigOptionLabel(WString configPath, CharBuffer label);

	boolean LogiLedSetTargetDevice(int targetDevice);

	boolean LogiLedSaveCurrentLighting();

	boolean LogiLedSetLighting(int redPercentage, int greenPercentage, int bluePercentage);

	boolean LogiLedRestoreLighting();

	boolean LogiLedFlashLighting(int redPercentage, int greenPercentage, int bluePercentage, int milliSecondsDuration, int milliSecondsInterval);

	boolean LogiLedPulseLighting(int redPercentage, int greenPercentage, int bluePercentage, int milliSecondsDuration, int milliSecondsInterval);

	boolean LogiLedStopEffects();

	boolean LogiLedSetLightingFromBitmap(ByteBuffer bitmap);

	boolean LogiLedSetLightingForKeyWithScanCode(int keyCode, int redPercentage, int greenPercentage, int bluePercentage);

	boolean LogiLedSetLightingForKeyWithHidCode(int keyCode, int redPercentage, int greenPercentage, int bluePercentage);

	boolean LogiLedSetLightingForKeyWithQuartzCode(int keyCode, int redPercentage, int greenPercentage, int bluePercentage);

	boolean LogiLedSetLightingForKeyWithKeyName(int keyName, int redPercentage, int greenPercentage, int bluePercentage);

	boolean LogiLedSaveLightingForKey(int keyName);

	boolean LogiLedRestoreLightingForKey(int keyName);

	boolean LogiLedExcludeKeysFromBitmap(IntBuffer keyList, int listCount);

	boolean LogiLedFlashSingleKey(int keyName, int redPercentage, int greenPercentage, int bluePercentage, int msDuration, int msInterval);

	boolean LogiLedPulseSingleKey(int keyName, int startRedPercentage, int startGreenPercentage, int startBluePercentage, int finishRedPercentage, int finishGreenPercentage, int finishBluePercentage, int msDuration, byte isInfinite);

	boolean LogiLedStopEffectsOnKey(int keyName);

	boolean LogiLedSetLightingForTargetZone(int deviceType, int zone, int redPercentage, int greenPercentage, int bluePercentage);

	void LogiLedShutdown();
}