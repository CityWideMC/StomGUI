package me.heroostech.stomgui.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.heroostech.stomgui.StomGUI;
import me.heroostech.stomgui.button.Button;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;

import java.util.function.Consumer;

public interface GUI {

    static Builder builder(Component title, GUIType type, StomGUI stomGUI) {
        return new GUIImpl.Builder(title, type, stomGUI);
    }

    Component title();
    GUIType type();
    Consumer<InventoryPreClickEvent> clickHandler();
    Consumer<InventoryCloseEvent> closeHandler();
    void openInventory(Player player);
    void setButton(int slot, Button button);
    void refreshInventory();

    @RequiredArgsConstructor
    enum GUIType {
        ROW_2(InventoryType.CHEST_2_ROW),
        ROW_3(InventoryType.CHEST_3_ROW),
        ROW_4(InventoryType.CHEST_4_ROW),
        ROW_5(InventoryType.CHEST_5_ROW),
        ROW_6(InventoryType.CHEST_6_ROW);

        @Getter
        private final InventoryType type;
    }

    interface Builder {
        Builder title(Component title);
        Builder type(GUIType type);
        Builder button(Button button);
        Builder button(int slot, Button button);
        Builder clickHandler(Consumer<InventoryPreClickEvent> clickHandler);
        Builder closeHandler(Consumer<InventoryCloseEvent> closeHandler);
        Builder fillBlanks(Button button);
        GUI build();
    }

}
