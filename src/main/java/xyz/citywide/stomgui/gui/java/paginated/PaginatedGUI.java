package xyz.citywide.stomgui.gui.java.paginated;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;
import xyz.citywide.citystom.Extension;
import xyz.citywide.stomgui.gui.java.paginated.pages.Page;

import java.util.function.Consumer;

public sealed interface PaginatedGUI permits PaginatedGUIImpl {

    static Builder builder(Component title, InventoryType type, Extension extension) {
        return new PaginatedGUIImpl.Builder(title, type, extension);
    }

    Component title();
    InventoryType type();
    Consumer<InventoryPreClickEvent> clickHandler();
    Consumer<InventoryCloseEvent> closeHandler();
    void openInventory(Player player);
    @Nullable Page getPage(int index);
    void refreshInventory();
    void nextPage();
    void previousPage();
    void setPage(int page);
    void callClose(Player player);
    int page();

    interface Builder {
        Builder title(Component title);
        Builder type(InventoryType type);
        Builder page(Page page);
        Builder page(int index, Page page);
        Builder clickHandler(Consumer<InventoryPreClickEvent> clickHandler);
        Builder closeHandler(Consumer<InventoryCloseEvent> closeHandler);
        PaginatedGUI build();
    }

}
