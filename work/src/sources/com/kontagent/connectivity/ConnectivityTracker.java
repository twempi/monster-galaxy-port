package com.kontagent.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import com.kontagent.KontagentLog;

/* JADX INFO: loaded from: classes.dex */
public class ConnectivityTracker extends BroadcastReceiver {
    private static final String TAG = ConnectivityTracker.class.getSimpleName();
    private boolean mConnected;
    private int mConnectivityCounter;
    private final Context mContext;
    private NetworkInfo mLastNetworkInfo;
    private ConnectivityListener mListener;

    public ConnectivityTracker(Context ctx) {
        this.mConnectivityCounter = 0;
        this.mContext = ctx.getApplicationContext();
        this.mConnectivityCounter = 0;
    }

    public void setListener(ConnectivityListener listener) {
        this.mListener = listener;
        if (this.mListener != null) {
            this.mListener.onConnectivityChanged(this, this.mConnected);
        }
    }

    public ConnectivityListener getListener() {
        return this.mListener;
    }

    public void startTracking() {
        KontagentLog.d(TAG, "Started tracking connectivity.");
        this.mContext.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.mContext.registerReceiver(this, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
    }

    public void stopTracking() {
        KontagentLog.d(TAG, "Stopped tracking connectivity.");
        this.mContext.unregisterReceiver(this);
    }

    public void resetCounter() {
        KontagentLog.d(TAG, "Reseting connectivity counter (was " + this.mConnectivityCounter + ")");
        this.mConnectivityCounter = 0;
    }

    public int getCounter() {
        return this.mConnectivityCounter;
    }

    public ConnectivitySnapshot snapshot() {
        return new ConnectivitySnapshot(this);
    }

    public boolean isOnline() {
        return this.mConnected;
    }

    public boolean isOffline() {
        return !this.mConnected;
    }

    public NetworkInfo getLastNetworkInfo() {
        return this.mLastNetworkInfo;
    }

    protected void setConnectedState(boolean connected) {
        if (this.mConnected != connected) {
            KontagentLog.d(TAG, "Connectivity state changed. Connected: " + connected);
            this.mConnected = connected;
            if (this.mListener != null) {
                this.mListener.onConnectivityChanged(this, this.mConnected);
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
            KontagentLog.d("Airplane mode broadcast received.");
            this.mConnectivityCounter++;
        } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            KontagentLog.d("Connectivity broadcast received.");
            this.mConnectivityCounter++;
            this.mLastNetworkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            boolean noConnectivity = intent.getBooleanExtra("noConnectivity", false);
            if (noConnectivity) {
                setConnectedState(false);
            } else {
                setConnectedState(this.mLastNetworkInfo.isConnected());
            }
        }
    }
}
