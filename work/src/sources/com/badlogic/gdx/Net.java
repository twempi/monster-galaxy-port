package com.badlogic.gdx;

import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

/* JADX INFO: loaded from: classes.dex */
public interface Net {

    public interface HttpResult {
        void cancel();

        float getProgress();

        byte[] getResult(int i);

        String getResultAsString(int i);

        boolean isAborted();

        boolean isDone();
    }

    public enum Protocol {
        TCP
    }

    HttpResult httpGet(String str, String... strArr);

    HttpResult httpPost(String str, String str2, byte[] bArr);

    Socket newClientSocket(Protocol protocol, String str, int i, SocketHints socketHints);

    ServerSocket newServerSocket(Protocol protocol, int i, ServerSocketHints serverSocketHints);

    void openURI(String str);
}
