package com.crittercism;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crittercism.app.Crittercism;

/* JADX INFO: loaded from: classes.dex */
public class NotificationActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        finish();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("com.crittercism.notification")) {
            return;
        }
        setTheme(R.style.Theme.Dialog);
        requestWindowFeature(1);
        String string = extras.getString("com.crittercism.notification");
        Crittercism crittercismA = Crittercism.a();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout.setOrientation(0);
        int iT = crittercismA.t();
        linearLayout.setPadding(iT, iT, iT, iT);
        linearLayout.setId(13);
        linearLayout.setOnClickListener(this);
        linearLayout.setOnTouchListener(this);
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(0, 0, crittercismA.t(), 0);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(16.0f);
        textView.setTextColor(-1);
        textView.setId(51);
        textView.setText(Crittercism.getNotificationTitle() + ": " + string);
        linearLayout.addView(textView);
        setContentView(linearLayout);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        finish();
        return true;
    }
}
