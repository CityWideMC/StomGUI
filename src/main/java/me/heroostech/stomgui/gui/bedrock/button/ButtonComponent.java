package me.heroostech.stomgui.gui.bedrock.button;

import me.heroostech.stomgui.gui.bedrock.SimpleGUI;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record ButtonComponent(@NotNull String text, @NotNull FormImage image, @NotNull BiConsumer<SimpleGUI, Player> clickHandler) {}
