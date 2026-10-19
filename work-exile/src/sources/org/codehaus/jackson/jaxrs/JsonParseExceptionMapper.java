package org.codehaus.jackson.jaxrs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.JsonParseException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {
    public Response toResponse(JsonParseException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).type("text/plain").build();
    }
}
