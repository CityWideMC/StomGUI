package me.heroostech.stomgui.buttons;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;

public record Button(Consumer<InventoryPreClickEvent> listener, ItemStack icon) {}
