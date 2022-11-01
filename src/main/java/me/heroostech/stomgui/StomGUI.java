package me.heroostech.stomgui;

import lombok.Getter;
import me.heroostech.cityengine.extension.Extension;
import me.heroostech.stomgui.gui.GUIListener;

@SuppressWarnings("ClassCanBeRecord")
public class StomGUI {
    @Getter private final Extension extension;

    public StomGUI(Extension extension) {
        this.extension = extension;
        extension.registerEvent(new GUIListener(this));
    }
}