package com.flurry.android;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class w extends LinearLayout {
    private View a;
    private List b;
    private boolean c;
    private /* synthetic */ CatalogActivity d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w(CatalogActivity catalogActivity, Context context) {
        super(context);
        this.d = catalogActivity;
        this.b = new ArrayList();
        this.c = true;
        setOrientation(1);
        setGravity(48);
        this.a = new s(catalogActivity, context);
        this.a.setId(10002);
        this.a.setOnClickListener(catalogActivity);
        a(a(context), this.c);
    }

    final List a(Context context) {
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= 3; i++) {
            arrayList.add("Flurry_Canvas_Hook_" + i);
        }
        v vVar = this.d.f == null ? null : this.d.f.b;
        List listA = this.d.e.a(context, arrayList, vVar != null ? Long.valueOf(vVar.a) : null, 1, true);
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            ((y) it.next()).setOnClickListener(this.d);
        }
        return listA;
    }

    final void a() {
        this.c = !this.c;
        a(null, this.c);
    }

    final void a(List list) {
        a(list, this.c);
    }

    private void a(List list, boolean z) {
        removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, 0, 0, 0);
        addView(this.a, layoutParams);
        if (list != null) {
            this.b.clear();
            this.b.addAll(list);
        }
        if (z) {
            for (y yVar : this.b) {
                addView(yVar, layoutParams);
                yVar.a().a(new f((byte) 3, this.d.e.j()));
            }
        }
        refreshDrawableState();
    }

    final List b() {
        return this.b;
    }
}
