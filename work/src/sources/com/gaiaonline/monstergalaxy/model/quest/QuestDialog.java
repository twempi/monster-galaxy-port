package com.gaiaonline.monstergalaxy.model.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class QuestDialog implements Serializable {
    private static Pattern pattern = Pattern.compile("<br\\s*/>", 2);
    private static final long serialVersionUID = -4599275100472553993L;
    public int currentLine;
    private List<QuestDialogLine> lines;

    public QuestDialog() {
        this.currentLine = -1;
        this.lines = new ArrayList();
    }

    public QuestDialog(String text) {
        this();
        parseDialogText(text);
    }

    public boolean hasNext() {
        return this.currentLine + 1 < this.lines.size();
    }

    public boolean hasPrevious() {
        return this.currentLine > 0;
    }

    public void moveLast() {
        this.currentLine = this.lines.size();
    }

    public void moveFirst() {
        this.currentLine = -1;
    }

    public QuestDialogLine nextLine() {
        this.currentLine++;
        if (this.currentLine >= this.lines.size()) {
            return null;
        }
        QuestDialogLine line = this.lines.get(this.currentLine);
        return line;
    }

    public QuestDialogLine previousLine() {
        this.currentLine--;
        if (this.currentLine >= 0) {
            QuestDialogLine line = this.lines.get(this.currentLine);
            return line;
        }
        this.currentLine = 0;
        return null;
    }

    public int getLength() {
        return this.lines.size();
    }

    public void addLine(QuestDialogLine line) {
        this.lines.add(line);
    }

    private void parseDialogText(String dialogText) {
        String speaker;
        String text;
        String[] result = pattern.split(dialogText);
        for (int i = 0; i < result.length; i++) {
            if (result[i].trim().length() > 0) {
                int index = result[i].indexOf("：");
                if (index == -1) {
                    index = result[i].indexOf(":");
                }
                if (index == -1) {
                    speaker = "...";
                    text = result[i].trim();
                } else {
                    speaker = result[i].substring(0, index);
                    text = result[i].substring(index + 1).trim();
                }
                addLine(new QuestDialogLine(speaker.trim(), text.trim()));
            }
        }
    }
}
