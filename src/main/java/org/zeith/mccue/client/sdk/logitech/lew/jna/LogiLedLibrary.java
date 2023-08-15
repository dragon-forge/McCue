package org.zeith.mccue.client.sdk.logitech.lew.jna;

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
	NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(JNA_LIBRARY_NAME);
	LogiLedLibrary INSTANCE = Native.loadLibrary(JNA_LIBRARY_NAME, LogiLedLibrary.class);
	
	int
			ESC = 0x01,
			F1 = 0x3b,
			F2 = 0x3c,
			F3 = 0x3d,
			F4 = 0x3e,
			F5 = 0x3f,
			F6 = 0x40,
			F7 = 0x41,
			F8 = 0x42,
			F9 = 0x43,
			F10 = 0x44,
			F11 = 0x57,
			F12 = 0x58,
			PRINT_SCREEN = 0x137,
			SCROLL_LOCK = 0x46,
			PAUSE_BREAK = 0x145,
			TILDE = 0x29,
			ONE = 0x02,
			TWO = 0x03,
			THREE = 0x04,
			FOUR = 0x05,
			FIVE = 0x06,
			SIX = 0x07,
			SEVEN = 0x08,
			EIGHT = 0x09,
			NINE = 0x0A,
			ZERO = 0x0B,
			MINUS = 0x0C,
			EQUALS = 0x0D,
			BACKSPACE = 0x0E,
			INSERT = 0x152,
			HOME = 0x147,
			PAGE_UP = 0x149,
			NUM_LOCK = 0x45,
			NUM_SLASH = 0x135,
			NUM_ASTERISK = 0x37,
			NUM_MINUS = 0x4A,
			TAB = 0x0F,
			Q = 0x10,
			W = 0x11,
			E = 0x12,
			R = 0x13,
			T = 0x14,
			Y = 0x15,
			U = 0x16,
			I = 0x17,
			O = 0x18,
			P = 0x19,
			OPEN_BRACKET = 0x1A,
			CLOSE_BRACKET = 0x1B,
			BACKSLASH = 0x2B,
			KEYBOARD_DELETE = 0x153,
			END = 0x14F,
			PAGE_DOWN = 0x151,
			NUM_SEVEN = 0x47,
			NUM_EIGHT = 0x48,
			NUM_NINE = 0x49,
			NUM_PLUS = 0x4E,
			CAPS_LOCK = 0x3A,
			A = 0x1E,
			S = 0x1F,
			D = 0x20,
			F = 0x21,
			G = 0x22,
			H = 0x23,
			J = 0x24,
			K = 0x25,
			L = 0x26,
			SEMICOLON = 0x27,
			APOSTROPHE = 0x28,
			ENTER = 0x1C,
			NUM_FOUR = 0x4B,
			NUM_FIVE = 0x4C,
			NUM_SIX = 0x4D,
			LEFT_SHIFT = 0x2A,
			Z = 0x2C,
			X = 0x2D,
			C = 0x2E,
			V = 0x2F,
			B = 0x30,
			N = 0x31,
			M = 0x32,
			COMMA = 0x33,
			PERIOD = 0x34,
			FORWARD_SLASH = 0x35,
			RIGHT_SHIFT = 0x36,
			ARROW_UP = 0x148,
			NUM_ONE = 0x4F,
			NUM_TWO = 0x50,
			NUM_THREE = 0x51,
			NUM_ENTER = 0x11C,
			LEFT_CONTROL = 0x1D,
			LEFT_WINDOWS = 0x15B,
			LEFT_ALT = 0x38,
			SPACE = 0x39,
			RIGHT_ALT = 0x138,
			RIGHT_WINDOWS = 0x15C,
			APPLICATION_SELECT = 0x15D,
			RIGHT_CONTROL = 0x11D,
			ARROW_LEFT = 0x14B,
			ARROW_DOWN = 0x150,
			ARROW_RIGHT = 0x14D,
			NUM_ZERO = 0x52,
			NUM_PERIOD = 0x53,
			G_1 = 0xFFF1,
			G_2 = 0xFFF2,
			G_3 = 0xFFF3,
			G_4 = 0xFFF4,
			G_5 = 0xFFF5,
			G_6 = 0xFFF6,
			G_7 = 0xFFF7,
			G_8 = 0xFFF8,
			G_9 = 0xFFF9,
			G_LOGO = 0xFFFF1,
			G_BADGE = 0xFFFF2,
	
	DeviceType_Keyboard = 0x0,
			DeviceType_Mouse = 0x3,
			DeviceType_Mousemat = 0x4,
			DeviceType_Headset = 0x8,
			DeviceType_Speaker = 0xE,
	
	LOGI_LED_BITMAP_WIDTH = 21,
			LOGI_LED_BITMAP_HEIGHT = 6,
			LOGI_LED_BITMAP_BYTES_PER_KEY = 4,
			LOGI_LED_BITMAP_SIZE = LOGI_LED_BITMAP_WIDTH * LOGI_LED_BITMAP_HEIGHT * LOGI_LED_BITMAP_BYTES_PER_KEY,
	
	LOGI_LED_DURATION_INFINITE = 0,
	
	LOGI_DEVICETYPE_MONOCHROME_ORD = 0,
			LOGI_DEVICETYPE_RGB_ORD = 1,
			LOGI_DEVICETYPE_PERKEY_RGB_ORD = 2,
	
	LOGI_DEVICETYPE_MONOCHROME = 1 << LOGI_DEVICETYPE_MONOCHROME_ORD,
			LOGI_DEVICETYPE_RGB = 1 << LOGI_DEVICETYPE_RGB_ORD,
			LOGI_DEVICETYPE_PERKEY_RGB = 1 << LOGI_DEVICETYPE_PERKEY_RGB_ORD,
	
	LOGI_DEVICETYPE_ALL = LOGI_DEVICETYPE_MONOCHROME | LOGI_DEVICETYPE_RGB | LOGI_DEVICETYPE_PERKEY_RGB;
	
	boolean LogiLedInit();
	
	boolean LogiLedInitWithName(String name);
	
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