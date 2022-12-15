package xyz.citywide.stomgui.gui.bedrock;

import lombok.Getter;
import me.heroostech.geyserutils.FloodgateApi;
import me.heroostech.geyserutils.forms.ModalForm;
import me.heroostech.geyserutils.forms.response.ModalFormResponse;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.NotNull;
import xyz.citywide.citystom.Extension;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

final class ModalGUIImpl implements ModalGUI {
    private final UUID player;
    private final Consumer<ModalFormResponse> clickHandler;
    private final EventListener<ModalFormResponse> listener;
    private final ModalForm form;
    @Getter private final String content;
    @Getter private final String title;
    @Getter private final Extension extension;

    public ModalGUIImpl(@NotNull UUID player, @NotNull String title, @NotNull String content, @NotNull String button1, @NotNull String button2, @NotNull Consumer<ModalFormResponse> clickHandler, @NotNull Extension extension) {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(content, "content");
        Objects.requireNonNull(button1, "button1");
        Objects.requireNonNull(button2, "button2");
        Objects.requireNonNull(clickHandler, "clickHandler");
        Objects.requireNonNull(extension, "extension");
        this.title = title;
        this.content = content;
        this.clickHandler = clickHandler;
        this.extension = extension;
        this.player = player;
        this.form = new ModalForm(title, content, button1, button2, player);
        this.listener = EventListener.of(ModalFormResponse.class, event -> {
            if(event.form().equals(form)) {
                this.clickHandler.accept(event);
                removeListener();
            }
        });
    }

    @Override
    public String title() {
        return getTitle();
    }

    @Override
    public UUID player() {
        return player;
    }

    @Override
    public Consumer<ModalFormResponse> clickHandler() {
        return clickHandler;
    }

    @Override
    public void openGUI() {
        FloodgateApi.getInstance().sendForm(form);
    }

    @Override
    public void removeListener() {
        EventNode<Event> node =  extension.getEventNode().findChildren("stomgui").get(0);
        node.removeListener(listener);
    }

    static class Builder implements ModalGUI.Builder {
        private final UUID player;
        private String title;
        private String content;
        private Consumer<ModalFormResponse> clickHandler;
        private String button1;
        private String button2;
        private final Extension extension;

        public Builder(UUID player, String title, String content, Extension extension) {
            this.extension = extension;
            this.player = player;
            this.title = title;
            this.content = content;
        }

        @Override
        public ModalGUI.Builder title(String title) {
            this.title = title;
            return this;
        }

        @Override
        public ModalGUI.Builder content(String content) {
            this.content = content;
            return this;
        }

        @Override
        public ModalGUI.Builder button1(String button) {
            this.button1 = button;
            return this;
        }

        @Override
        public ModalGUI.Builder button2(String button) {
            this.button2 = button;
            return this;
        }

        @Override
        public ModalGUI.Builder clickHandler(Consumer<ModalFormResponse> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        @Override
        public ModalGUI build() {
            return new ModalGUIImpl(player, title, content, button1, button2, clickHandler, extension);
        }
    }
}
