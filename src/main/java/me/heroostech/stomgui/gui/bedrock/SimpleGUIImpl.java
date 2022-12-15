package me.heroostech.stomgui.gui.bedrock;

import lombok.Getter;
import me.heroostech.geyserutils.FloodgateApi;
import me.heroostech.geyserutils.forms.SimpleForm;
import me.heroostech.geyserutils.forms.response.SimpleFormResponse;
import me.heroostech.geyserutils.util.FormImage;
import me.heroostech.stomgui.gui.bedrock.button.ButtonComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.NotNull;
import xyz.citywide.citystom.Extension;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

final class SimpleGUIImpl implements SimpleGUI {
    private final UUID player;
    private final Consumer<SimpleFormResponse> clickHandler;
    private final EventListener<SimpleFormResponse> listener;
    private final SimpleForm form;
    @Getter private final String content;
    @Getter private final String title;
    @Getter private final HashMap<Integer, ButtonComponent> buttons;
    @Getter private final Extension extension;

    public SimpleGUIImpl(@NotNull UUID player, @NotNull String title, @NotNull String content, @NotNull HashMap<Integer, ButtonComponent> buttons, @NotNull Consumer<SimpleFormResponse> clickHandler, @NotNull Extension extension) {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(content, "content");
        Objects.requireNonNull(buttons, "buttons");
        Objects.requireNonNull(clickHandler, "clickHandler");
        Objects.requireNonNull(extension, "extension");
        this.title = title;
        this.content = content;
        this.clickHandler = clickHandler;
        this.extension = extension;
        this.buttons = buttons;
        this.player = player;
        this.form = new SimpleForm(title, content, buttons.values().stream().map(button -> new me.heroostech.geyserutils.component.ButtonComponent(button.text(), new FormImage(FormImage.Type.fromName(button.image().type().name()), button.image().data()))).toList(), player);
        this.listener = EventListener.of(SimpleFormResponse.class, event -> {
            if(event.form().equals(form)) {
                this.clickHandler.accept(event);
                this.buttons.values().forEach(button -> button.clickHandler().accept(this, MinecraftServer.getConnectionManager().getPlayer(player)));
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
    public Consumer<SimpleFormResponse> clickHandler() {
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

    static class Builder implements SimpleGUI.Builder {
        private final UUID player;
        private String title;
        private String content;
        private final HashMap<Integer, ButtonComponent> buttons;
        private int nextButton;
        private Consumer<SimpleFormResponse> clickHandler;
        private final Extension extension;

        public Builder(UUID player, String title, String content, Extension extension) {
            this.extension = extension;
            this.player = player;
            buttons = new HashMap<>();
            nextButton = 0;
            this.title = title;
            this.content = content;
        }

        @Override
        public SimpleGUI.Builder title(String title) {
            this.title = title;
            return this;
        }

        @Override
        public SimpleGUI.Builder content(String content) {
            this.content = content;
            return this;
        }

        @Override
        public SimpleGUI.Builder button(ButtonComponent button) {
            buttons.put(nextButton, button);
            nextButton++;
            return this;
        }

        @Override
        public SimpleGUI.Builder clickHandler(Consumer<SimpleFormResponse> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        @Override
        public SimpleGUI build() {
            return new SimpleGUIImpl(player, title, content, buttons, clickHandler, extension);
        }
    }
}
