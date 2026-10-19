package com.gaiaonline.monstergalaxy.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.gaiaonline.monstergalaxy.R;
import com.gaiaonline.monstergalaxy.tweak.AutoPlayerScreen;
import com.gaiaonline.monstergalaxy.tweak.Tweak;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class TweaksScreen {
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    public static final int DIALOG_AUTPLAY_ISLAND = 1;
    public static final int DIALOG_SNAPSHOTS = 2;
    private static boolean showing;
    private static ScrollView tweaksView;

    public static void show(final MonsterGalaxyAndroid activity) {
        showing = true;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService("layout_inflater");
        tweaksView = (ScrollView) inflater.inflate(R.layout.tweaks, (ViewGroup) null);
        Button saveButton = (Button) tweaksView.findViewById(R.id.tweaks_save);
        saveButton.setOnClickListener(new View.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TweaksScreen.save();
            }
        });
        Button cancelButton = (Button) tweaksView.findViewById(R.id.tweaks_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TweaksScreen.hide();
            }
        });
        Button autoplayButton = (Button) tweaksView.findViewById(R.id.tweaks_autoplay);
        autoplayButton.setOnClickListener(new View.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                activity.showDialog(1);
            }
        });
        Button saveSnapButton = (Button) tweaksView.findViewById(R.id.tweaks_savesnap);
        saveSnapButton.setOnClickListener(new View.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                activity.removeDialog(2);
                TweaksScreen.saveSnapshot();
            }
        });
        Button loadSnapButton = (Button) tweaksView.findViewById(R.id.tweaks_loadsnap);
        loadSnapButton.setOnClickListener(new View.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                activity.showDialog(2);
            }
        });
        ToggleButton tweaksEnabled = (ToggleButton) tweaksView.findViewById(R.id.tweaks_enable);
        tweaksEnabled.setOnClickListener(new View.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TweaksScreen.optionsChanged();
            }
        });
        ToggleButton customRivalStartingEnergyEnabled = (ToggleButton) tweaksView.findViewById(R.id.tweaks_customrivalstartingenergy);
        customRivalStartingEnergyEnabled.setOnClickListener(new View.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TweaksScreen.optionsChanged();
            }
        });
        ((FrameLayout) activity.getView().getParent()).addView(tweaksView);
        populateFields();
        optionsChanged();
    }

    public static void autoPlay(int islandIndex) {
        MonsterGalaxy.showScreen(new AutoPlayerScreen(islandIndex));
        hide();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void optionsChanged() {
        int visibility = getBoolFieldValue(R.id.tweaks_enable) ? 0 : 8;
        ((LinearLayout) tweaksView.findViewById(R.id.tweaks_otheroptions)).setVisibility(visibility);
        int visibility2 = getBoolFieldValue(R.id.tweaks_customrivalstartingenergy) ? 0 : 4;
        ((EditText) tweaksView.findViewById(R.id.tweaks_rivalstartingenergy)).setVisibility(visibility2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void save() {
        Tweak.setEnabled(getBoolFieldValue(R.id.tweaks_enable));
        if (Tweak.isEnabled()) {
            Tweak.setBlueCoffees(getIntFieldValue(R.id.tweaks_bluecoffees));
            Tweak.setMogaCash(getIntFieldValue(R.id.tweaks_mogacash));
            Tweak.setStarSeeds(getIntFieldValue(R.id.tweaks_starseeds));
        }
        Tweak.setCustomCpuStartingEnergyEnabled(getBoolFieldValue(R.id.tweaks_customrivalstartingenergy));
        Tweak.setCustomCpuStartingEnergy(getIntFieldValue(R.id.tweaks_rivalstartingenergy));
        hide();
    }

    private static void populateFields() {
        populateField(R.id.tweaks_enable, Tweak.isEnabled());
        populateField(R.id.tweaks_bluecoffees, Tweak.getBlueCoffees());
        populateField(R.id.tweaks_mogacash, Tweak.getMogaCash());
        populateField(R.id.tweaks_starseeds, Tweak.getStarSeeds());
        populateField(R.id.tweaks_rivalstartingenergy, Tweak.getCustomCpuStartingEnergy());
        populateField(R.id.tweaks_customrivalstartingenergy, Tweak.isCustomCpuStartingEnergyEnabled());
    }

    private static void populateField(int id, int value) {
        ((EditText) tweaksView.findViewById(id)).setText(String.valueOf(value));
    }

    private static void populateField(int id, boolean value) {
        ((ToggleButton) tweaksView.findViewById(id)).setChecked(value);
    }

    private static boolean getBoolFieldValue(int id) {
        return ((ToggleButton) tweaksView.findViewById(id)).isChecked();
    }

    private static int getIntFieldValue(int id) {
        return Integer.parseInt(((EditText) tweaksView.findViewById(id)).getText().toString());
    }

    public static String[] getSnapshots() {
        ArrayList<String> snapshotNames = new ArrayList<>();
        File[] files = Game.getPlatformHelper().getExportDirectory().listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (!file.isDirectory()) {
                snapshotNames.add(file.getName());
            }
        }
        return (String[]) snapshotNames.toArray(new String[0]);
    }

    public static void snapshotPicked(final String name) {
        Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.8
            @Override // java.lang.Runnable
            public void run() {
                File file = new File(Game.getPlatformHelper().getExportDirectory(), name);
                if (!file.canRead()) {
                    TweaksScreen.showMessage("Can't read " + file.getPath());
                }
                MonsterGalaxy.loadAndSetTrainer(file);
                MonsterGalaxy.showScreen(ScreenCode.ISLAND);
                TweaksScreen.hide();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showMessage(String message) {
        Toast.makeText(MonsterGalaxyApp.getInstance(), message, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void saveSnapshot() {
        File file = new File(Game.getPlatformHelper().getExportDirectory(), "snap-" + DATE_FORMAT.format(new Date()));
        if (Game.save(file)) {
            showMessage("Saved as " + file.getName());
            MonsterGalaxy.showScreen(ScreenCode.ISLAND);
            hide();
            return;
        }
        showMessage("Can't save " + file.getName());
    }

    public static void hide() {
        showing = false;
        if (tweaksView != null) {
            Runnable action = new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.TweaksScreen.9
                @Override // java.lang.Runnable
                public void run() {
                    ((ViewGroup) TweaksScreen.tweaksView.getParent()).removeView(TweaksScreen.tweaksView);
                    TweaksScreen.tweaksView = null;
                }
            };
            tweaksView.post(action);
        }
    }

    public static boolean isShowing() {
        return showing;
    }
}
