package gdx.awt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdx.editor.EditorApplicationAdapter;

public class Lwjgl2Launcher {
    public static void main(String[] args) {
        Lwjgl2Launcher.createApplication();
    }
    private static LwjglApplication createApplication() {
        return new LwjglApplication(new EditorApplicationAdapter(), Lwjgl2Launcher.getDefaultConfiguration());
    }
    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "gdx-editor";
        configuration.width = 1280;
        configuration.height = 720;
        configuration.vSyncEnabled = false;

        return configuration;
    }
}

