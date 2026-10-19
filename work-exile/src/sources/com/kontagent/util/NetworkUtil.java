package com.kontagent.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.kontagent.Kontagent;
import com.kontagent.KontagentLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class NetworkUtil {
    public static boolean isOnline() {
        try {
            return httpGetStatusCode("http://api.geo.kontagent.net/api/v0/ping/") == 404;
        } catch (NetworkConnectivityError e) {
            KontagentLog.e("isOnline check threw exceoption", e);
            return false;
        }
    }

    public static String httpGet(String urlString) throws NetworkConnectivityError {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            if (urlConnection.getResponseCode() == 200) {
                BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), Opcodes.ACC_ANNOTATION);
                while (true) {
                    String strLine = input.readLine();
                    if (strLine == null) {
                        break;
                    }
                    response.append(strLine);
                }
                input.close();
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            KontagentLog.e("Problem connecting to url " + urlString, e);
            if (Kontagent.debugEnabled().booleanValue()) {
                throw new RuntimeException(e);
            }
        } catch (IOException e2) {
            KontagentLog.e("Problem connecting to url " + urlString, e2);
            throw new NetworkConnectivityError(e2);
        }
        return response.toString();
    }

    private static int httpGetStatusCode(String urlString) throws NetworkConnectivityError {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            KontagentLog.d("Connection to " + urlString + "returned response code " + Integer.toString(urlConnection.getResponseCode()));
            return urlConnection.getResponseCode();
        } catch (MalformedURLException e) {
            KontagentLog.d("Problem connecting to url " + urlString);
            return 0;
        } catch (IOException e2) {
            KontagentLog.d("Problem connecting to url " + urlString);
            return 0;
        }
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress().toString();
                        KontagentLog.d("Current ip address is " + ip);
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            KontagentLog.e(ex.toString(), ex);
        }
        return null;
    }

    public static boolean isValidIpAddress() {
        return (getLocalIpAddress() == null || getLocalIpAddress().equals("")) ? false : true;
    }

    public static String carrierName(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService("phone");
        return manager.getNetworkOperatorName();
    }

    public static boolean isValidCarrierName(Context context) {
        String name = carrierName(context);
        if (name == null) {
            KontagentLog.d("Carrier name is null");
            return false;
        }
        KontagentLog.d("Carrier name is " + name);
        return !name.equals("");
    }
}
