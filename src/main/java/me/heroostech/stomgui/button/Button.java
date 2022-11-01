package me.heroostech.stomgui.button;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;

public record Button(ItemStack stack, Consumer<InventoryPreClickEvent> clickHandler) {}
