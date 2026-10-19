package com.badlogic.gdx.backends.android;

import android.content.Context;
import com.badlogic.gdx.utils.Clipboard;

/* JADX INFO: loaded from: classes.dex */
public class AndroidClipboard implements Clipboard {
    private String contents;
    Context context;

    protected AndroidClipboard(Context context) {
        this.context = context;
    }

    @Override // com.badlogic.gdx.utils.Clipboard
    public String getContents() {
        return this.contents;
    }

    @Override // com.badlogic.gdx.utils.Clipboard
    public void setContents(String contents) {
        this.contents = contents;
    }
}
