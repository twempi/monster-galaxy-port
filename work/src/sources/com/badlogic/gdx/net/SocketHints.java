package com.badlogic.gdx.net;

import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public class SocketHints {
    public int connectTimeout = 5000;
    public int performancePrefConnectionTime = 0;
    public int performancePrefLatency = 1;
    public int performancePrefBandwidth = 0;
    public int trafficClass = 20;
    public boolean keepAlive = true;
    public boolean tcpNoDelay = true;
    public int sendBufferSize = Opcodes.ACC_SYNTHETIC;
    public int receiveBufferSize = Opcodes.ACC_SYNTHETIC;
    public boolean linger = false;
    public int lingerDuration = 0;
}
