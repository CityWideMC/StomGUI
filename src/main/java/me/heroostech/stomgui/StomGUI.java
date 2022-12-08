package me.heroostech.stomgui;

import lombok.Getter;
import me.heroostech.citystom.Extension;
import me.heroostech.stomgui.gui.java.GUIListener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;

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
        EventNode<Event> node = EventNode.type("stomgui", EventFilter.ALL);
        extension.getEventNode().addChild(node);
    }
}