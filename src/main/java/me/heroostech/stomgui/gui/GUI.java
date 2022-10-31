package me.heroostech.stomgui.gui;

import lombok.Getter;
import lombok.Setter;
import me.heroostech.stomgui.StomGUI;
import me.heroostech.stomgui.buttons.Button;
import me.heroostech.stomgui.pagination.PaginationButtonBuilder;
import me.heroostech.stomgui.pagination.PaginationButtonType;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

public class GUI extends Inventory {
    @Setter @Getter private PaginationButtonBuilder builder;

    @Getter private final StomGUI stomGUI;
    @Getter private final Extension owner;
    private final Map<Integer, Button> items;
    private final HashSet<Integer> stickiedSlots;
    @Getter @Setter private int currentPage;

    @Getter private final Consumer<GUI> onClose;
    private final Consumer<GUI> onPageChange;

    public GUI(@NotNull InventoryType inventoryType, @NotNull Component title, @NotNull Extension owner, @NotNull StomGUI stomGUI, @Nullable Consumer<GUI> onClose, @Nullable Consumer<GUI> onPageChange) {
        super(inventoryType, title);
        this.owner = owner;
        this.items = new HashMap<>();
        this.stickiedSlots = new HashSet<>();
        this.onClose = onClose;
        this.onPageChange = onPageChange;
        this.stomGUI = stomGUI;
    }

    /**
     * Returns the slot number of the highest filled slot.
     * This is mainly used to calculate the number of pages there needs to be to
     * display the GUI's contents in the rendered inventory.
     *
     * @return The highest filled slot's number.
     */
    public int getHighestFilledSlot() {
        int slot = 0;

        for (int nextSlot : items.keySet()) {
            if (items.get(nextSlot) != null && nextSlot > slot)
                slot = nextSlot;
        }

        return slot;
    }

    /// BUTTONS ///

    /**
     * Adds the provided {@link Button}.
     *
     * @param button The button to add.
     */
    public void addButton(Button button) {
        // If slot 0 is empty but it's the 'highest filled slot', then set slot 0 to contain button.
        // (This is an edge case for when the whole inventory is empty).
        if (getHighestFilledSlot() == 0 && getButton(0) == null) {
            setButton(0, button);
            return;
        }

        // Otherwise, add one to the highest filled slot, then use that slot for the new button.
        setButton(getHighestFilledSlot() + 1, button);
    }

    /**
     * Adds the provided {@link Button} at the position denoted by the
     * supplied slot parameter.
     * <p>
     * If you specify a value larger than the value of the first page,
     * pagination will be automatically applied when the inventory is
     * rendered. An alternative to this is to use {@link #setButton(int, int, Button)}.
     *
     * @see #setButton(int, int, Button)
     * @param slot The desired location of the button.
     * @param button The button to add.
     */
    public void setButton(int slot, Button button) {
        items.put(slot, button);
    }

    /**
     * Adds the provided {@link Button} at the position denoted by the
     * supplied slot parameter <i>on the page denoted by the supplied page parameter</i>.
     * <p>
     * This is an alias for {@link #setButton(int, Button)}, however one where the slot
     * value is mapped to the specified page. So if page is 2 (the third page) and the
     * inventory row count was 3 (so a size of 27), a supplied slot value of 3 would actually map to
     * a slot value of (2 * 27) + 3 = 54. The mathematical formula for this is <code>(page * pageSize) + slot</code>.
     * <p>
     * If the slot value is out of the bounds of the specified page, this function will do nothing.
     *
     * @see #setButton(int, Button)
     * @param page The page to which the button should be added.
     * @param slot The position on that page the button should be added at.
     * @param button The button to add.
     */
    public void setButton(int page, int slot, Button button) {
        if (slot < 0 || slot > getPageSize())
            return;

        setButton((page * getPageSize()) + slot, button);
    }

    /**
     * Removes a button from the specified slot.
     *
     * @param slot The slot containing the button you wish to remove.
     */
    public void removeButton(int slot) {
        items.remove(slot);
    }

    /**
     * An alias for {@link #removeButton(int)} to remove a button from the specified
     * slot on the specified page.
     * <p>
     * If the slot value is out of the bounds of the specified page, this function will do nothing.
     *
     * @param page The page containing the button you wish to remove.
     * @param slot The slot, of that page, containing the button you wish to remove.
     */
    public void removeButton(int page, int slot) {
        if (slot < 0 || slot > getPageSize())
            return;

        removeButton((page * getPageSize()) + slot);
    }

    /**
     * Returns the {@link Button} in the specified slot.
     * <p>
     * If you attempt to get a slot less than 0 or greater than the slot containing
     * the button at the greatest slot value, this will return null.
     *
     * @param slot The slot containing the button you wish to get.
     * @return The {@link Button} that was in that slot or null if the slot was invalid or if there was no button that slot.
     */
    public Button getButton(int slot) {
        if (slot < 0 || slot > getHighestFilledSlot())
            return null;

        return items.get(slot);
    }

    /**
     * This is an alias for {@link #getButton(int)} that allows you to get a button
     * contained by a slot on a given page.
     *
     * @param page The page containing the button.
     * @param slot The slot, on that page, containing the button.
     * @return The {@link Button} that was in that slot or null if the slot was invalid or if there was no button that slot.
     */
    public Button getButton(int page, int slot) {
        if (slot < 0 || slot > getPageSize())
            return null;

        return getButton((page * getPageSize()) + slot);
    }

    /// PAGINATION ///

