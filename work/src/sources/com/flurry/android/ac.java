package com.flurry.android;

import android.view.View;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
final class ac implements View.OnFocusChangeListener {
    private /* synthetic */ TextView a;
    private /* synthetic */ ab b;

    ac(ab abVar, TextView textView) {
        this.b = abVar;
        this.a = textView;
    }

    @Override // android.view.View.OnFocusChangeListener
    public final void onFocusChange(View view, boolean z) {
        this.a.setText(z ? this.b.b : this.b.a);
    }
}
