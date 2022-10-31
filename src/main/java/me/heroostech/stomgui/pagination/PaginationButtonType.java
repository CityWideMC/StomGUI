package me.heroostech.stomgui.pagination;

public enum PaginationButtonType {

    PREV_BUTTON(3),
    CURRENT_BUTTON(4),
    NEXT_BUTTON(5),
    UNASSIGNED(0);

    private final int slot;

    PaginationButtonType(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public static PaginationButtonType forSlot(int slot) {
        for (PaginationButtonType buttonType : PaginationButtonType.values()) {
            if (buttonType.slot == slot) return buttonType;
        }

        return PaginationButtonType.UNASSIGNED;
    }

}
