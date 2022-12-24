package xyz.citywide.stomgui.gui.java.paginated;

import lombok.RequiredArgsConstructor;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import xyz.citywide.citystom.Extension;
import xyz.citywide.stomgui.gui.java.paginated.button.Button;

import java.util.Objects;

@RequiredArgsConstructor
public final class PaginatedGUIListener {

    private final Extension extension;

    public EventNode<InventoryEvent> events() {
        EventNode<InventoryEvent> node = EventNode.type("stomgui", EventFilter.INVENTORY);

        node.addListener(InventoryPreClickEvent.class, event -> {
            Inventory inventory = event.getInventory();

            if(!(inventory instanceof PaginatedGUIImpl gui)) return;
            if(!gui.getExtension().equals(extension)) return;

            event.setCancelled(true);

            Button button = Objects.requireNonNull(gui.getPage(gui.page())).buttons().get(event.getSlot());

            if(button == null) return;

            if(gui.clickHandler() != null)
                gui.clickHandler().accept(event);

            if(button.clickHandler() != null)
                button.clickHandler().accept(gui, event.getPlayer());
        });

        node.addListener(InventoryCloseEvent.class, event -> {
           Inventory inventory = event.getInventory();

            if(!(inventory instanceof PaginatedGUIImpl gui)) return;
            if(!gui.getExtension().equals(extension)) return;

            if(gui.closeHandler() != null)
                gui.closeHandler().accept(event);
        });

        return node;
    }
}