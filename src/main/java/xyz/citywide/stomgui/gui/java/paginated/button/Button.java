package xyz.citywide.stomgui.gui.java.paginated.button;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.citywide.stomgui.gui.java.paginated.PaginatedGUI;

import java.util.function.BiConsumer;

public record Button(@NotNull ItemStack stack, @Nullable BiConsumer<PaginatedGUI, Player> clickHandler) {}
