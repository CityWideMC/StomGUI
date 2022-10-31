package me.heroostech.stomgui.listeners;

import lombok.RequiredArgsConstructor;
import me.heroostech.cityengine.extension.Extension;
import me.heroostech.cityengine.extension.listener.Listener;
import me.heroostech.stomgui.StomGUI;
import me.heroostech.stomgui.buttons.Button;
import me.heroostech.stomgui.gui.GUI;
import me.heroostech.stomgui.pagination.PaginationButtonBuilder;
import me.heroostech.stomgui.pagination.PaginationButtonType;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.PlayerEvent;

@RequiredArgsConstructor
public class GUIListener implements Listener<PlayerEvent> {

    private final Extension owner;
    private final StomGUI stomGUI;

    @Override
    public EventNode<PlayerEvent> events() {
        var node = EventNode.type("stomgui", EventFilter.PLAYER);

        node.addListener(InventoryPreClickEvent.class, event -> {
            var inventory = event.getInventory();

            if(!(inventory instanceof GUI gui)) return;
            if(!gui.getOwner().equals(owner)) return;

            // If the slot is on the pagination row, get the appropriate pagination handler.
            if (event.getSlot() > gui.getPageSize()) {
                int offset = event.getSlot() - gui.getPageSize();
                PaginationButtonBuilder paginationButtonBuilder = stomGUI.getDefaultPaginationButtonBuilder();

                if (gui.getBuilder() != null)
                    paginationButtonBuilder = gui.getBuilder();

                PaginationButtonType buttonType = PaginationButtonType.forSlot(offset);
                Button paginationButton = paginationButtonBuilder.buildPaginationButton(buttonType, gui);
                if (paginationButton != null) paginationButton.listener().accept(event);
                return;
            }

            // If the slot is a stickied slot, get the button from page 0.
            if (gui.isStickiedSlot(event.getSlot())) {
                Button button = gui.getButton(0, event.getSlot());
                if (button != null && button.listener() != null) button.listener().accept(event);
                return;
            }

            // Otherwise, get the button normally.
            Button button = gui.getButton(gui.getCurrentPage(), event.getSlot());
            if (button != null && button.listener() != null) {
                button.listener().accept(event);
            }
        });

        node.addListener(InventoryCloseEvent.class, event -> {
            var inventory = event.getInventory();
            // Determine if the inventory was a SpiGUI.
            if (!(event.getInventory() instanceof GUI gui)) return;

            // Check if the GUI is owner by the current plugin
            // (if not, it'll be deferred to the SGMenuListener registered
            // by that plugin that does own the GUI.)
            if (!gui.getOwner().equals(owner)) return;

            // If all the above is true and the inventory's onClose is not null,
            // call it.
            if (gui.getOnClose() != null)
                gui.getOnClose().accept(gui);
        });

        return node;
    }
}
