package com.pyxis.petstore.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class EntityToStringStyle extends ToStringStyle {
    public static final ToStringStyle INSTANCE = new EntityToStringStyle();

    public EntityToStringStyle() {
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
        setContentStart(": { ");
        setContentEnd(" }");
        setFieldNameValueSeparator(": ");
        setFieldSeparator(", ");
        setArrayStart("[");
        setArrayEnd("[");
    }

    public static String reflectionToString(Object entity) {
        return ToStringBuilder.reflectionToString(entity, INSTANCE);
    }
}