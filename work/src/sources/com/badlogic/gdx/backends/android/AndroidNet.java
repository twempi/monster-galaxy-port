package com.badlogic.gdx.backends.android;

import android.content.Intent;
import android.net.Uri;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

/* JADX INFO: loaded from: classes.dex */
public class AndroidNet implements Net {
    final AndroidApplication app;

    public AndroidNet(AndroidApplication activity) {
        this.app = activity;
    }

    @Override // com.badlogic.gdx.Net
    public Net.HttpResult httpGet(String url, String... parameters) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override // com.badlogic.gdx.Net
    public Net.HttpResult httpPost(String url, String contentType, byte[] content) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override // com.badlogic.gdx.Net
    public ServerSocket newServerSocket(Net.Protocol protocol, int port, ServerSocketHints hints) {
        return new AndroidServerSocket(protocol, port, hints);
    }

    @Override // com.badlogic.gdx.Net
    public Socket newClientSocket(Net.Protocol protocol, String host, int port, SocketHints hints) {
        return new AndroidSocket(protocol, host, port, hints);
    }

    @Override // com.badlogic.gdx.Net
    public void openURI(String URI) {
        final Uri uri = Uri.parse(URI);
        this.app.runOnUiThread(new Runnable() { // from class: com.badlogic.gdx.backends.android.AndroidNet.1
            @Override // java.lang.Runnable
            public void run() {
                AndroidNet.this.app.startActivity(new Intent("android.intent.action.VIEW", uri));
            }
        });
    }
}
