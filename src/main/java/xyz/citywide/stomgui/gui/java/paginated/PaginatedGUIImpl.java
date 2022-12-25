package xyz.citywide.stomgui.gui.java.paginated;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.citywide.citystom.Extension;
import xyz.citywide.stomgui.gui.java.paginated.pages.Page;

import java.util.HashMap;
import java.util.function.Consumer;

final class PaginatedGUIImpl extends Inventory implements PaginatedGUI {

    private final Component title;
    private final InventoryType type;
    private final Consumer<InventoryPreClickEvent> clickHandler;
    private final Consumer<InventoryCloseEvent> closeHandler;
    @Getter private final Extension extension;
    @Getter private final HashMap<Integer, Page> pages;
    private int page;

    public PaginatedGUIImpl(@NotNull Component title, @NotNull InventoryType type, @NotNull HashMap<Integer, Page> pages, Consumer<InventoryPreClickEvent> clickHandler, Consumer<InventoryCloseEvent> closeHandler, @NotNull Extension extension) {
        super(type, title);
        this.title = title;
        this.type = type;
        this.clickHandler = clickHandler;
        this.closeHandler = closeHandler;
        this.extension = extension;
        this.pages = pages;
        page = 0;
        refreshInventory();
    }

    @Override
    public Component title() {
        return title;
    }

    @Override
    public InventoryType type() {
        return type;
    }

    @Override
    public Consumer<InventoryPreClickEvent> clickHandler() {
        return clickHandler;
    }

    @Override
    public Consumer<InventoryCloseEvent> closeHandler() {
        return closeHandler;
    }

    @Override
    public void openInventory(Player player) {
        player.openInventory(this);
    }

    @Override
    public @Nullable Page getPage(int index) {
        return pages.get(index);
    }

    @Override
    public void refreshInventory() {
        Page currentPage = pages.get(page);
        currentPage.buttons().forEach((index, button) -> setItemStack(index, button.stack()));
    }

    @Override
    public void nextPage() {
        page++;
        refreshInventory();
    }

    @Override
    public void previousPage() {
        page--;
        refreshInventory();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
        refreshInventory();
    }

    @Override
    public int page() {
        return page;
    }

    static class Builder implements PaginatedGUI.Builder {

        private Component title;
        private InventoryType type;
        private Consumer<InventoryPreClickEvent> clickHandler;
        private Consumer<InventoryCloseEvent> closeHandler;
        private int nextPage;
        private final Extension extension;
        private final HashMap<Integer, Page> pages;

        public Builder(Component title, InventoryType type, Extension extension) {
            this.extension = extension;
            this.title = title;
            this.type = type;
            this.pages = new HashMap<>();
            nextPage = 0;
        }

        @Override
        public PaginatedGUI.Builder title(Component title) {
            this.title = title;
            return this;
        }

        @Override
        public PaginatedGUI.Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        @Override
        public PaginatedGUI.Builder page(Page page) {
            page(nextPage, page);
            nextPage++;
            return this;
        }

        @Override
        public PaginatedGUI.Builder page(int index, Page page) {
            if(pages.containsKey(index))
                pages.remove(index, pages.get(index));
            pages.put(index, page);
            return this;
        }

        @Override
        public PaginatedGUI.Builder clickHandler(Consumer<InventoryPreClickEvent> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        @Override
        public PaginatedGUI.Builder closeHandler(Consumer<InventoryCloseEvent> closeHandler) {
            this.closeHandler = closeHandler;
            return this;
        }

        @Override
        public PaginatedGUI build() {
            return new PaginatedGUIImpl(title, type, pages, clickHandler, closeHandler, extension);
        }
    }
}
