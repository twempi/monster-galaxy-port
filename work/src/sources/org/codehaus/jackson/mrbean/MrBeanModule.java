package org.codehaus.jackson.mrbean;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.Module;

/* JADX INFO: loaded from: classes.dex */
public class MrBeanModule extends Module {
    private static final Version VERSION = new Version(1, 8, 0, null);
    private final String NAME;
    protected AbstractTypeMaterializer _materializer;

    public MrBeanModule() {
        this(new AbstractTypeMaterializer());
    }

    public MrBeanModule(AbstractTypeMaterializer materializer) {
        this.NAME = "MrBeanModule";
        this._materializer = materializer;
    }

    @Override // org.codehaus.jackson.map.Module
    public String getModuleName() {
        return "MrBeanModule";
    }

    @Override // org.codehaus.jackson.map.Module, org.codehaus.jackson.Versioned
    public Version version() {
        return VERSION;
    }

    @Override // org.codehaus.jackson.map.Module
    public void setupModule(Module.SetupContext context) {
        context.addAbstractTypeResolver(this._materializer);
    }
}
