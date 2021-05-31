package org.zeith.mccue.client.sdk.logitech.lew.jna;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.lang.reflect.Field;

public final class KeyName
{
	public static final int ESC = 0x01;
	public static final int F1 = 0x3b;
	public static final int F2 = 0x3c;
	public static final int F3 = 0x3d;
	public static final int F4 = 0x3e;
	public static final int F5 = 0x3f;
	public static final int F6 = 0x40;
	public static final int F7 = 0x41;
	public static final int F8 = 0x42;
	public static final int F9 = 0x43;
	public static final int F10 = 0x44;
	public static final int F11 = 0x57;
	public static final int F12 = 0x58;
	public static final int PRINT_SCREEN = 0x137;
	public static final int SCROLL_LOCK = 0x46;
	public static final int PAUSE_BREAK = 0x145;
	public static final int TILDE = 0x29;
	public static final int ONE = 0x02;
	public static final int TWO = 0x03;
	public static final int THREE = 0x04;
	public static final int FOUR = 0x05;
	public static final int FIVE = 0x06;
	public static final int SIX = 0x07;
	public static final int SEVEN = 0x08;
	public static final int EIGHT = 0x09;
	public static final int NINE = 0x0A;
	public static final int ZERO = 0x0B;
	public static final int MINUS = 0x0C;
	public static final int EQUALS = 0x0D;
	public static final int BACKSPACE = 0x0E;
	public static final int INSERT = 0x152;
	public static final int HOME = 0x147;
	public static final int PAGE_UP = 0x149;
	public static final int NUM_LOCK = 0x45;
	public static final int NUM_SLASH = 0x135;
	public static final int NUM_ASTERISK = 0x37;
	public static final int NUM_MINUS = 0x4A;
	public static final int TAB = 0x0F;
	public static final int Q = 0x10;
	public static final int W = 0x11;
	public static final int E = 0x12;
	public static final int R = 0x13;
	public static final int T = 0x14;
	public static final int Y = 0x15;
	public static final int U = 0x16;
	public static final int I = 0x17;
	public static final int O = 0x18;
	public static final int P = 0x19;
	public static final int OPEN_BRACKET = 0x1A;
	public static final int CLOSE_BRACKET = 0x1B;
	public static final int BACKSLASH = 0x2B;
	public static final int KEYBOARD_DELETE = 0x153;
	public static final int END = 0x14F;
	public static final int PAGE_DOWN = 0x151;
	public static final int NUM_SEVEN = 0x47;
	public static final int NUM_EIGHT = 0x48;
	public static final int NUM_NINE = 0x49;
	public static final int NUM_PLUS = 0x4E;
	public static final int CAPS_LOCK = 0x3A;
	public static final int A = 0x1E;
	public static final int S = 0x1F;
	public static final int D = 0x20;
	public static final int F = 0x21;
	public static final int G = 0x22;
	public static final int H = 0x23;
	public static final int J = 0x24;
	public static final int K = 0x25;
	public static final int L = 0x26;
	public static final int SEMICOLON = 0x27;
	public static final int APOSTROPHE = 0x28;
	public static final int ENTER = 0x1C;
	public static final int NUM_FOUR = 0x4B;
	public static final int NUM_FIVE = 0x4C;
	public static final int NUM_SIX = 0x4D;
	public static final int LEFT_SHIFT = 0x2A;
	public static final int Z = 0x2C;
	public static final int X = 0x2D;
	public static final int C = 0x2E;
	public static final int V = 0x2F;
	public static final int B = 0x30;
	public static final int N = 0x31;
	public static final int M = 0x32;
	public static final int COMMA = 0x33;
	public static final int PERIOD = 0x34;
	public static final int FORWARD_SLASH = 0x35;
	public static final int RIGHT_SHIFT = 0x36;
	public static final int ARROW_UP = 0x148;
	public static final int NUM_ONE = 0x4F;
	public static final int NUM_TWO = 0x50;
	public static final int NUM_THREE = 0x51;
	public static final int NUM_ENTER = 0x11C;
	public static final int LEFT_CONTROL = 0x1D;
	public static final int LEFT_WINDOWS = 0x15B;
	public static final int LEFT_ALT = 0x38;
	public static final int SPACE = 0x39;
	public static final int RIGHT_ALT = 0x138;
	public static final int RIGHT_WINDOWS = 0x15C;
	public static final int APPLICATION_SELECT = 0x15D;
	public static final int RIGHT_CONTROL = 0x11D;
	public static final int ARROW_LEFT = 0x14B;
	public static final int ARROW_DOWN = 0x150;
	public static final int ARROW_RIGHT = 0x14D;
	public static final int NUM_ZERO = 0x52;
	public static final int NUM_PERIOD = 0x53;
	public static final int G_1 = 0xFFF1;
	public static final int G_2 = 0xFFF2;
	public static final int G_3 = 0xFFF3;
	public static final int G_4 = 0xFFF4;
	public static final int G_5 = 0xFFF5;
	public static final int G_6 = 0xFFF6;
	public static final int G_7 = 0xFFF7;
	public static final int G_8 = 0xFFF8;
	public static final int G_9 = 0xFFF9;
	public static final int G_LOGO = 0xFFFF1;
	public static final int G_BADGE = 0xFFFF2;

	private static final int[] VALUES;

	public static int[] values()
	{
		return VALUES;
	}

	static
	{
		IntArrayList ints = new IntArrayList();
		// collect keys
		for(Field f : KeyName.class.getDeclaredFields())
			if(int.class.isAssignableFrom(f.getType()))
			{
				try
				{
					ints.add(f.getInt(null));
				} catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		VALUES = ints.toIntArray();
	}
}