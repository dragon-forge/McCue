package org.zeith.mccue.client.sdk.corsair.icue.jna;

import com.sun.jna.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface CUESDKLibrary
		extends Library
{
	String JNA_LIBRARY_NAME = "CUESDK";
	NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(JNA_LIBRARY_NAME);
	CUESDKLibrary INSTANCE = Native.loadLibrary(JNA_LIBRARY_NAME, CUESDKLibrary.class);

	boolean CorsairSetLedsColors(int size, CorsairLedColor ledsColors);

	boolean CorsairSetLedsColorsBufferByDeviceIndex(int deviceIndex, int size, CorsairLedColor ledsColors);

	boolean CorsairSetLedsColorsFlushBuffer();

	boolean CorsairSetLedsColorsFlushBufferAsync(CUESDKLibrary.CorsairSetLedsColorsFlushBufferAsync_callback_callback callback, Pointer context);

	boolean CorsairGetLedsColors(int size, CorsairLedColor ledsColors);

	boolean CorsairGetLedsColorsByDeviceIndex(int deviceIndex, int size, CorsairLedColor ledsColors);

	boolean CorsairSetLedsColorsAsync(int size, CorsairLedColor ledsColors, CUESDKLibrary.CorsairSetLedsColorsAsync_CallbackType_callback CallbackType, Pointer context);

	int CorsairGetDeviceCount();

	CorsairDeviceInfo CorsairGetDeviceInfo(int deviceIndex);

	CorsairLedPositions CorsairGetLedPositions();

	CorsairLedPositions CorsairGetLedPositionsByDeviceIndex(int deviceIndex);

	int CorsairGetLedIdForKeyName(byte keyName);

	boolean CorsairRequestControl(int accessMode);

	CorsairProtocolDetails.ByValue CorsairPerformProtocolHandshake();

	int CorsairGetLastError();

	boolean CorsairReleaseControl(int accessMode);

	boolean CorsairSetLayerPriority(int priority);

	boolean CorsairRegisterKeypressCallback(CUESDKLibrary.CorsairRegisterKeypressCallback_CallbackType_callback CallbackType, Pointer context);

	boolean CorsairGetBoolPropertyValue(int deviceIndex, int propertyId, ByteBuffer propertyValue);

	boolean CorsairGetInt32PropertyValue(int deviceIndex, int propertyId, IntBuffer propertyValue);

	boolean CorsairSubscribeForEvents(CUESDKLibrary.CorsairEventHandler onEvent, Pointer context);

	boolean CorsairUnsubscribeFromEvents();

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
}