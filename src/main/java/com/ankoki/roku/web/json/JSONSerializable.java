package com.ankoki.roku.web.json;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an object that may be serialized into JSON.
 * </p>
 * These classes <bold>must</bold> provide a method to deserialize a map into the current object.
 * This method must be called `deserialize`, accepts a {@link Map}<{@link String}, {@link Object}> parameter, and returns a single value of the type.
 */
public abstract class JSONSerializable {

	private static Map<String, Class<? extends JSONSerializable>> registry = new ConcurrentHashMap<>();

	/**
	 * Registers a json serializable class.
	 *
	 * @param clazz the class to register.
	 */
	public static void register(Class<? extends JSONSerializable> clazz) {
		String alias = clazz.getPackageName() + "." + clazz.getName();
		if (JSONSerializable.registry.containsKey(alias))
			throw new IllegalArgumentException("Class '" + alias + "' is already a registered JSONSerializable.");
		JSONSerializable.registry.put(alias, clazz);
	}

	/**
	 * Gets a class instance by its full package and name if it is registered.
	 *
	 * @param name the name to look for.
	 * @return the class, or null if not found.
	 */
	@Nullable
	public static Class<? extends JSONSerializable> get(String name) {
		return registry.get(name);
	}

	/**
	 * Accesses the declared static deserialize method for the given class, using the map
	 * to get the value.
	 *
	 * @param clazz the class.
	 * @param map the map.
	 * @return the object, may be null.
	 */
	@Nullable
	protected static Object deserializeHelper(Class<? extends JSONSerializable> clazz, Map<String, Object> map) {
		try {
			Method method = clazz.getMethod("deserialize", Map.class);
			try {
				return method.invoke(null, map);
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
			}
		} catch (NoSuchMethodException ex) {
			throw new IllegalStateException("'" + clazz.getPackageName() + "." + clazz.getName() + "' does not have a static deserialize(Map<String, Object>) method. This is required.");
		}
		return null;
	}

	/**
	 * Serializes the current class into a map.
	 *
	 * @return the map containing all data.
	 */
	public abstract Map<String, Object> serialize();

}
