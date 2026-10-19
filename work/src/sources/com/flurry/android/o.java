package com.flurry.android;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class o extends RelativeLayout {
    private u a;
    private Context b;
    private String c;
    private int d;
    private boolean e;
    private boolean f;

    o(u uVar, Context context, String str, int i) {
        super(context);
        this.a = uVar;
        this.b = context;
        this.c = str;
        this.d = i;
    }

    final void a() {
        if (!this.e) {
            y yVarC = c();
            if (yVarC != null) {
                removeAllViews();
                addView(yVarC, b());
                yVarC.a().a(new f((byte) 3, this.a.j()));
                this.e = true;
            } else if (!this.f) {
                TextView textView = new TextView(this.b);
                textView.setText(u.b);
                textView.setTextSize(1, 20.0f);
                addView(textView, b());
            }
            this.f = true;
        }
    }

    private static RelativeLayout.LayoutParams b() {
        return new RelativeLayout.LayoutParams(-1, -1);
    }

    private synchronized y c() {
        y yVar;
        List listA = this.a.a(this.b, Arrays.asList(this.c), null, this.d, false);
        if (listA.isEmpty()) {
            yVar = null;
        } else {
            yVar = (y) listA.get(0);
            yVar.setOnClickListener(this.a);
        }
        return yVar;
    }
}
