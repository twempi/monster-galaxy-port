package com.tapjoy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import oauth.signpost.OAuth;
import org.codehaus.jackson.smile.SmileConstants;
import org.w3c.dom.Document;

/* JADX INFO: loaded from: classes.dex */
public class TapjoyDisplayAd {
    private static final int MASK_8BITS = 255;
    private static String adClickURL;
    private static TapjoyDisplayAdNotifier displayAdNotifier;
    private static String displayAdSize;
    public static String displayAdURLParams;
    final String TAPJOY_DISPLAY_AD = "Display Ad";
    View adView;
    private boolean autoRefresh;
    Bitmap bitmapImage;
    private byte[] buffer;
    private Context context;
    private boolean eof;
    private int modulus;
    private int pos;
    Timer timer;
    private int x;
    private static TapjoyURLConnection tapjoyURLConnection = null;
    private static final byte PAD = 61;
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, SmileConstants.TOKEN_KEY_LONG_STRING, 53, 54, 55, 56, 57, SmileConstants.HEADER_BYTE_1, 59, 60, PAD, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, 36, 37, 38, 39, 40, SmileConstants.HEADER_BYTE_2, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

    public TapjoyDisplayAd(Context ctx) {
        displayAdSize = TapjoyDisplayAdSize.TJC_AD_BANNERSIZE_320X50;
        this.context = ctx;
        tapjoyURLConnection = new TapjoyURLConnection();
    }

    public void setBannerAdSize(String dimensions) {
        displayAdSize = dimensions;
    }

    public String getBannerAdSize() {
        return displayAdSize;
    }

    public void enableAutoRefresh(boolean shouldAutoRefresh) {
        this.autoRefresh = shouldAutoRefresh;
    }

    public void getDisplayAd(TapjoyDisplayAdNotifier notifier) {
        TapjoyLog.i("Display Ad", "Get display ad");
        getDisplayAd(null, notifier);
    }

