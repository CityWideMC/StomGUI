package xyz.citywide.stomgui.gui.bedrock.button;

public record FormImage(Type type, String data) {
    public enum Type {
        PATH, URL;
    }
}
