package me.heroostech.stomgui;

import me.heroostech.citystom.Extension;
import me.heroostech.stomgui.gui.GUIListener;

public record StomGUI(Extension extension) {
    public StomGUI(Extension extension) {
        this.extension = extension;
        extension.registerEvent(new GUIListener(this));
    }
}