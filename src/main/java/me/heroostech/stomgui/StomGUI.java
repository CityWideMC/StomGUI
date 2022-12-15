package me.heroostech.stomgui;

import lombok.Getter;
import me.heroostech.stomgui.gui.java.GUIListener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import xyz.citywide.citystom.Extension;

public final class StomGUI {

    @Getter private static final StomGUI instance = new StomGUI();

    public void registerExtension(Extension extension) {
        EventNode<Event> node = EventNode.type("stomgui", EventFilter.ALL);
        GUIListener listener = new GUIListener(extension);
        node.addChild(listener.events());
        extension.getEventNode().addChild(node);
    }
}