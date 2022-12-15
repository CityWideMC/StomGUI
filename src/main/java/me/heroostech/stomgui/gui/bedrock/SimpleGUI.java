package me.heroostech.stomgui.gui.bedrock;

import me.heroostech.geyserutils.forms.response.SimpleFormResponse;
import me.heroostech.stomgui.gui.bedrock.button.ButtonComponent;
import xyz.citywide.citystom.Extension;

import java.util.UUID;
import java.util.function.Consumer;

public sealed interface SimpleGUI permits SimpleGUIImpl {

    static Builder builder(String title, UUID player, String content, Extension extension) {
        return new SimpleGUIImpl.Builder(player, title, content, extension);
    }

    String title();
    UUID player();
    Consumer<SimpleFormResponse> clickHandler();
    void openGUI();
    void removeListener();

    interface Builder {
        Builder title(String title);
        Builder content(String content);
        Builder button(ButtonComponent button);
        Builder clickHandler(Consumer<SimpleFormResponse> clickHandler);
        SimpleGUI build();
    }

}
