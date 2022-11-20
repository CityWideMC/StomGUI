package me.heroostech.stomgui.button;

import me.heroostech.stomgui.gui.GUI;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

import java.util.function.BiConsumer;

public record Button(ItemStack stack, BiConsumer<GUI, Player> clickHandler) {}
