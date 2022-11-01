package me.heroostech.stomgui.gui;

import lombok.RequiredArgsConstructor;
import me.heroostech.cityengine.extension.listener.Listener;
import me.heroostech.stomgui.StomGUI;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;

@RequiredArgsConstructor
public class GUIListener implements Listener<InventoryEvent> {

    private final StomGUI stomGUI;

    @Override
    public EventNode<InventoryEvent> events() {
        var node = EventNode.type("stomgui", EventFilter.INVENTORY);

        node.addListener(InventoryPreClickEvent.class, event -> {
            var inventory = event.getInventory();

            if(!(inventory instanceof GUIImpl gui)) return;
            if(!gui.getStomGUI().equals(stomGUI)) return;

            var button = gui.getButtons().get(event.getSlot());

            if(button == null) return;

            if(gui.clickHandler() != null)
                gui.clickHandler().accept(event);

            if(button.clickHandler() != null)
                button.clickHandler().accept(event);
        });

        node.addListener(InventoryCloseEvent.class, event -> {
            var inventory = event.getInventory();

            if(!(inventory instanceof GUIImpl gui)) return;
            if(!gui.getStomGUI().equals(stomGUI)) return;

            if(gui.closeHandler() != null)
                gui.closeHandler().accept(event);
        });

        return node;
    }
}
