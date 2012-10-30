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
        try {
            Field field = entity.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Entity has no field " + name + ": " + entity, e);
        }
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
