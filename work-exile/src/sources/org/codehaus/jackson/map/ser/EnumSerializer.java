package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
@JacksonStdImpl
public class EnumSerializer extends ScalarSerializerBase<Enum<?>> {
    protected final EnumValues _values;

    public EnumSerializer(EnumValues v) {
        super(Enum.class, false);
        this._values = v;
    }

    public static EnumSerializer construct(Class<Enum<?>> enumClass, SerializationConfig config, BasicBeanDescription beanDesc) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        EnumValues v = config.isEnabled(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING) ? EnumValues.constructFromToString(enumClass, intr) : EnumValues.constructFromName(enumClass, intr);
        return new EnumSerializer(v);
    }

    @Override // org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.map.JsonSerializer
    public final void serialize(Enum<?> en, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(this._values.serializedValueFor(en));
    }

    @Override // org.codehaus.jackson.map.ser.ScalarSerializerBase, org.codehaus.jackson.map.ser.SerializerBase, org.codehaus.jackson.schema.SchemaAware
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        ObjectNode objectNode = createSchemaNode("string", true);
        if (typeHint != null) {
            JavaType type = provider.constructType(typeHint);
            if (type.isEnumType()) {
                ArrayNode enumNode = objectNode.putArray("enum");
                for (SerializedString value : this._values.values()) {
                    enumNode.add(value.getValue());
                }
            }
        }
        return objectNode;
    }

    public EnumValues getEnumValues() {
        return this._values;
    }
}
