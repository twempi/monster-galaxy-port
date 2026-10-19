package com.flurry.android;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CatalogActivity extends Activity implements View.OnClickListener {
    private static volatile String a = "<html><body><table height='100%' width='100%' border='0'><tr><td style='vertical-align:middle;text-align:center'>No recommendations available<p><button type='input' onClick='activity.finish()'>Back</button></td></tr></table></body></html>";
    private WebView b;
    private w c;
    private List d = new ArrayList();
    private u e;
    private p f;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        Long lValueOf;
        setTheme(R.style.Theme.Translucent);
        super.onCreate(bundle);
        this.e = FlurryAgent.b();
        Intent intent = getIntent();
        if (intent.getExtras() != null && (lValueOf = Long.valueOf(intent.getExtras().getLong("o"))) != null) {
            this.f = this.e.b(lValueOf.longValue());
        }
        ab abVar = new ab(this, this);
        abVar.setId(1);
        abVar.setBackgroundColor(-16777216);
        this.b = new WebView(this);
        this.b.setId(2);
        this.b.setScrollBarStyle(0);
        this.b.setBackgroundColor(-1);
        if (this.f != null) {
            this.b.setWebViewClient(new q(this));
        }
        this.b.getSettings().setJavaScriptEnabled(true);
        this.b.addJavascriptInterface(this, "activity");
        this.c = new w(this, this);
        this.c.setId(3);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(10, abVar.getId());
        relativeLayout.addView(abVar, layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.addRule(3, abVar.getId());
        layoutParams2.addRule(2, this.c.getId());
        relativeLayout.addView(this.b, layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams3.addRule(12, abVar.getId());
        relativeLayout.addView(this.c, layoutParams3);
        Bundle extras = getIntent().getExtras();
        String string = extras == null ? null : extras.getString("u");
        if (string == null) {
            this.b.loadDataWithBaseURL(null, a, "text/html", "utf-8", null);
        } else {
            this.b.loadUrl(string);
        }
        setContentView(relativeLayout);
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.e.g();
        super.onDestroy();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view instanceof y) {
            x xVar = new x();
            xVar.a = this.b.getUrl();
            xVar.b = new ArrayList(this.c.b());
            this.d.add(xVar);
            if (this.d.size() > 5) {
                this.d.remove(0);
            }
            x xVar2 = new x();
            y yVar = (y) view;
            this.f = this.e.b(yVar.a());
            yVar.a(this.f);
            xVar2.a = this.e.i() + this.e.a(yVar.a());
            xVar2.b = this.c.a(view.getContext());
            a(xVar2);
            return;
        }
        if (view.getId() == 10000) {
            finish();
            return;
        }
        if (view.getId() == 10002) {
            this.c.a();
        } else if (this.d.isEmpty()) {
            finish();
        } else {
            a((x) this.d.remove(this.d.size() - 1));
        }
    }

    private void a(x xVar) {
        try {
            this.b.loadUrl(xVar.a);
            this.c.a(xVar.b);
        } catch (Exception e) {
            ah.a("FlurryAgent", "Error loading url: " + xVar.a);
        }
    }
}
