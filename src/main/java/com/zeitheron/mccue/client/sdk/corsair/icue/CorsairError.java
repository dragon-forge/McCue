package com.zeitheron.mccue.client.sdk.corsair.icue;

public enum CorsairError
{
	CE_Success("Success"),
	CE_ServerNotFound("Server not found"),
	CE_NoControl("No control"),
	CE_ProtocolHandshakeMissing("Protocol handshake missing"),
	CE_IncompatibleProtocol("Incompatible protocol"),
	CE_InvalidArguments("Invalid arguments");

	private String message;
	private static CorsairError[] values;

	CorsairError(String message)
	{
		this.message = message;
	}

	public static CorsairError byOrdinal(int ordinal)
	{
		if(ordinal >= 0 && ordinal < values.length)
		{
			return values[ordinal];
		}
		return null;
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String toString()
	{
		return "CorsairError{" + this.message + "}";
	}

	static
	{
		values = CorsairError.values();
	}
}