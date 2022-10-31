package me.heroostech.stomgui;

import lombok.Getter;
import me.heroostech.cityengine.extension.Extension;
import me.heroostech.stomgui.buttons.Button;
import me.heroostech.stomgui.gui.GUI;
import me.heroostech.stomgui.listeners.GUIListener;
import me.heroostech.stomgui.pagination.PaginationButtonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class StomGUI {

    @Getter private final Extension extension;

    public StomGUI(@NotNull Extension extension) {
        this.extension = extension;
        extension.registerEvent(new GUIListener(extension, this));
    }

    /**
     * The defaultPaginationButtonBuilder is the plugin-wide {@link PaginationButtonBuilder}
     * called when building pagination buttons for inventory GUIs.
     * <p>
     * This can be overridden per-inventory, as well as per-plugin using the appropriate methods
     * on either the inventory class ({@link GUI}) or your plugin's instance of
     * {@link StomGUI}.
     */
    @Getter private final PaginationButtonBuilder defaultPaginationButtonBuilder = (type, inventory) -> {
        switch (type) {
            case PREV_BUTTON:
                if (inventory.getCurrentPage() > 0) return new Button(event -> {
                    event.setCancelled(true);
                    inventory.previousPage(event.getPlayer());
                }, ItemStack.builder(Material.ARROW)
                        .displayName(Component.text("\u2190 Previous Page").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                        .lore(Component.text("&aClick to move back to"), Component.text("&apage " + inventory.getCurrentPage() + "."))
                        .build());
                else return null;

            case CURRENT_BUTTON:
                return new Button(event -> event.setCancelled(true),
                        ItemStack.builder(Material.NAME_TAG)
                                .displayName(Component.text("&7&lPage " + (inventory.getCurrentPage() + 1) + " of " + inventory.getMaxPage()))
                                .lore(Component.text("&7You are currently viewing"), Component.text("&7page " + (inventory.getCurrentPage() + 1) + "."))
                                .build());

            case NEXT_BUTTON:
                if (inventory.getCurrentPage() < inventory.getMaxPage() - 1) return new Button(event -> {
                    event.setCancelled(true);
                    inventory.nextPage(event.getPlayer());
                }, ItemStack.builder(Material.ARROW)
                        .displayName(Component.text("&a&lNext Page \u2192"))
                        .lore(Component.text("&aClick to move forward to"), Component.text("&apage " + (inventory.getCurrentPage() + 2) + "."))
                        .build()
                );
                else return null;

            case UNASSIGNED:
            default:
                return null;
        }
    };
}