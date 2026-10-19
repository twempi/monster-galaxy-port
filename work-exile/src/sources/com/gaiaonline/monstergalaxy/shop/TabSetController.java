package com.gaiaonline.monstergalaxy.shop;

import com.gaiaonline.monstergalaxy.screen.ButtonElement;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import com.gaiaonline.monstergalaxy.screen.ScreenListener;
import com.gaiaonline.monstergalaxy.screen.UIEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TabSetController {
    private Listener listener;
    private Map<String, Tab> tabs = new HashMap();

    public interface Listener {
        void tabSelected(Tab tab);
    }

    public static class Tab {
        public ButtonElement button;
        public ScreenElement content;
        public String id;
    }

    public void addTab(String id, ButtonElement button, ScreenElement content) {
        final Tab tab = new Tab();
        tab.id = id;
        tab.button = button;
        tab.content = content;
        this.tabs.put(id, tab);
        button.setScreenListener(new ScreenListener() { // from class: com.gaiaonline.monstergalaxy.shop.TabSetController.1
            @Override // com.gaiaonline.monstergalaxy.screen.ScreenListener
            public void onUIEvent(UIEvent event) {
                TabSetController.this.tabSelected(tab);
            }
        });
    }

    public void setSelected(String id) {
        tabSelected(this.tabs.get(id));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tabSelected(Tab selectedTab) {
        Iterator<Tab> it = this.tabs.values().iterator();
        while (it.hasNext()) {
            Tab tab = it.next();
            if (tab.content != null) {
                tab.content.setVisible(tab == selectedTab);
            }
            tab.button.setEnabled(tab != selectedTab);
        }
        if (this.listener != null) {
            this.listener.tabSelected(selectedTab);
        }
    }
}
