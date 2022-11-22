package gdx.awt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdx.awt.SceneViewer;
import gdx.editor.EditorGame;

public class CreateEditor {
    public static void main(String[] args) {
        CreateEditor.createApplication();
    }
    private static LwjglApplication createApplication() {
        return new LwjglApplication(new EditorGame(), CreateEditor.getDefaultConfiguration());
    }
    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "gdx-editor";
        configuration.width = 1280;
        configuration.height = 720;
        return configuration;
    }
}

