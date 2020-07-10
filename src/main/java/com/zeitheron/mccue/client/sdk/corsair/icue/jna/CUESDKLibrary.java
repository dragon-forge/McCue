package com.zeitheron.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface CUESDKLibrary
		extends Library
{
	String JNA_LIBRARY_NAME = "CUESDK";
	NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("CUESDK");
	CUESDKLibrary INSTANCE = Native.loadLibrary("CUESDK", CUESDKLibrary.class);
	int CORSAIR_DEVICE_ID_MAX = 128;

	byte CorsairSetLedsColors(int var1, CorsairLedColor var2);

	byte CorsairSetLedsColorsBufferByDeviceIndex(int var1, int var2, CorsairLedColor var3);

	byte CorsairSetLedsColorsFlushBuffer();

	byte CorsairSetLedsColorsFlushBufferAsync(CorsairSetLedsColorsFlushBufferAsync_callback_callback var1, Pointer var2);

	byte CorsairGetLedsColors(int var1, CorsairLedColor var2);

	byte CorsairGetLedsColorsByDeviceIndex(int var1, int var2, CorsairLedColor var3);

	byte CorsairSetLedsColorsAsync(int var1, CorsairLedColor var2, CorsairSetLedsColorsAsync_CallbackType_callback var3, Pointer var4);

	int CorsairGetDeviceCount();

	CorsairDeviceInfo CorsairGetDeviceInfo(int var1);

	CorsairLedPositions CorsairGetLedPositions();

	CorsairLedPositions CorsairGetLedPositionsByDeviceIndex(int var1);

	int CorsairGetLedIdForKeyName(byte var1);

	byte CorsairRequestControl(int var1);

	CorsairProtocolDetails.ByValue CorsairPerformProtocolHandshake();

	int CorsairGetLastError();

	byte CorsairReleaseControl(int var1);

	byte CorsairSetLayerPriority(int var1);

	byte CorsairRegisterKeypressCallback(CorsairRegisterKeypressCallback_CallbackType_callback var1, Pointer var2);

	@Deprecated
	byte CorsairGetBoolPropertyValue(int var1, int var2, Pointer var3);

	byte CorsairGetBoolPropertyValue(int var1, int var2, ByteBuffer var3);

	@Deprecated
	byte CorsairGetInt32PropertyValue(int var1, int var2, IntByReference var3);

	byte CorsairGetInt32PropertyValue(int var1, int var2, IntBuffer var3);

	byte CorsairSubscribeForEvents(CorsairEventHandler var1, Pointer var2);

	byte CorsairUnsubscribeFromEvents();

	class CorsairKeyId
			extends PointerType
	{
		public CorsairKeyId(Pointer address)
		{
			super(address);
		}

		public CorsairKeyId()
		{
		}
	}

	interface CorsairRegisterKeypressCallback_CallbackType_callback
			extends Callback
	{
		void apply(Pointer var1, CorsairKeyId var2, byte var3);
	}

	interface CorsairSetLedsColorsAsync_CallbackType_callback
			extends Callback
	{
		void apply(Pointer var1, byte var2, int var3);
	}

	interface CorsairSetLedsColorsFlushBufferAsync_callback_callback
			extends Callback
	{
		void apply(Pointer var1, byte var2, int var3);
	}

	interface CorsairEventHandler
			extends Callback
	{
		void apply(Pointer var1, CorsairEvent var2);
	}

	interface CorsairEventId
	{
		int CEI_Invalid = 0;
		int CEI_DeviceConnectionStatusChangedEvent = 1;
		int CEI_KeyEvent = 2;
	}

	interface CorsairDevicePropertyId
	{
		int CDPI_Headset_MicEnabled = 4096;
		int CDPI_Headset_SurroundSoundEnabled = 4097;
		int CDPI_Headset_SidetoneEnabled = 4098;
		int CDPI_Headset_EqualizerPreset = 8192;
	}

	interface CorsairDevicePropertyType
	{
		int CDPT_Boolean = 4096;
		int CDPT_Int32 = 8192;
	}

	interface CorsairChannelDeviceType
	{
		int CCDT_Invalid = 0;
		int CCDT_HD_Fan = 1;
		int CCDT_SP_Fan = 2;
		int CCDT_LL_Fan = 3;
		int CCDT_ML_Fan = 4;
		int CCDT_Strip = 5;
		int CCDT_DAP = 6;
		int CCDT_Pump = 7;
	}

	interface CorsairError
	{
		int CE_Success = 0;
		int CE_ServerNotFound = 1;
		int CE_NoControl = 2;
		int CE_ProtocolHandshakeMissing = 3;
		int CE_IncompatibleProtocol = 4;
		int CE_InvalidArguments = 5;
	}

	interface CorsairAccessMode
	{
		int CAM_ExclusiveLightingControl = 0;
	}

	interface CorsairDeviceCaps
	{
		int CDC_None = 0;
		int CDC_Lighting = 1;
		int CDC_PropertyLookup = 2;
	}

	interface CorsairLogicalLayout
	{
		int CLL_Invalid = 0;
		int CLL_US_Int = 1;
		int CLL_NA = 2;
		int CLL_EU = 3;
		int CLL_UK = 4;
		int CLL_BE = 5;
		int CLL_BR = 6;
		int CLL_CH = 7;
		int CLL_CN = 8;
		int CLL_DE = 9;
		int CLL_ES = 10;
		int CLL_FR = 11;
		int CLL_IT = 12;
		int CLL_ND = 13;
		int CLL_RU = 14;
		int CLL_JP = 15;
		int CLL_KR = 16;
		int CLL_TW = 17;
		int CLL_MEX = 18;
	}

	interface CorsairPhysicalLayout
	{
		int CPL_Invalid = 0;
		int CPL_US = 1;
		int CPL_UK = 2;
		int CPL_BR = 3;
		int CPL_JP = 4;
		int CPL_KR = 5;
		int CPL_Zones1 = 6;
		int CPL_Zones2 = 7;
		int CPL_Zones3 = 8;
		int CPL_Zones4 = 9;
	}

	interface CorsairDeviceType
	{
		int CDT_Unknown = 0;
		int CDT_Mouse = 1;
		int CDT_Keyboard = 2;
		int CDT_Headset = 3;
		int CDT_MouseMat = 4;
		int CDT_HeadsetStand = 5;
		int CDT_CommanderPro = 6;
		int CDT_LightingNodePro = 7;
		int CDT_MemoryModule = 8;
		int CDT_Cooler = 9;
	}
}