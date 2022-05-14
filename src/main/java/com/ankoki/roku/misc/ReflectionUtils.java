package com.ankoki.roku.misc;

import com.ankoki.roku.bukkit.BukkitImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Class for reflection utilities.
 * Also includes bukkit reflection, as we don't need any minecraft imports
 * for that as long as we set the bukkit version and whether we use new nms.
 */
public class ReflectionUtils {

    private static String MC_SERVER_VERSION = "null";
    private static boolean MC_NEW_NMS = false;

    /**
     * Sets the bukkit version. <p>
     * Roku does this in {@link BukkitImpl#onEnable()} so this doesn't need to be done by the user.
     */
    public static void setBukkitVersion(String version) {
        MC_SERVER_VERSION = version;
    }

    /**
     * Declares whether bukkit is using 1.17 or above.
     * Roku does this in {@link BukkitImpl#onEnable()} so this doesn't need to be done by the user.
     * @param bool true if bukkit is running 1.17 or above.
     */
    public static void setNewNms(boolean bool) {
        MC_NEW_NMS = bool;
    }

    /**
     * Gets a craft bukkit class by name.
     * {@link ReflectionUtils#setBukkitVersion(String)}
     *
     * @param className the class name.
     * @return the craft bukkit class.
     */
    public static Class<?> getCBClass(String className) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + MC_SERVER_VERSION + "." + className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the nms class depending on version.
     * {@link ReflectionUtils#setBukkitVersion(String)}
     * {@link ReflectionUtils#setNewNms(boolean)}
     *
     * @param nmsClass the nms class.
     * @param nmsPackage the nms package.
     * @return the nms class.
     */
    public static Class<?> getNMSClass(String nmsClass, String nmsPackage) {
        try {
            String name = MC_NEW_NMS ? nmsPackage + "." + nmsClass : "net.minecraft.server." + MC_SERVER_VERSION + "." + nmsClass;
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a static field from a class.
     * @param clazz the class to search.
     * @param name the name of the field.
     * @return the field's value.
     */
    public static Object getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a field from a class.
     * @param clazz the class to search.
     * @param name the name of the field.
     * @param object the object to get the field from.
     * @return the field's value.
     */
    public static Object getField(Class<?> clazz, String name, Object object) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Sets a static field for a class.
     * @param clazz the class to search.
     * @param name the name of the field.
     * @param value the value to set it to.
     */
    public static void setField(Class<?> clazz, String name, Object value) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(null, value);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets a field for a class.
     * @param clazz the class to search.
     * @param name the name of the field.
     * @param object the object to set the field for.
     * @param value the value to set it to.
     */
    public static void setField(Class<?> clazz, String name, Object object, Object value) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets a method from the given class with the name and parameters.
     * @param clazz the class to search.
     * @param method the name of the method.
     * @param mappedMethod the name of the mapped method.
     * @param parameters the parameters of the method. Can be absent.
     * @return the method found.
     */
    public static Method getNMSMethod(Class<?> clazz, String method, String mappedMethod, Class<?>... parameters) {
        try {
            return clazz.getMethod(MC_NEW_NMS ? mappedMethod : method, parameters);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a method from the given class with the name and parameters.
     * @param clazz the class to search.
     * @param name the name of the method.
     * @param parameters the parameters of the method. Can be absent.
     * @return the method found.
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameters) {
        try {
            return clazz.getMethod(name, parameters);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Invokes a static method.
     * @param method the method to invoke the arguments on.
     * @param arguments the arguments to invoke.
     * @return the method's value.
     */
    public static Object invokeMethod(Method method, Object... arguments) {
        try {
            return method.invoke(null, arguments);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Invokes an instance method.
     * @param method the method to invoke the arguments on.
     * @param instance the instance to invoke it on.
     * @param arguments the arguments to invoke.
     * @return the method's value.
     */
    public static Object invokeInstanceMethod(Method method, Object instance, Object... arguments) {
        try {
            return method.invoke(instance, arguments);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a constructor.
     * @param clazz the class to get the constructor for.
     * @param parameters the parameters the constructor has.
     * @return the constructor.
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameters) {
        try {
            return clazz.getConstructor(parameters);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Invokes a constructor.
     * @param constructor the constructor to invoke.
     * @param parameters the parameters to use.
     * @return the new instance.
     */
    public static Object invokeConstructor(Constructor<?> constructor, Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
