package com.millennialmedia.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import java.util.HashMap;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMNotification extends MMJSObject implements DialogInterface.OnClickListener {
    private int index;

    MMNotification() {
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0010 A[Catch: all -> 0x001b, TryCatch #0 {, blocks: (B:6:0x0005, B:9:0x000a, B:12:0x0010, B:13:0x0013), top: B:19:0x0005 }] */
    /* JADX WARN: Code duplicated, block: B:9:0x000a A[Catch: all -> 0x001b, TryCatch #0 {, blocks: (B:6:0x0005, B:9:0x000a, B:12:0x0010, B:13:0x0013), top: B:19:0x0005 }] */
    @Override // android.content.DialogInterface.OnClickListener
    public synchronized void onClick(DialogInterface dialog, int which) {
        if (which == -2) {
            this.index = 0;
            if (which == -3) {
                this.index = 1;
            }
            if (which == -1) {
                this.index = 2;
            }
            dialog.cancel();
            notify();
        } else {
            if (which == -3) {
                this.index = 1;
            }
            if (which == -1) {
                this.index = 2;
            }
            dialog.cancel();
            notify();
        }
        throw th;
    }

    public synchronized MMJSResponse alert(final HashMap<String, String> arguments) {
        MMJSResponse response;
        final Context context = this.contextRef.get();
        if (context != null) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMNotification.1
                @Override // java.lang.Runnable
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    if (arguments.containsKey("title")) {
                        alertDialog.setTitle((CharSequence) arguments.get("title"));
                    }
                    if (arguments.containsKey("message")) {
                        alertDialog.setMessage((CharSequence) arguments.get("message"));
                    }
                    if (arguments.containsKey("cancelButton")) {
                        alertDialog.setButton(-2, (CharSequence) arguments.get("cancelButton"), MMNotification.this);
                    }
                    if (arguments.containsKey("buttons")) {
                        String[] buttons = ((String) arguments.get("buttons")).split(",");
                        if (buttons.length > 0) {
                            alertDialog.setButton(-3, buttons[0], MMNotification.this);
                        }
                        if (buttons.length > 1) {
                            alertDialog.setButton(-1, buttons[1], MMNotification.this);
                        }
                    }
                    alertDialog.show();
                }
            });
            try {
                wait();
                response = new MMJSResponse();
                response.result = 1;
                response.response = new Integer(this.index);
            } catch (Exception e) {
                e.printStackTrace();
                response = null;
            }
        } else {
            response = null;
        }
        return response;
    }

    public MMJSResponse vibrate(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        long time = 0;
        if (arguments.containsKey("duration")) {
            time = (long) (((double) Float.parseFloat(arguments.get("duration"))) * 1000.0d);
        }
        if (context != null && time > 0) {
            if (context.getPackageManager().checkPermission("android.permission.VIBRATE", context.getPackageName()) == 0) {
                Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
                vibrator.vibrate(time);
                return MMJSResponse.responseWithSuccess();
            }
            return MMJSResponse.responseWithError("The required permissions to vibrate are not set.");
        }
        return null;
    }
}
