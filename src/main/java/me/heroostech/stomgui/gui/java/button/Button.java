package me.heroostech.stomgui.gui.java.button;

import me.heroostech.stomgui.gui.java.GUI;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record Button(@NotNull ItemStack stack, @NotNull BiConsumer<GUI, Player> clickHandler) {}
