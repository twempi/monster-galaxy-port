package com.gaiaonline.monstergalaxy.model.quest;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class QuestDialogLine implements Serializable {
    private static final long serialVersionUID = -8040036539534426778L;
    private String speaker;
    private String text;

    public QuestDialogLine(String speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    public String getSpeaker() {
        return this.speaker;
    }

    public String getText() {
        return this.text;
    }
}