    public void getDisplayAd(String currencyID, TapjoyDisplayAdNotifier notifier) {
        TapjoyLog.i("Display Ad", "Get display ad, currencyID: " + currencyID);
        displayAdNotifier = notifier;
        displayAdURLParams = TapjoyConnectCore.getURLParams();
        displayAdURLParams = String.valueOf(displayAdURLParams) + "&publisher_user_id=" + TapjoyConnectCore.getUserID();
        displayAdURLParams = String.valueOf(displayAdURLParams) + "&size=" + displayAdSize;
        if (currencyID != null) {
            displayAdURLParams = String.valueOf(displayAdURLParams) + "&currency_id=" + currencyID;
        }
        new Thread(new Runnable() { // from class: com.tapjoy.TapjoyDisplayAd.1
            @Override // java.lang.Runnable
            public void run() {
                String response = TapjoyDisplayAd.tapjoyURLConnection.connectToURL("https://ws.tapjoyads.com/display_ad?", TapjoyDisplayAd.displayAdURLParams);
                if (response == null || response.length() == 0) {
                    TapjoyDisplayAd.displayAdNotifier.getDisplayAdResponseFailed("Network error.");
                    return;
                }
                boolean returnValue = TapjoyDisplayAd.this.buildResponse(response);
                if (!returnValue) {
                    TapjoyDisplayAd.displayAdNotifier.getDisplayAdResponseFailed("No ad to display.");
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean buildResponse(String response) {
        RefreshTimer refreshTimer = null;
        boolean retValue = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            InputStream is = new ByteArrayInputStream(response.getBytes(OAuth.ENCODING));
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            adClickURL = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("ClickURL"));
            String image_data = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Image"));
            TapjoyLog.i("Display Ad", "decoding...");
            decodeBase64(image_data.getBytes(), 0, image_data.getBytes().length);
            TapjoyLog.i("Display Ad", "pos: " + this.pos);
            TapjoyLog.i("Display Ad", "buffer_size: " + this.buffer.length);
            this.bitmapImage = BitmapFactory.decodeByteArray(this.buffer, 0, this.pos);
            TapjoyLog.i("Display Ad", "image: " + this.bitmapImage.getWidth() + "x" + this.bitmapImage.getHeight());
            this.adView = new View(this.context);
            ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(this.bitmapImage.getWidth(), this.bitmapImage.getHeight());
            this.adView.setLayoutParams(layout);
            this.adView.setBackgroundDrawable(new BitmapDrawable(this.bitmapImage));
            this.adView.setOnClickListener(new View.OnClickListener() { // from class: com.tapjoy.TapjoyDisplayAd.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    TapjoyLog.i("Display Ad", "Opening URL in new browser = [" + TapjoyDisplayAd.adClickURL + "]");
                    Intent intent = new Intent(TapjoyDisplayAd.this.context, (Class<?>) TJCOffersWebView.class);
                    intent.putExtra(TapjoyConstants.EXTRA_DISPLAY_AD_URL, TapjoyDisplayAd.adClickURL);
                    intent.setFlags(268435456);
                    TapjoyDisplayAd.this.context.startActivity(intent);
                }
            });
            TapjoyLog.i("Display Ad", "notify displayAdNotifier");
            displayAdNotifier.getDisplayAdResponse(this.adView);
            retValue = true;
        } catch (Exception e) {
            TapjoyLog.e("Display Ad", "Error parsing XML: " + e.toString());
        }
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        if (this.autoRefresh && this.timer == null) {
            TapjoyLog.i("Display Ad", "will refresh banner ad in 15s...");
            this.timer = new Timer();
            this.timer.schedule(new RefreshTimer(this, refreshTimer), 15000L);
        }
        TapjoyLog.i("Display Ad", "return: " + retValue);
        return retValue;
    }

    private class RefreshTimer extends TimerTask {
        private RefreshTimer() {
        }

        /* synthetic */ RefreshTimer(TapjoyDisplayAd tapjoyDisplayAd, RefreshTimer refreshTimer) {
            this();
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            TapjoyLog.i("Display Ad", "refreshing banner ad...");
            TapjoyDisplayAd.this.getDisplayAd(TapjoyDisplayAd.displayAdNotifier);
            TapjoyDisplayAd.this.timer.cancel();
            TapjoyDisplayAd.this.timer = null;
        }
    }

    void decodeBase64(byte[] in, int inPos, int inAvail) {
        int result;
        this.buffer = new byte[in.length];
        this.pos = 0;
        this.eof = false;
        this.modulus = 0;
        if (inAvail < 0) {
            this.eof = true;
        }
        int i = 0;
        int inPos2 = inPos;
        while (i < inAvail) {
            int inPos3 = inPos2 + 1;
            byte b = in[inPos2];
            if (b == 61) {
                this.eof = true;
                break;
            }
            if (b >= 0 && b < DECODE_TABLE.length && (result = DECODE_TABLE[b]) >= 0) {
                int i2 = this.modulus + 1;
                this.modulus = i2;
                this.modulus = i2 % 4;
                this.x = (this.x << 6) + result;
                if (this.modulus == 0) {
                    byte[] bArr = this.buffer;
                    int i3 = this.pos;
                    this.pos = i3 + 1;
                    bArr[i3] = (byte) ((this.x >> 16) & 255);
                    byte[] bArr2 = this.buffer;
                    int i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr2[i4] = (byte) ((this.x >> 8) & 255);
                    byte[] bArr3 = this.buffer;
                    int i5 = this.pos;
                    this.pos = i5 + 1;
                    bArr3[i5] = (byte) (this.x & 255);
                }
            }
            i++;
            inPos2 = inPos3;
        }
        if (this.eof && this.modulus != 0) {
            this.x <<= 6;
            switch (this.modulus) {
                case 2:
                    this.x <<= 6;
                    byte[] bArr4 = this.buffer;
                    int i6 = this.pos;
                    this.pos = i6 + 1;
                    bArr4[i6] = (byte) ((this.x >> 16) & 255);
                    break;
                case 3:
                    byte[] bArr5 = this.buffer;
                    int i7 = this.pos;
                    this.pos = i7 + 1;
                    bArr5[i7] = (byte) ((this.x >> 16) & 255);
                    byte[] bArr6 = this.buffer;
                    int i8 = this.pos;
                    this.pos = i8 + 1;
                    bArr6[i8] = (byte) ((this.x >> 8) & 255);
                    break;
            }
        }
    }
}
