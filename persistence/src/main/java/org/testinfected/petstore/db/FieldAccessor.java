package org.testinfected.petstore.db;

import java.lang.reflect.Field;

public class FieldAccessor<T>  {

    public static <T> FieldAccessor<T> access(Object entity, String fieldName) {
        Field field = findField(entity, fieldName);
        if (field == null) throw new IllegalArgumentException(entity.getClass().getName() + " has no field '" + fieldName + "'");
        return new FieldAccessor<>(entity, field);
    }

    public static Field findField(Object object, final String fieldName) {
        Class<?> type = object.getClass();
        while (type != Object.class) {
            Field f = field(type, fieldName);
            if (f != null) return f;
            type = type.getSuperclass();
        }
        return null;
    }

    public static Field field(Class<?> type, String fieldName) {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) return field;
        }
        return null;
    }

    private final Object entity;
    private final Field field;

    public FieldAccessor(Object entity, Field field) {
        this.entity = entity;
        this.field = field;
    }

    public void set(T value) {
        try {
            madeAccessible(field).set(entity, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    public T get() {
        try {
            return (T) madeAccessible(field).get(entity);
        } catch (IllegalAccessException ignored) {
            return null;
        }
    }

    private Field madeAccessible(Field field) {
        field.setAccessible(true);
        return field;
    }
}