    /**
     * Returns the number of slots per page of the inventory. This would be
     * associated with the Minestom API's inventory 'size' parameter.
     *
     * @return The number of inventory slots per page.
     */
    public int getPageSize() {
        return getInventoryType().getSize() * 9;
    }

    /**
     * Gets the page number of the final page of the GUI.
     *
     * @return The highest page number that can be viewed.
     */
    public int getMaxPage() {
        return (int) Math.ceil(((double) getHighestFilledSlot() + 1) / ((double) getPageSize()));
    }

    /**
     * Increments the current page.
     * This will automatically refresh the inventory by calling {@link #refreshInventory(Player)} if
     * the page was changed.
     *
     * @param viewer The {@link Player} viewing the inventory.
     * @return Whether or not the page could be changed (false means the max page is currently open).
     */
    public boolean nextPage(Player viewer) {
        if (currentPage < getMaxPage() - 1) {
            currentPage++;
            refreshInventory(viewer);
            if (this.onPageChange != null) this.onPageChange.accept(this);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Decrements the current page.
     * This will automatically refresh the inventory by calling {@link #refreshInventory(Player)} if
     * the page was changed.
     *
     * @param viewer The {@link Player} viewing the inventory.
     * @return Whether or not the page could be changed (false means the first page is currently open).
     */
    public boolean previousPage(Player viewer) {
        if (currentPage > 0) {
            currentPage--;
            refreshInventory(viewer);
            if (this.onPageChange != null) this.onPageChange.accept(this);
            return true;
        } else {
            return false;
        }
    }

    /// INVENTORY API ///

    public void refreshInventory(Player viewer) {
        // If the open inventory isn't an SGMenu - or if it isn't this inventory, do nothing.
        if (!(viewer.getOpenInventory() instanceof GUI) || viewer.getOpenInventory() != this) return;

        // Otherwise, we can refresh the contents without re-opening the inventory.
        viewer.getOpenInventory().copyContents(getInventory().getItemStacks());
    }
    /**
     * Returns the Bukkit/Spigot {@link Inventory} that represents the GUI.
     * This is shown to a player using {@link Player#openInventory(Inventory)}.
     *
     * @return The created inventory used to display the GUI.
     */
    public Inventory getInventory() {
        var needsPagination = getMaxPage() > 0;

        var inventory = new Inventory(this.getInventoryType(), this.getTitle());

        // Add the main inventory items.
        for (int key = currentPage * getPageSize(); key < (currentPage + 1) * getPageSize(); key++) {
            // If we've already reached the maximum assigned slot, stop assigning
            // slots.
            if (key > getHighestFilledSlot()) break;

            if (items.containsKey(key)) {
                inventory.setItemStack(key - (currentPage * getPageSize()), items.get(key).icon());
            }
        }

        // Update the stickied slots.
        for (int stickiedSlot : stickiedSlots) {
            inventory.setItemStack(stickiedSlot, items.get(stickiedSlot).icon());
        }

        // Render the pagination items.
        if (needsPagination) {
            PaginationButtonBuilder paginationButtonBuilder = stomGUI.getDefaultPaginationButtonBuilder();

            if (builder != null) {
                paginationButtonBuilder = builder;
            }

            int pageSize = getPageSize();
            for (int i = pageSize; i < pageSize + 9; i++) {
                int offset = i - pageSize;

                Button paginationButton = paginationButtonBuilder.buildPaginationButton(
                        PaginationButtonType.forSlot(offset),this
                );
                inventory.setItemStack(i, paginationButton.icon());
            }
        }

        return inventory;
    }

    /// STICKY SLOTS ///

    /**
     * Marks a slot as 'sticky', so that when the page is changed,
     * the slot will always display the value on the first page.
     * <p>
     * This is useful for implementing things like 'toolbars', where
     * you have a set of common items on every page.
     * <p>
     * If the slot is out of the bounds of the first page (i.e. less
     * than 0 or greater than {@link #getPageSize()} - 1) this method
     * will do nothing.
     *
     * @param slot The slot to mark as 'sticky'.
     */
    public void stickSlot(int slot) {
        if (slot < 0 || slot >= getPageSize())
            return;

        this.stickiedSlots.add(slot);
    }

    /**
     * Un-marks a slot as sticky - thereby meaning that slot will display
     * whatever its value on the current page is.
     *
     * @see #stickSlot(int)
     * @param slot The slot to un-mark as 'sticky'.
     */
    public void unstickSlot(int slot) {
        this.stickiedSlots.remove(slot);
    }

    /**
     * This clears all the 'stuck' slots - essentially un-marking all
     * stuck slots.
     *
     * @see #stickSlot(int)
     */
    public void clearStickiedSlots() {
        this.stickiedSlots.clear();
    }

    /**
     * This checks whether a given slot is sticky.
     * If the slot is out of bounds of the first page (as defined by
     * the same parameters as {@link #stickSlot(int)}), this will return
     * false.
     *
     * @see #stickSlot(int)
     * @param slot The slot to check.
     * @return True if the slot is sticky, false if it isn't or the slot was out of bounds.
     */
    public boolean isStickiedSlot(int slot) {
        if (slot < 0 || slot >= getPageSize())
            return false;

        return this.stickiedSlots.contains(slot);
    }

    /**
     * This clears all slots in the inventory, except those which
     * have been marked as 'sticky'.
     *
     * @see #stickSlot(int)
     */
    public void clearAllButStickiedSlots() {
        this.currentPage = 0;
        items.entrySet().removeIf(item -> !isStickiedSlot(item.getKey()));
    }
}
