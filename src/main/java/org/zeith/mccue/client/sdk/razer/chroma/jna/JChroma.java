package org.zeith.mccue.client.sdk.razer.chroma.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import org.zeith.mccue.client.sdk.razer.chroma.jna.devices.DeviceInfos;
import org.zeith.mccue.client.sdk.razer.chroma.jna.devices.GUIDStruct;
import org.zeith.mccue.client.sdk.razer.chroma.jna.effects.KeyboardEffect;

import java.util.UUID;

/**
 * Entry point of the API, allows to create effects for the device and query Razer devices
 */
public class JChroma
{
	
	private static JChroma instance;
	private final ChromaLib wrapper;
	
	private JChroma()
			throws UnsatisfiedLinkError
	{
		String libName = "RzChromaSDK";
		if(System.getProperty("os.arch").contains("64"))
			libName += "64";
		wrapper = (ChromaLib) Native.loadLibrary(libName, ChromaLib.class);
	}
	
	/**
	 * Returns the <code>JChroma</code> singleton. One must be warned that this method performs
	 * lazy initialisation and that loading the native files is done at initialisation
	 *
	 * @return The JChroma singleton
	 */
	public static JChroma getInstance()
			throws UnsatisfiedLinkError
	{
		if(instance == null)
			instance = new JChroma();
		return instance;
	}
	
	private void throwIfError(int err, String method)
	{
		if(err != 0)
			throw new JChromaException(method, err);
	}
	
	/**
	 * Initialises the ChromaSDK.
	 *
	 * @throws JChromaException
	 * 		If there is any error while initialisation
	 */
	public void init()
	{
		int err = wrapper.Init();
		throwIfError(err, "init()");
	}
	
	/**
	 * Frees the ChromaSDK. Cleans up memory and let other applications take control
	 *
	 * @throws JChromaException
	 * 		If there is any error while freeing
	 */
	public void free()
	{
		int err = wrapper.UnInit();
		throwIfError(err, "free()");
	}
	
	/**
	 * Creates a keyboard effect on the currently plugged keyboard. The effect is immediately activated when calling this method.<br/>
	 * <b>Warning:</b> Any call to <code>createKeyboardEffect</code> will replace the current effect!
	 *
	 * @param effect
	 * 		The effect to create
	 *
	 * @throws JChromaException
	 * 		If the parameters of the effect are invalid or the effect is not supported
	 */
	public void createKeyboardEffect(KeyboardEffect effect)
	{
		Structure param = effect.createParameter();
		int err;
		if(param == null)
		{
			err = wrapper.CreateKeyboardEffect(effect.getType().ordinal(), Pointer.NULL, Pointer.NULL);
		} else
		{
			param.write();
			err = wrapper.CreateKeyboardEffect(effect.getType().ordinal(), param.getPointer(), Pointer.NULL);
		}
		throwIfError(err, "createKeyboardEffect(" + effect.getType().name() + ")");
	}
	
	/**
	 * Query for device informations
	 *
	 * @param deviceID
	 * 		The device to query
	 *
	 * @return The informations about the device
	 */
	public DeviceInfos queryDevice(UUID deviceID)
	{
		DeviceInfos deviceInfos = new DeviceInfos();
		queryDevice(deviceID, deviceInfos);
		return deviceInfos;
	}
	
	/**
	 * Query for device informations
	 *
	 * @param deviceID
	 * 		The device to query
	 * @param deviceInfos
	 * 		The object to store the infos in
	 */
	public void queryDevice(UUID deviceID, DeviceInfos deviceInfos)
	{
		DeviceInfos.DeviceInfosStruct struct = deviceInfos.getUnderlyingStructure();
		int err = wrapper.QueryDevice(createGUID(deviceID), struct);
		throwIfError(err, "queryDevice");
		struct.read();
	}
	
	/**
	 * Tells if a given device is connected. If the query returns an error, this method will simply return <code>false</code>
	 *
	 * @param deviceID
	 * 		The device ID
	 *
	 * @return If the given device is connected
	 */
	public boolean isDeviceConnected(UUID deviceID)
	{
		try
		{
			return queryDevice(deviceID).isConnected();
		} catch(JChromaException e)
		{
			return false;
		}
	}
	
	/**
	 * Converts a UUID to a GUID struct
	 *
	 * @param uuid
	 *
	 * @return
	 */
	private GUIDStruct createGUID(UUID uuid)
	{
		GUIDStruct struct = new GUIDStruct();
		struct.data1 = (int) (uuid.getMostSignificantBits() >> 32);
		struct.data2 = (short) ((uuid.getMostSignificantBits() >> 16) & 0xFFFF);
		struct.data3 = (short) ((uuid.getMostSignificantBits()) & 0xFFFF);
		struct.data4 = longToBytes(uuid.getLeastSignificantBits());
		
		struct.write();
		return struct;
	}
	
	public byte[] longToBytes(long x)
	{
		byte[] bytes = new byte[Long.BYTES];
		for(int i = 0; i < Long.BYTES; i++)
		{
			bytes[Long.BYTES - i - 1] = (byte) (x >> (i * 8) & 0xFF);
		}
		return bytes;
	}
}
