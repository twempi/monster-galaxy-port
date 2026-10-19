package com.badlogic.gdx.scenes.scene2d.ui.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DesktopClipboard extends Clipboard implements ClipboardOwner {
    @Override // com.badlogic.gdx.scenes.scene2d.ui.utils.Clipboard
    public String getContents() {
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents((Object) null);
        boolean hasTransferableText = contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (!hasTransferableText) {
            return "";
        }
        try {
            String result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            return result;
        } catch (UnsupportedFlavorException e) {
            return "";
        } catch (IOException e2) {
            return "";
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.ui.utils.Clipboard
    public void setContents(String content) {
        StringSelection stringSelection = new StringSelection(content);
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    public void lostOwnership(java.awt.datatransfer.Clipboard arg0, Transferable arg1) {
    }
}
