package org.codehaus.jackson.map;

import java.io.IOException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;

/* JADX INFO: loaded from: classes.dex */
public class MappingJsonFactory extends JsonFactory {
    public MappingJsonFactory() {
        this(null);
    }

    public MappingJsonFactory(ObjectMapper mapper) {
        super(mapper);
        if (mapper == null) {
            setCodec(new ObjectMapper(this));
        }
    }

    @Override // org.codehaus.jackson.JsonFactory
    public final ObjectMapper getCodec() {
        return (ObjectMapper) this._objectCodec;
    }

    @Override // org.codehaus.jackson.JsonFactory
    public String getFormatName() {
        return JsonFactory.FORMAT_NAME_JSON;
    }

    @Override // org.codehaus.jackson.JsonFactory
    public MatchStrength hasFormat(InputAccessor acc) throws IOException {
        return hasJSONFormat(acc);
    }
}
