package org.testinfected.petstore.jdbc;

import org.testinfected.petstore.ExceptionImposter;

import java.lang.reflect.Field;

public class Property<T>  {

    private final Object entity;
    private final Field field;

    public Property(Object entity, String name) {
        this.entity = entity;
        this.field = lookupField(name);
    }

    private Field lookupField(final String name) {
        Class<?> type = entity.getClass();
        while (type != Object.class) {
            Field f = findField(type, name);
            if (f != null) return f;
            type = type.getSuperclass();
        }
        throw new IllegalArgumentException(entity.getClass().getName() + " has no field '" + name + "'");
    }

    private static Field findField(Class<?> type, String name) {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) return makeAccessible(field);
        }
        return null;
    }

    private static Field makeAccessible(Field field) {
        field.setAccessible(true);
        return field;
    }

    public void set(T value) {
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T get() {
        try {
            return (T) (field.get(entity));
        } catch (IllegalAccessException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}
