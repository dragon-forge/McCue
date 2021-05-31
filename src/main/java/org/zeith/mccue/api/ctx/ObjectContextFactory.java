package org.zeith.mccue.api.ctx;

import com.zeitheron.hammercore.utils.color.ColorNamePicker;
import org.zeith.mccue.api.EFunc;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ObjectContextFactory
{
	public static final Supplier<ObjectContextFactory> EMPTY_FACTORY = () -> ObjectContextFactory.builder().build();
	private static final Map<Class<?>, Function<String, ? extends NBTBase>> TYPE_FACTORY = new HashMap();
	private static final Map<Class<?>, EFunc<NBTBase, String>> TO_STRING_FACTORY = new HashMap();
	private final Map<String, Class<?>> requiredFields;
	private final Map<String, Class<?>> optionalFields;
	private final Map<String, NBTBase> defaultValues;
	private final Map<String, Predicate<NBTBase>> constrains;

	public static <T> void registerTypeFactory(Class<T> type, Function<String, ? extends NBTBase> factory)
	{
		TYPE_FACTORY.put(type, factory);
	}

	public static void registerToStringFactory(EFunc<NBTBase, String> factory, Class... types)
	{
		for(Class type : types)
		{
			TO_STRING_FACTORY.put(type, factory);
		}
	}

	public ObjectContextFactory(Map<String, Class<?>> required, Map<String, Class<?>> optional, Map<String, NBTBase> defaults, Map<String, Predicate<NBTBase>> constrains)
	{
		this.requiredFields = required;
		this.optionalFields = optional;
		this.defaultValues = defaults;
		this.constrains = constrains;
	}

	public Set<String> getRequiredFields()
	{
		return this.requiredFields.keySet();
	}

	public Set<String> getOptionalFields()
	{
		return this.optionalFields.keySet();
	}

	public Stream<String> keyStream()
	{
		return Stream.concat(this.getRequiredFields().stream(), this.getOptionalFields().stream());
	}

	public boolean isLogical(String field)
	{
		Class<?> type = this.getType(field);
		return type != null && (Boolean.TYPE == type || Boolean.class == type);
	}

	public Class<?> getType(String field)
	{
		Class<?> type = this.requiredFields.get(field);
		if(type == null)
		{
			type = this.optionalFields.get(field);
		}
		return type;
	}

	public boolean isEmpty()
	{
		return this.requiredFields.isEmpty() && this.optionalFields.isEmpty();
	}

	public boolean hasField(String field)
	{
		return this.requiredFields.containsKey(field) || this.optionalFields.containsKey(field);
	}

	public NBTBase getDefault(String field)
	{
		return this.defaultValues.get(field);
	}

	public String toString(String field, NBTBase tag)
	{
		if(!this.hasField(field))
		{
			return "-";
		}
		if(tag == null)
		{
			tag = this.getDefault(field);
		}
		if(tag == null)
		{
			return "?";
		}
		EFunc<NBTBase, String> toStr = TO_STRING_FACTORY.get(this.getType(field));
		if(toStr != null)
		{
			return toStr.f(tag);
		}
		return tag.toString();
	}

	public ContextParseResult<? extends NBTBase> parseOrDefault(String field, String content) throws ContextFieldParseException
	{
		NBTBase value;
		NBTBase pval = null;
		try
		{
			Function<String, ? extends NBTBase> factory;
			Class<?> type = this.getType(field);
			if(type != null && (factory = TYPE_FACTORY.get(type)) != null)
			{
				pval = factory.apply(content);
			}
		} catch(Throwable err)
		{
			if(err instanceof ContextFieldParseException) throw (ContextFieldParseException) err;
			throw new ContextFieldParseException(err);
		}
		if(pval != null && this.constrains.containsKey(field) && !this.constrains.get(field).test(pval)) pval = null;
		if((value = pval) == null && (value = this.defaultValues.get(field)) != null && this.constrains.containsKey(field) && !this.constrains.get(field).test(value))
			return new ContextParseResult<>(field, null, false, false);
		return new ContextParseResult<>(field, value, value != null, value != null && pval == null);
	}

	public static Builder builder()
	{
		return new Builder();
	}

	static
	{
		ObjectContextFactory.registerTypeFactory(Boolean.TYPE, str -> new NBTTagByte((byte) (Boolean.parseBoolean(str) ? 1 : 0)));
		ObjectContextFactory.registerTypeFactory(Boolean.class, str -> new NBTTagByte((byte) (Boolean.parseBoolean(str) ? 1 : 0)));
		ObjectContextFactory.registerTypeFactory(Byte.TYPE, str -> new NBTTagByte(Byte.parseByte(str)));
		ObjectContextFactory.registerTypeFactory(Byte.class, str -> new NBTTagByte(Byte.parseByte(str)));
		ObjectContextFactory.registerTypeFactory(Short.TYPE, str -> new NBTTagShort(Short.parseShort(str)));
		ObjectContextFactory.registerTypeFactory(Short.class, str -> new NBTTagShort(Short.parseShort(str)));
		ObjectContextFactory.registerTypeFactory(Integer.TYPE, str -> new NBTTagInt(Integer.parseInt(str)));
		ObjectContextFactory.registerTypeFactory(Integer.class, str -> new NBTTagInt(Integer.parseInt(str)));
		ObjectContextFactory.registerTypeFactory(Float.TYPE, str -> new NBTTagFloat(Float.parseFloat(str)));
		ObjectContextFactory.registerTypeFactory(Float.class, str -> new NBTTagFloat(Float.parseFloat(str)));
		ObjectContextFactory.registerTypeFactory(Long.TYPE, str -> new NBTTagLong(Long.parseLong(str)));
		ObjectContextFactory.registerTypeFactory(Long.class, str -> new NBTTagLong(Long.parseLong(str)));
		ObjectContextFactory.registerTypeFactory(Double.TYPE, str -> new NBTTagDouble(Double.parseDouble(str)));
		ObjectContextFactory.registerTypeFactory(Double.class, str -> new NBTTagDouble(Double.parseDouble(str)));
		ObjectContextFactory.registerTypeFactory(String.class, s -> new NBTTagString(s));
		ObjectContextFactory.registerTypeFactory(Color.class, s ->
		{
			int radix = 10;
			if(s.startsWith("#"))
			{
				radix = 16;
				s = s.substring(1);
			}
			if(s.startsWith("0x"))
			{
				radix = 16;
				s = s.substring(2);
			}
			return new NBTTagInt(Integer.parseInt(s, radix));
		});
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagByte)
			{
				return ((NBTTagByte) tag).getByte() > 0 ? "true" : "false";
			}
			return null;
		}, Boolean.TYPE, Boolean.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagByte)
			{
				return Byte.toString(((NBTTagByte) tag).getByte());
			}
			return null;
		}, Byte.TYPE, Byte.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagShort)
			{
				return Short.toString(((NBTTagShort) tag).getShort());
			}
			return null;
		}, Short.TYPE, Short.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagInt)
			{
				return Integer.toString(((NBTTagInt) tag).getInt());
			}
			return null;
		}, Integer.TYPE, Integer.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagFloat)
			{
				return Float.toString(((NBTTagFloat) tag).getFloat());
			}
			return null;
		}, Float.TYPE, Float.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagLong)
			{
				return Long.toString(((NBTTagLong) tag).getLong());
			}
			return null;
		}, Long.TYPE, Long.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagDouble)
			{
				return Double.toString(((NBTTagDouble) tag).getDouble());
			}
			return null;
		}, Double.TYPE, Double.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagString)
			{
				return ((NBTTagString) tag).getString();
			}
			return null;
		}, String.class);
		ObjectContextFactory.registerToStringFactory(tag ->
		{
			if(tag instanceof NBTTagInt)
			{
				String i18cl;
				int c = ((NBTTagInt) tag).getInt();
				String cl = ColorNamePicker.getColorNameFromHex(c);
				return "#" + Integer.toHexString(0xFF000000 | c).substring(2).toUpperCase() + " (" + I18n.format(I18n.hasKey(i18cl = "color." + cl.toLowerCase().replaceAll(" ", "_")) ? I18n.format(i18cl) : cl) + ")";
			}
			return null;
		}, Color.class);
	}

	public static class Builder
	{
		private final Map<String, Class<?>> requiredFields = new HashMap();
		private final Map<String, Class<?>> optionalFields = new HashMap();
		private final Map<String, NBTBase> defaultValues = new HashMap<String, NBTBase>();
		private final Map<String, Predicate<NBTBase>> constrains = new HashMap<String, Predicate<NBTBase>>();

		public <T> Builder require(String field, Class<T> type, NBTBase defaultValue)
		{
			if(defaultValue != null)
			{
				this.defaultValues.put(field, defaultValue);
			}
			return this.require(field, type);
		}

		public <T> Builder require(String field, Class<T> type)
		{
			if(this.requiredFields.containsKey(field))
			{
				throw new IllegalArgumentException("Field \"" + field + "\" is already mapped as \"REQUIRED\"");
			}
			if(this.optionalFields.containsKey(field))
			{
				throw new IllegalArgumentException("Field \"" + field + "\" is already mapped as \"OPTIONAL\"");
			}
			this.requiredFields.put(field, type);
			return this;
		}

		public <T> Builder optionally(String field, Class<T> type, NBTBase defaultValue)
		{
			if(this.requiredFields.containsKey(field))
			{
				throw new IllegalArgumentException("Field \"" + field + "\" is already mapped as \"REQUIRED\"");
			}
			if(this.optionalFields.containsKey(field))
			{
				throw new IllegalArgumentException("Field \"" + field + "\" is already mapped as \"OPTIONAL\"");
			}
			if(defaultValue != null)
			{
				this.defaultValues.put(field, defaultValue);
			}
			return this.optionally(field, type);
		}

		public <T> Builder optionally(String field, Class<T> type)
		{
			this.optionalFields.put(field, type);
			return this;
		}

		public Builder restrict(String field, Predicate<NBTBase> restriction)
		{
			this.constrains.put(field, restriction);
			return this;
		}

		public ObjectContextFactory build()
		{
			return new ObjectContextFactory(this.requiredFields, this.optionalFields, this.defaultValues, this.constrains);
		}
	}

	public static class ContextParseResult<T>
	{
		public final String field;
		public final boolean successful;
		public final boolean defaulted;
		public final T value;

		public ContextParseResult(String field, T value, boolean ok, boolean defaulted)
		{
			this.field = field;
			this.successful = ok;
			this.value = value;
			this.defaulted = defaulted;
		}
	}

	public static class ContextFieldParseException
			extends RuntimeException
	{
		public ContextFieldParseException(Throwable err)
		{
			super(err);
		}
	}
}