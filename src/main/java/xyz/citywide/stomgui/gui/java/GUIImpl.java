package xyz.citywide.stomgui.gui.java;

import lombok.Getter;
import xyz.citywide.stomgui.gui.java.button.Button;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.citywide.citystom.Extension;

import java.util.HashMap;
import java.util.function.Consumer;

final class GUIImpl extends Inventory implements GUI {
    private final InventoryType type;
    private final Consumer<InventoryPreClickEvent> clickHandler;
    private final Consumer<InventoryCloseEvent> closeHandler;
    @Getter private final HashMap<Integer, Button> buttons;
    @Getter private final Extension extension;

    public GUIImpl(@NotNull InventoryType type, @NotNull Component title, @NotNull HashMap<Integer, Button> buttons, Consumer<InventoryPreClickEvent> clickHandler, Consumer<InventoryCloseEvent> closeHandler, Extension extension, @Nullable Button fillBlanks) {
        super(type, title);
        this.type = type;
        this.clickHandler = clickHandler;
        this.closeHandler = closeHandler;
        this.extension = extension;
        this.buttons = buttons;
        buttons.forEach((integer, button) -> setItemStack(integer, button.stack()));
        if(fillBlanks != null) {
            for (int i = 0; i < this.getInventoryType().getSize(); i++) {
                if (!buttons.containsKey(i)) {
                    buttons.put(i, fillBlanks);
                    setItemStack(i, fillBlanks.stack());
                }
            }
        }
    }

    @Override
    public Component title() {
        return getTitle();
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
    public void setButton(int slot, Button button) {
        if(buttons.containsKey(slot))
            buttons.remove(slot, buttons.get(slot));
        buttons.put(slot, button);
    }

    @Override
    public void refreshInventory() {
        buttons.forEach((slot, button) -> setItemStack(slot, button.stack()));
    }

    @Override
    public void callClose(Player player) {
        closeHandler.accept(new InventoryCloseEvent(this, player));
    }

    static class Builder implements GUI.Builder {
        private Component title;
        private InventoryType type;
        private final HashMap<Integer, Button> buttons;
        private int nextButton;
        private Consumer<InventoryPreClickEvent> clickHandler;
        private Consumer<InventoryCloseEvent> closeHandler;
        private Button fillBlanks;
        private final Extension extension;

        public Builder(Component title, InventoryType type, Extension extension) {
            this.extension = extension;
            buttons = new HashMap<>();
            nextButton = 0;
            this.title = title;
            this.type = type;
        }

        @Override
        public GUI.Builder title(Component title) {
            this.title = title;
            return this;
        }

        @Override
        public GUI.Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        @Override
        public GUI.Builder button(Button button) {
            if(nextButton > type.getSize() - 1)
                throw new UnsupportedOperationException();
            buttons.put(nextButton, button);
            nextButton++;
            return this;
        }

        @Override
        public GUI.Builder button(int slot, Button button) {
            if(slot > type.getSize() - 1)
                throw new UnsupportedOperationException();
            buttons.put(slot, button);
            nextButton = slot + 1;
            return this;
        }

        @Override
        public GUI.Builder clickHandler(Consumer<InventoryPreClickEvent> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        @Override
        public GUI.Builder closeHandler(Consumer<InventoryCloseEvent> closeHandler) {
            this.closeHandler = closeHandler;
            return this;
        }

        @Override
        public GUI.Builder fillBlanks(Button button) {
            this.fillBlanks = button;
            return this;
        }

        @Override
        public GUI build() {
            return new GUIImpl(type, title, buttons, clickHandler, closeHandler, extension, fillBlanks);
        }
    }
}
