package org.codehaus.jackson.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;

/* JADX INFO: loaded from: classes.dex */
@Produces({"application/json", "text/json"})
@Provider
@Consumes({"application/json", "text/json"})
public class JacksonJaxbJsonProvider extends JacksonJsonProvider {
    public static final Annotations[] DEFAULT_ANNOTATIONS = {Annotations.JACKSON, Annotations.JAXB};

    public JacksonJaxbJsonProvider() {
        this(null, DEFAULT_ANNOTATIONS);
    }

    public JacksonJaxbJsonProvider(Annotations... annotationsToUse) {
        this(null, annotationsToUse);
    }

    public JacksonJaxbJsonProvider(ObjectMapper mapper, Annotations[] annotationsToUse) {
        super(mapper, annotationsToUse);
    }
}
