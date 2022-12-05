package me.heroostech.stomgui.gui;

import lombok.Getter;
import me.heroostech.stomgui.StomGUI;
import me.heroostech.stomgui.button.Button;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

class GUIImpl extends Inventory implements GUI {
    private final InventoryType type;
    private final Consumer<InventoryPreClickEvent> clickHandler;
    private final Consumer<InventoryCloseEvent> closeHandler;
    @Getter private final HashMap<Integer, Button> buttons;
    @Getter private final StomGUI stomGUI;

    public GUIImpl(@NotNull InventoryType type, @NotNull Component title, @NotNull HashMap<Integer, Button> buttons, @NotNull Consumer<InventoryPreClickEvent> clickHandler, @NotNull Consumer<InventoryCloseEvent> closeHandler, @NotNull StomGUI gui, @Nullable Button fillBlanks) {
        super(Objects.requireNonNull(type, "type"), Objects.requireNonNull(title, "title"));
        Objects.requireNonNull(buttons, "buttons");
        Objects.requireNonNull(clickHandler, "clickHandler");
        Objects.requireNonNull(closeHandler, "closeHandler");
        this.type = type;
        this.clickHandler = clickHandler;
        this.closeHandler = closeHandler;
        this.stomGUI = gui;
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
        this.buttons.remove(slot);
        this.buttons.put(slot, button);
    }

    @Override
    public void refreshInventory() {
        this.buttons.forEach((slot, button) -> this.setItemStack(slot, button.stack()));
    }

    static class Builder implements GUI.Builder {
        private Component title;
        private InventoryType type;
        private final HashMap<Integer, Button> buttons;
        private int nextButton;
        private Consumer<InventoryPreClickEvent> clickHandler;
        private Consumer<InventoryCloseEvent> closeHandler;
        private Button fillBlanks;
        private final StomGUI stomGUI;

        public Builder(Component title, InventoryType type, StomGUI stomGUI) {
            this.stomGUI = stomGUI;
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
            return new GUIImpl(type, title, buttons, clickHandler, closeHandler, stomGUI, fillBlanks);
        }
    }
}
