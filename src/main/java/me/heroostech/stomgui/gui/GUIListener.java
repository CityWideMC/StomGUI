package me.heroostech.stomgui.gui;

import lombok.RequiredArgsConstructor;
import me.heroostech.citystom.listener.Listener;
import me.heroostech.stomgui.StomGUI;
import me.heroostech.stomgui.button.Button;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;

@RequiredArgsConstructor
public class GUIListener implements Listener<InventoryEvent> {

    private final StomGUI stomGUI;

    @Override
    public EventNode<InventoryEvent> events() {
        EventNode<InventoryEvent> node = EventNode.type("stomgui", EventFilter.INVENTORY);

        node.addListener(InventoryPreClickEvent.class, event -> {
            Inventory inventory = event.getInventory();

            if(!(inventory instanceof GUIImpl gui)) return;
            if(!gui.getStomGUI().equals(stomGUI)) return;

            event.setCancelled(true);

            Button button = gui.getButtons().get(event.getSlot());

            if(button == null) return;

            if(gui.clickHandler() != null)
                gui.clickHandler().accept(event);

            if(button.clickHandler() != null)
                button.clickHandler().accept(gui, event.getPlayer());
        });

        node.addListener(InventoryCloseEvent.class, event -> {
           Inventory inventory = event.getInventory();

            if(!(inventory instanceof GUIImpl gui)) return;
            if(!gui.getStomGUI().equals(stomGUI)) return;

            if(gui.closeHandler() != null)
                gui.closeHandler().accept(event);
        });

        return node;
    }
}
