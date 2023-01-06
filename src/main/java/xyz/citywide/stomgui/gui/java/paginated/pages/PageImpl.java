package xyz.citywide.stomgui.gui.java.paginated.pages;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.citywide.citystom.Extension;
import xyz.citywide.stomgui.gui.java.paginated.button.Button;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

final class PageImpl implements Page {
    private final Consumer<InventoryPreClickEvent> clickHandler;
    private final Consumer<InventoryCloseEvent> closeHandler;
    @Getter private final HashMap<Integer, Button> buttons;
    @Getter private final Extension extension;

    public PageImpl(@NotNull InventoryType type, HashMap<Integer, Button> buttons, Consumer<InventoryPreClickEvent> clickHandler, Consumer<InventoryCloseEvent> closeHandler, Extension extension, @Nullable Button fillBlanks) {
        this.clickHandler = clickHandler;
        this.closeHandler = closeHandler;
        this.extension = extension;
        this.buttons = buttons;
        if(fillBlanks != null)
            for (int i = 0; i < type.getSize(); i++)
                if (!buttons.containsKey(i))
                    buttons.put(i, fillBlanks);
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
    public void setButton(int slot, Button button) {
        if(buttons.containsKey(slot))
            buttons.remove(slot, buttons.get(slot));
        buttons.put(slot, button);
    }

    @Override
    public Map<Integer, Button> buttons() {
        return Collections.unmodifiableMap(buttons);
    }

    static class Builder implements Page.Builder {
        private final InventoryType type;
        private final HashMap<Integer, Button> buttons;
        private int nextButton;
        private Consumer<InventoryPreClickEvent> clickHandler;
        private Consumer<InventoryCloseEvent> closeHandler;
        private Button fillBlanks;
        private final Extension extension;

        public Builder(InventoryType type, Extension extension) {
            this.type = type;
            this.extension = extension;
            buttons = new HashMap<>();
            nextButton = 0;
        }

        @Override
        public Page.Builder button(Button button) {
            if(nextButton > type.getSize() - 1)
                throw new UnsupportedOperationException();
            buttons.put(nextButton, button);
            nextButton++;
            return this;
        }

        @Override
        public Page.Builder button(int slot, Button button) {
            if(slot > type.getSize() - 1)
                throw new UnsupportedOperationException();
            buttons.put(slot, button);
            nextButton = slot + 1;
            return this;
        }

        @Override
        public Page.Builder clickHandler(Consumer<InventoryPreClickEvent> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        @Override
        public Page.Builder closeHandler(Consumer<InventoryCloseEvent> closeHandler) {
            this.closeHandler = closeHandler;
            return this;
        }

        @Override
        public Page.Builder fillBlanks(Button button) {
            this.fillBlanks = button;
            return this;
        }

        @Override
        public Page build() {
            return new PageImpl(type, buttons, clickHandler, closeHandler, extension, fillBlanks);
        }
    }
}
