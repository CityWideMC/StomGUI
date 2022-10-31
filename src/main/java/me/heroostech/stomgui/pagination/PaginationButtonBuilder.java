package me.heroostech.stomgui.pagination;

import me.heroostech.stomgui.buttons.Button;
import me.heroostech.stomgui.gui.GUI;

public interface PaginationButtonBuilder {

    Button buildPaginationButton(PaginationButtonType type, GUI inventory);

}
