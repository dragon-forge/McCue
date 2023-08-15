package org.zeith.mccue.client.sdk.razer.chroma.jna;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.json.*;

public class JAppInfoType
		extends Structure
		implements Structure.ByReference
{
	public char[] title = new char[256]; //TCHAR Title[256];
	public char[] description = new char[1024]; //TCHAR Description[1024];
	public char[] authorName = new char[256]; //TCHAR Name[256];
	public char[] authorContact = new char[256]; //TCHAR Contact[256];
	public int supportedDevice; //DWORD SupportedDevice;
	public int category; //DWORD Category;
	
	public static JAppInfoType create(InputStream input)
			throws JSONException
	{
		try
		{
			JSONObject obj = new JSONTokener(input).nextValueOBJ()
					.orElseThrow(() -> new JSONException("Provided data is not a JSON Object."));
			
			JAppInfoType app = new JAppInfoType();
			
			app.setTitle(getField(obj, "title", 256))
					.setDescription(getField(obj, "description", 1024))
					.setAuthorName(getField(obj, "author", 256))
					.setAuthorContact(getField(obj, "url", 256));
			app.supportedDevice = obj.getInt("supported_device");
			app.category = 2;
			return app;
		} catch(IOException e)
		{
			throw (JSONException) new JSONException("Unable to read JSON object from stream.").initCause(e);
		}
	}
	
	public JAppInfoType setTitle(String val)
	{
		setField(title, val);
		return this;
	}
	
	public JAppInfoType setDescription(String val)
	{
		setField(description, val);
		return this;
	}
	
	public JAppInfoType setAuthorName(String val)
	{
		setField(authorName, val);
		return this;
	}
	
	public JAppInfoType setAuthorContact(String val)
	{
		setField(authorContact, val);
		return this;
	}
	
	@Override
	protected List<String> getFieldOrder()
	{
		return Arrays.asList("title", "description", "authorName", "authorContact", "supportedDevice", "category");
	}
	
	private static String getField(JSONObject obj, String field, int maxLength)
	{
		if(!obj.has(field)) throw new JSONException("Missing required field: " + field);
		String s = obj.getString(field);
		if(s.length() > maxLength)
			throw new JSONException("Field " + field + " is too long (" + s.length() + " > " + maxLength + ")");
		return s;
	}
	
	private static void setField(char[] field, String val)
	{
		if(val.length() > field.length)
		{
			return;
		}
		for(int i = 0; i < val.length(); ++i)
		{
			field[i] = val.charAt(i);
		}
		for(int i = val.length(); i < field.length; ++i)
		{
			field[i] = 0;
		}
	}
}