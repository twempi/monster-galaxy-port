package com.gaiaonline.monstergalaxy.platform;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.gaiaonline.monstergalaxy.R;
import com.gaiaonline.monstergalaxy.app.Config;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxyApp;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.shop.DealListener;
import com.gaiaonline.monstergalaxy.reward.TapjoyNotifierImpl;
import com.gaiaonline.monstergalaxy.service.AdsService;
import com.gaiaonline.monstergalaxy.service.AndroidAdsService;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import com.tapjoy.TapjoyConnect;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidPlatformHelperImpl extends PlatformHelper implements AndroidPlatformHelper {
    private static MogaProgressDialog resumeProgressDialog;
    private StorageHelper dbHelper;
    private DealListener dealListener;
    private File exportDirectory;
    private String externalStoragePath;
    private float fontScaleFactor;
    private MonsterGalaxyAndroid mgActivity;
    private ScrollView scrollview;
    private TextView textView;
    private Typeface typeface;
    private Typeface typefaceRegular;
    private int versionCode;
    private String versionName;
    CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.1
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.settings_dailyprizesalert) {
                SettingsManager.setDailyPrizesAlerts(isChecked);
                return;
            }
            if (buttonView.getId() == R.id.settings_mogarestedalert) {
                SettingsManager.setMogaRestedAlerts(isChecked);
            } else if (buttonView.getId() == R.id.settings_tammeralerts) {
                SettingsManager.setTammerAlerts(isChecked);
            } else if (buttonView.getId() == R.id.settings_islandalerts) {
                SettingsManager.setIslandAlerts(isChecked);
            }
        }
    };
    SeekBar.OnSeekBarChangeListener seekbarListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.2
        int initialProgress = 0;

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            float value = seekBar.getProgress() / 100.0f;
            if (seekBar.getId() == R.id.settings_effectsvolume) {
                SoundManager.setSoundsVolume(value);
                SettingsManager.setSoundVolume(value);
                SoundManager.playSound(SoundManager.SOUND_MENU_SLIDE);
            } else if (seekBar.getId() == R.id.settings_musicvolume) {
                SoundManager.setMusicVolume(value);
                SettingsManager.setMusicVolume(value);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            this.initialProgress = seekBar.getProgress();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int deltaProgress = this.initialProgress - progress;
            if (Math.abs(deltaProgress) > 5) {
                float value = seekBar.getProgress() / 100.0f;
                if (seekBar.getId() == R.id.settings_effectsvolume) {
                    SoundManager.setSoundsVolume(value);
                    SoundManager.playSound(SoundManager.SOUND_MENU_SLIDE);
                } else if (seekBar.getId() == R.id.settings_musicvolume) {
                    SoundManager.setMusicVolume(value);
                }
            }
        }
    };
    private Map<String, EditText> editTexts = new ConcurrentHashMap();

    public AndroidPlatformHelperImpl(MonsterGalaxyAndroid mgActivity) {
        this.fontScaleFactor = 1.0f;
        this.mgActivity = mgActivity;
        this.dbHelper = ((MonsterGalaxyApp) mgActivity.getApplicationContext()).getDbHelper();
        this.typeface = Typeface.createFromAsset(mgActivity.getAssets(), "fonts/VAGRunds.ttf");
        this.typefaceRegular = Typeface.createFromAsset(mgActivity.getAssets(), "fonts/VAGRundsRegular.ttf");
        try {
            PackageInfo pkgInfo = mgActivity.getPackageManager().getPackageInfo(mgActivity.getPackageName(), 0);
            this.versionCode = pkgInfo.versionCode;
            this.versionName = pkgInfo.versionName;
            DisplayMetrics metrics = new DisplayMetrics();
            mgActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int density = metrics.densityDpi;
            if ((density == 160 || density == 120) && ((mgActivity.getResources().getConfiguration().screenLayout & 15) == 3 || (mgActivity.getResources().getConfiguration().screenLayout & 15) == 4)) {
                this.fontScaleFactor = 1.7f;
            }
            this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Can't read app version", e);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public StorageHelper getStorageHelper() {
        return this.dbHelper;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public String getExternalStoragePath() {
        return this.externalStoragePath;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void showNativeProgressDialog() {
        if (!AndroidAdsService.getInstance().isShowingInterstitial() && resumeProgressDialog == null) {
            this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    AndroidPlatformHelperImpl.resumeProgressDialog = MogaProgressDialog.show(AndroidPlatformHelperImpl.this.mgActivity, " Please wait ", "Wait please", true, false);
                }
            });
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void hideNativeProgressDialog() {
        if (resumeProgressDialog != null) {
            this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    if (AndroidPlatformHelperImpl.resumeProgressDialog != null) {
                        try {
                            AndroidPlatformHelperImpl.resumeProgressDialog.dismiss();
                        } catch (IllegalArgumentException e) {
                            Log.w(Game.TAG, "AndroidPlatformHelperImpl: Failed to dismiss dialog", e);
                        }
                        AndroidPlatformHelperImpl.resumeProgressDialog = null;
                    }
                }
            });
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void switchKeyboard(final boolean visible) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.5
            @Override // java.lang.Runnable
            public void run() {
                AndroidPlatformHelperImpl.this.switchKeyboardSync(visible);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchKeyboardSync(boolean visible) {
        InputMethodManager imm = (InputMethodManager) this.mgActivity.getSystemService("input_method");
        if (visible) {
            imm.showSoftInput(this.mgActivity.getView(), 0);
        } else {
            imm.hideSoftInputFromWindow(this.mgActivity.getView().getApplicationWindowToken(), 0);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void requestFocus(final String textFieldName) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.6
            @Override // java.lang.Runnable
            public void run() {
                AndroidPlatformHelperImpl.this.requestFocusSync(textFieldName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestFocusSync(String textFieldName) {
        EditText editText = this.editTexts.get(textFieldName);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void addTextField(final String name, final int x, final int y, final int width, final int height, final boolean multiline, final boolean fullScreenKeyboard, final Integer maxChars, final String disallowedChars, final boolean capWords, final boolean requestFocus) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.7
            @Override // java.lang.Runnable
            public void run() {
                EditText editText = new EditText(AndroidPlatformHelperImpl.this.mgActivity);
                if (!multiline) {
                    editText.setSingleLine();
                }
                editText.setTypeface(AndroidPlatformHelperImpl.this.typeface);
                editText.setTextSize(20.0f * AndroidPlatformHelperImpl.this.fontScaleFactor);
                editText.setTextColor(-1);
                if (capWords) {
                    editText.setInputType(editText.getInputType() | Opcodes.ACC_ANNOTATION);
                }
                List<InputFilter> filters = new ArrayList<>();
                if (maxChars != null) {
                    filters.add(new InputFilter.LengthFilter(maxChars.intValue()));
                }
                if (disallowedChars != null) {
                    final String str = disallowedChars;
                    filters.add(new InputFilter() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.7.1
                        @Override // android.text.InputFilter
                        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                            String newSubstring = source.subSequence(start, end).toString();
                            String modifiedSubstring = new String(newSubstring);
                            for (int i = 0; i < str.length(); i++) {
                                String disallowedChar = str.substring(i, i + 1);
                                modifiedSubstring = modifiedSubstring.replace(disallowedChar, "");
                            }
                            if (newSubstring.equals(modifiedSubstring)) {
                                return null;
                            }
                            return modifiedSubstring;
                        }
                    });
                }
                editText.setFilters((InputFilter[]) filters.toArray(new InputFilter[filters.size()]));
                editText.setWidth(width);
                editText.setHeight(height);
                editText.setBackgroundDrawable(AndroidPlatformHelperImpl.this.mgActivity.getResources().getDrawable(R.drawable.edittext));
                if (!fullScreenKeyboard) {
                    editText.setImeOptions(268435456);
                }
                FrameLayout wrapLayout = new FrameLayout(AndroidPlatformHelperImpl.this.mgActivity);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
                params.gravity = 51;
                wrapLayout.setLayoutParams(params);
                wrapLayout.setBackgroundColor(0);
                wrapLayout.setPadding(x, y, 0, 0);
                wrapLayout.addView(editText);
                ((FrameLayout) AndroidPlatformHelperImpl.this.mgActivity.getView().getParent()).addView(wrapLayout);
                AndroidPlatformHelperImpl.this.editTexts.put(name, editText);
                if (requestFocus) {
                    AndroidPlatformHelperImpl.this.switchKeyboardSync(true);
                    AndroidPlatformHelperImpl.this.requestFocusSync(name);
                }
            }
        });
        if (requestFocus) {
            this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.8
                @Override // java.lang.Runnable
                public void run() {
                    AndroidPlatformHelperImpl.this.switchKeyboardSync(true);
                }
            });
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void removeTextField(final String name) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.9
            @Override // java.lang.Runnable
            public void run() {
                View view = (View) AndroidPlatformHelperImpl.this.editTexts.get(name);
                ((ViewGroup) view.getParent()).removeView(view);
                AndroidPlatformHelperImpl.this.editTexts.remove(name);
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public String getTextfieldValue(String name) {
        EditText editText = this.editTexts.get(name);
        return editText == null ? "" : editText.getText().toString();
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void setTextfieldValue(final String name, final String text) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.10
            @Override // java.lang.Runnable
            public void run() {
                EditText editText = (EditText) AndroidPlatformHelperImpl.this.editTexts.get(name);
                editText.setText("");
                editText.append(text);
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public boolean isAndroid() {
        return true;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void removeTextView() {
        if (this.textView != null) {
            Runnable action = new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.11
                @Override // java.lang.Runnable
                public void run() {
                    ((ViewGroup) AndroidPlatformHelperImpl.this.mgActivity.getView().getParent()).removeView(AndroidPlatformHelperImpl.this.textView);
                    AndroidPlatformHelperImpl.this.textView = null;
                }
            };
            this.mgActivity.runOnUiThread(action);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void addTextView(final int width, final int left, final int top, final String text, final Color color) {
        if (this.textView == null) {
            this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.12
                @Override // java.lang.Runnable
                public void run() {
                    int r = ((int) color.r) * 255;
                    int g = ((int) color.g) * 255;
                    int b = ((int) color.b) * 255;
                    DisplayMetrics metrics = new DisplayMetrics();
                    AndroidPlatformHelperImpl.this.mgActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    AndroidPlatformHelperImpl.this.textView = new TextView(AndroidPlatformHelperImpl.this.mgActivity);
                    AndroidPlatformHelperImpl.this.textView.setTypeface(AndroidPlatformHelperImpl.this.typefaceRegular);
                    AndroidPlatformHelperImpl.this.textView.setTextSize(18.0f * AndroidPlatformHelperImpl.this.fontScaleFactor);
                    AndroidPlatformHelperImpl.this.textView.setTextColor(android.graphics.Color.rgb(r, g, b));
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
                    int topPix = (int) ((top * Gdx.graphics.getHeight()) / 320.0f);
                    int leftPix = (int) ((left * Gdx.graphics.getWidth()) / 480.0f);
                    params.leftMargin = leftPix;
                    params.topMargin = topPix;
                    params.gravity = 51;
                    params.width = (int) ((width * Gdx.graphics.getWidth()) / 480.0f);
                    if (text != null) {
                        AndroidPlatformHelperImpl.this.textView.setText(Html.fromHtml(text));
                    }
                    AndroidPlatformHelperImpl.this.textView.setLayoutParams(params);
                    AndroidPlatformHelperImpl.this.textView.setBackgroundColor(AndroidPlatformHelperImpl.this.mgActivity.getResources().getColor(android.R.color.transparent));
                    ((FrameLayout) AndroidPlatformHelperImpl.this.mgActivity.getView().getParent()).addView(AndroidPlatformHelperImpl.this.textView);
                }
            });
        } else {
            this.textView.setVisibility(0);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void setTextViewText(final String text) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.13
            @Override // java.lang.Runnable
            public void run() {
                if (AndroidPlatformHelperImpl.this.textView != null) {
                    CharSequence c = Html.fromHtml(text);
                    if (c.length() <= 238) {
                        AndroidPlatformHelperImpl.this.textView.setTextSize(AndroidPlatformHelperImpl.this.fontScaleFactor * 18.0f);
                    } else {
                        float newSize = (c.length() / 34) - 7;
                        AndroidPlatformHelperImpl.this.textView.setTextSize((18.0f - newSize) * AndroidPlatformHelperImpl.this.fontScaleFactor);
                    }
                    AndroidPlatformHelperImpl.this.textView.setText(Html.fromHtml(text));
                }
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void addOptionsView(final String UdId) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.14
            @Override // java.lang.Runnable
            public void run() {
                LayoutInflater inflater = (LayoutInflater) AndroidPlatformHelperImpl.this.mgActivity.getSystemService("layout_inflater");
                AndroidPlatformHelperImpl.this.scrollview = (ScrollView) inflater.inflate(R.layout.settings, (ViewGroup) null);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
                DisplayMetrics metrics = new DisplayMetrics();
                AndroidPlatformHelperImpl.this.mgActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int pix = (int) ((105.0f * metrics.ydpi) / 160.0f);
                int left = (int) ((10.0f * metrics.xdpi) / 160.0f);
                params.leftMargin = left;
                params.topMargin = pix;
                params.gravity = 51;
                AndroidPlatformHelperImpl.this.scrollview.setLayoutParams(params);
                AndroidPlatformHelperImpl.this.scrollview.setBackgroundColor(AndroidPlatformHelperImpl.this.mgActivity.getResources().getColor(android.R.color.transparent));
                SeekBar effectsVolume = (SeekBar) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_effectsvolume);
                effectsVolume.setOnSeekBarChangeListener(AndroidPlatformHelperImpl.this.seekbarListener);
                Float mVol = Float.valueOf(SettingsManager.getSoundVolume() * 100.0f);
                effectsVolume.setProgress(mVol.intValue());
                SeekBar musicVolume = (SeekBar) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_musicvolume);
                musicVolume.setOnSeekBarChangeListener(AndroidPlatformHelperImpl.this.seekbarListener);
                Float mVol2 = Float.valueOf(SettingsManager.getMusicVolume() * 100.0f);
                musicVolume.setProgress(mVol2.intValue());
                ToggleButton tammerAlerts = (ToggleButton) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_tammeralerts);
                tammerAlerts.setOnCheckedChangeListener(AndroidPlatformHelperImpl.this.toggleListener);
                tammerAlerts.setChecked(SettingsManager.isTammerAlerts());
                ToggleButton islandAlerts = (ToggleButton) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_islandalerts);
                islandAlerts.setOnCheckedChangeListener(AndroidPlatformHelperImpl.this.toggleListener);
                islandAlerts.setChecked(SettingsManager.isIslandAlerts());
                ToggleButton daulyPrizesAlert = (ToggleButton) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_dailyprizesalert);
                daulyPrizesAlert.setOnCheckedChangeListener(AndroidPlatformHelperImpl.this.toggleListener);
                daulyPrizesAlert.setChecked(SettingsManager.isDailyPrizesAlerts());
                ToggleButton mogaRestedAlert = (ToggleButton) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_mogarestedalert);
                mogaRestedAlert.setOnCheckedChangeListener(AndroidPlatformHelperImpl.this.toggleListener);
                mogaRestedAlert.setChecked(SettingsManager.isMogaRestedAlerts());
                TextView udid = (TextView) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_udid);
                udid.setText(UdId);
                TextView versionLabel = (TextView) AndroidPlatformHelperImpl.this.scrollview.findViewById(R.id.settings_version);
                versionLabel.setText("v " + AndroidPlatformHelperImpl.this.versionName + " (" + AndroidPlatformHelperImpl.this.versionCode + ")\nBuild # " + Config.SVN_REVISION);
                ((FrameLayout) AndroidPlatformHelperImpl.this.mgActivity.getView().getParent()).addView(AndroidPlatformHelperImpl.this.scrollview);
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void removeOptionsView() {
        if (this.scrollview != null) {
            Runnable action = new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.15
                @Override // java.lang.Runnable
                public void run() {
                    ((ViewGroup) AndroidPlatformHelperImpl.this.mgActivity.getView().getParent()).removeView(AndroidPlatformHelperImpl.this.scrollview);
                    AndroidPlatformHelperImpl.this.scrollview = null;
                }
            };
            this.scrollview.post(action);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void buyProduct(String productId) {
        MonsterGalaxyApp.BILLING_SERVICE.requestPurchase(productId);
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public File getExportDirectory() {
        if (this.exportDirectory == null) {
            this.exportDirectory = new File(Environment.getExternalStorageDirectory(), "mgsnapshots");
        }
        if (!this.exportDirectory.exists()) {
            this.exportDirectory.mkdir();
        }
        return this.exportDirectory;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public void quit() {
        this.mgActivity.nativeOnBackPressed();
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public String getVersionName() {
        return this.versionName;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public int getVersionCode() {
        return this.versionCode;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public String getPlatformVersion() {
        return Build.VERSION.RELEASE;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void openMarketProductPage() {
        String url;
        try {
            if (getSourceStore() == PlatformHelper.Store.AMAZON) {
                if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                    url = "amzn://apps/android?p=com.gaiaonline.monstergalaxy";
                } else {
                    url = "amzn://apps/android?p=com.gaiaonline.mge";
                }
            } else if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                url = "market://details?id=com.gaiaonline.monstergalaxy";
            } else {
                url = "market://details?id=com.gaiaonline.mge";
            }
            Intent marketIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            marketIntent.addFlags(1074266112);
            this.mgActivity.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
        }
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void setDealListener(DealListener listener) {
        this.dealListener = listener;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public DealListener getDealListener() {
        return this.dealListener;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void showAdColonyVideo() {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.16
            @Override // java.lang.Runnable
            public void run() {
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void showW3iOfferWall() {
    }

    private void getW3iReward() {
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void getDealRewards() {
        getTapPoints();
        getW3iReward();
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void showTapJoyOffers() {
        TapjoyConnect.getTapjoyConnectInstance().showOffers();
    }

    private void getTapPoints() {
        TapjoyConnect.getTapjoyConnectInstance().getTapPoints(new TapjoyNotifierImpl(this.dealListener));
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void composeSms(final String message) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.17
            @Override // java.lang.Runnable
            public void run() {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setType("vnd.android-dir/mms-sms");
                intent.setFlags(872415232);
                intent.putExtra("sms_body", message);
                try {
                    AndroidPlatformHelperImpl.this.mgActivity.startActivity(Intent.createChooser(intent, "Send SMS using..."));
                } catch (ActivityNotFoundException e) {
                    AndroidPlatformHelperImpl.this.showMessage("Couldn't find an app to send SMS");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public void composeEmail(final String subject, final String message) {
        this.mgActivity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl.18
            @Override // java.lang.Runnable
            public void run() {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("message/rfc822");
                intent.setFlags(872415232);
                intent.putExtra("android.intent.extra.EMAIL", new String[0]);
                intent.putExtra("android.intent.extra.SUBJECT", subject);
                intent.putExtra("android.intent.extra.TEXT", message);
                PackageManager packageManager = AndroidPlatformHelperImpl.this.mgActivity.getApplicationContext().getPackageManager();
                List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 65600);
                if (list.size() > 0) {
                    try {
                        AndroidPlatformHelperImpl.this.mgActivity.startActivity(Intent.createChooser(intent, "Send email using..."));
                        return;
                    } catch (ActivityNotFoundException e) {
                        AndroidPlatformHelperImpl.this.showMessage("Couldn't find an app to send email");
                        e.printStackTrace();
                        return;
                    }
                }
                AndroidPlatformHelperImpl.this.showMessage("Couldn't find an application to send email");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMessage(String message) {
        Toast.makeText(this.mgActivity, message, 1).show();
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public AdsService getAdsService() {
        return AndroidAdsService.getInstance();
    }

    @Override // com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper
    public boolean isSMSCapable() {
        TelephonyManager m = (TelephonyManager) this.mgActivity.getApplication().getSystemService("phone");
        return m.getSimState() == 5;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public PlatformHelper.MGSkin getSkin() {
        boolean isSk2 = this.mgActivity.getApplication().getApplicationInfo().packageName.equals("com.gaiaonline.mge");
        return isSk2 ? PlatformHelper.MGSkin.SK2 : PlatformHelper.MGSkin.SK1;
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public long getBootElapsedTimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public boolean isPurchaseTransactionPending() {
        return MonsterGalaxyApp.BILLING_SERVICE.isPurchaseTransactionPending();
    }

    @Override // com.gaiaonline.monstergalaxy.platform.PlatformHelper
    public void share(String shareText) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "Monster Galaxy");
            intent.putExtra("android.intent.extra.TEXT", shareText);
            this.mgActivity.startActivity(Intent.createChooser(intent, "Share Monster Galaxy"));
        } catch (Exception e) {
            Log.e(Game.TAG, e.getMessage());
        }
    }
}
