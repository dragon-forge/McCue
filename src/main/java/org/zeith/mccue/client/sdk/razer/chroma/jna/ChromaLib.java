package org.zeith.mccue.client.sdk.razer.chroma.jna;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import org.zeith.mccue.client.sdk.razer.chroma.jna.devices.DeviceInfos;
import org.zeith.mccue.client.sdk.razer.chroma.jna.devices.GUIDStruct;

/**
 * Wrapper used by JNA to load Razer Chroma SDK libraries.
 */
interface ChromaLib extends Library {

    int Init();
    int UnInit();
    int CreateKeyboardEffect(int type, Pointer param, Pointer effectID);
    int QueryDevice(GUIDStruct struct, DeviceInfos.DeviceInfosStruct infos);

}
