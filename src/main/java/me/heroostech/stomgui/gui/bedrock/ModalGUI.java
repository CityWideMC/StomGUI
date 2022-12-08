package me.heroostech.stomgui.gui.bedrock;

import me.heroostech.citystom.Extension;
import me.heroostech.geyserutils.forms.response.ModalFormResponse;

import java.util.UUID;
import java.util.function.Consumer;

public sealed interface ModalGUI permits ModalGUIImpl {

    static Builder builder(String title, UUID player, String content, Extension extension) {
        return new ModalGUIImpl.Builder(player, title, content, extension);
    }

    String title();
    UUID player();
    Consumer<ModalFormResponse> clickHandler();
    void openGUI();
    void removeListener();

    interface Builder {
        Builder title(String title);
        Builder content(String content);
        ModalGUI.Builder button1(String button);
        ModalGUI.Builder button2(String button);
        Builder clickHandler(Consumer<ModalFormResponse> clickHandler);
        ModalGUI build();
    }

}
