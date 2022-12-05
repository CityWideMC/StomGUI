package me.heroostech.stomgui;

import lombok.Getter;
import me.heroostech.citystom.Extension;
import me.heroostech.stomgui.gui.java.GUIListener;

public class StomGUI extends Extension {

    @Getter private static StomGUI instance;

    @Override
    public void initialize() {
        instance = this;
    }

    @Override
    public void terminate() {

    }

    public void registerExtension(Extension extension) {
        extension.registerEvent(new GUIListener(extension));
    }
}