package xyz.citywide.stomgui.gui.java.paginated.pages;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import xyz.citywide.citystom.Extension;
import xyz.citywide.stomgui.gui.java.paginated.button.Button;

import java.util.Map;
import java.util.function.Consumer;

public sealed interface Page permits PageImpl {

    static Builder builder(@NotNull InventoryType type, @NotNull Extension extension) {
        return new PageImpl.Builder(type, extension);
    }

    Consumer<InventoryPreClickEvent> clickHandler();
    Consumer<InventoryCloseEvent> closeHandler();
    void setButton(int slot, Button button);
    Map<Integer, Button> buttons();

    interface Builder {
        Builder button(Button button);
        Builder button(int slot, Button button);
        Builder clickHandler(Consumer<InventoryPreClickEvent> clickHandler);
        Builder closeHandler(Consumer<InventoryCloseEvent> closeHandler);
        Builder fillBlanks(Button button);
        Page build();
    }

}
