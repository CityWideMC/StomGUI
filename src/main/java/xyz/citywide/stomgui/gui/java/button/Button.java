package xyz.citywide.stomgui.gui.java.button;

import xyz.citywide.stomgui.gui.java.GUI;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public record Button(@NotNull ItemStack stack, @Nullable BiConsumer<GUI, Player> clickHandler) {}
