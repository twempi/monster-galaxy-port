package com.gaiaonline.monstergalaxy.platform;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.gaiaonline.monstergalaxy.R;

/* JADX INFO: loaded from: classes.dex */
public class MogaProgressDialog extends Dialog {
    public static MogaProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static MogaProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static MogaProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static synchronized MogaProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        MogaProgressDialog dialog;
        dialog = new MogaProgressDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        ImageView aImg = new ImageView(context);
        aImg.setImageResource(R.drawable.starseed);
        Animation progressAnimation = AnimationUtils.loadAnimation(context, R.anim.progressanimation);
        aImg.startAnimation(progressAnimation);
        dialog.addContentView(aImg, new ViewGroup.LayoutParams(-1, -1));
        dialog.show();
        return dialog;
    }

    public MogaProgressDialog(Context context) {
        super(context, R.style.NewDialog);
    }
}
