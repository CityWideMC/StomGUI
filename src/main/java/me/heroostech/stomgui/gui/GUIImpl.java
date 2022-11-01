package me.heroostech.stomgui.gui;

import lombok.Getter;
import me.heroostech.cityengine.player.Player;
import me.heroostech.stomgui.StomGUI;
import me.heroostech.stomgui.button.Button;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

class GUIImpl extends Inventory implements GUI {
    private final GUIType type;
    private final Consumer<InventoryPreClickEvent> clickHandler;
    private final Consumer<InventoryCloseEvent> closeHandler;
    @Getter private final HashMap<Integer, Button> buttons;
    @Getter private final StomGUI stomGUI;

    public GUIImpl(@NotNull GUIType type, @NotNull Component title, @NotNull HashMap<Integer, Button> buttons, Consumer<InventoryPreClickEvent> clickHandler, Consumer<InventoryCloseEvent> closeHandler, @NotNull StomGUI gui, Button fillBlanks) {
        super(type.getType(), title);
        this.type = type;
        this.clickHandler = clickHandler;
        this.closeHandler = closeHandler;
        this.stomGUI = gui;
        this.buttons = buttons;
        for(int i = 0; i < this.getInventoryType().getSize(); i++)
            setItemStack(i, fillBlanks.stack());
        buttons.forEach((integer, button) -> setItemStack(integer, button.stack()));
    }

    @Override
    public Component title() {
        return getTitle();
    }

    @Override
    public GUIType type() {
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

    static class Builder implements GUI.Builder {
        private Component title;
        private GUIType type;
        private final HashMap<Integer, Button> buttons;
        private int nextButton;
        private Consumer<InventoryPreClickEvent> clickHandler;
        private Consumer<InventoryCloseEvent> closeHandler;
        private Button fillBlanks;
        private final StomGUI stomGUI;

        public Builder(Component title, GUIType type, StomGUI stomGUI) {
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
        public GUI.Builder type(GUIType type) {
            this.type = type;
            return this;
        }

        @Override
        public GUI.Builder button(Button button) {
            if(nextButton > type.getType().getSize() - 1)
                throw new UnsupportedOperationException();
            buttons.put(nextButton, button);
            nextButton++;
            return this;
        }

        @Override
        public GUI.Builder button(int slot, Button button) {
            if(slot > type.getType().getSize() - 1)
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
