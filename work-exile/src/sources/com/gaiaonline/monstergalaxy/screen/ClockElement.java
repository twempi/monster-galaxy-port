package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Fonts;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ClockElement extends ScreenElement {
    private static ClockElement instance;
    private ShadowTextElement testElement = null;
    private Calendar calendar = Calendar.getInstance();
    private Vector2 position = new Vector2(7.0f, 7.0f);
    private Vector2 position00 = new Vector2(0.0f, 0.0f);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    public static synchronized ClockElement getInstance() {
        if (instance == null) {
            instance = new ClockElement();
        }
        return instance;
    }

    private ClockElement() {
    }

    protected void present(SpriteBatch batcher) {
        present(this.position00, batcher);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        if (this.testElement == null && Fonts.regular != null) {
            this.testElement = new ShadowTextElement("--:--:--", 0.6f, Color.WHITE, Color.BLACK, true);
        }
        if (this.testElement != null) {
            if (Clock.isInitialized()) {
                this.calendar.setTimeInMillis(Clock.getCurrentTimeSec() * 1000);
                this.testElement.setText(String.valueOf(Clock.isTimeTrusted() ? "Trusted " : "Untrusted ") + this.dateFormat.format(Long.valueOf(this.calendar.getTimeInMillis())));
            }
            this.testElement.present(this.position, batcher);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void dispose() {
    }
}
