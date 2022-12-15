package xyz.citywide.stomgui.gui.java;

import xyz.citywide.stomgui.gui.java.button.Button;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import xyz.citywide.citystom.Extension;

import java.util.function.Consumer;

public sealed interface GUI permits GUIImpl {

    static Builder builder(Component title, InventoryType type, Extension extension) {
        return new GUIImpl.Builder(title, type, extension);
    }

    Component title();
    InventoryType type();
    Consumer<InventoryPreClickEvent> clickHandler();
    Consumer<InventoryCloseEvent> closeHandler();
    void openInventory(Player player);
    void setButton(int slot, Button button);
    void refreshInventory();

    interface Builder {
        Builder title(Component title);
        Builder type(InventoryType type);
        Builder button(Button button);
        Builder button(int slot, Button button);
        Builder clickHandler(Consumer<InventoryPreClickEvent> clickHandler);
        Builder closeHandler(Consumer<InventoryCloseEvent> closeHandler);
        Builder fillBlanks(Button button);
        GUI build();
    }

}
