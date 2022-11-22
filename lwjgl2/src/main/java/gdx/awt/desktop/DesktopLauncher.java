package gdx.awt.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdx.awt.Editor;
import gdx.editor.EditorGame;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
	public static void main(String[] args) {
		DesktopLauncher.createApplication();
	}

	private static LwjglApplication createApplication() {
		return new LwjglApplication(new EditorGame(), DesktopLauncher.getDefaultConfiguration());
	}

	private static LwjglApplicationConfiguration getDefaultConfiguration() {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "gdx-editor";
		configuration.width = 640;
		configuration.height = 480;
		//// This prevents a confusing error that would appear after exiting normally.
		configuration.forceExit = false;

		for (int size : new int[] { 128, 64, 32, 16 }) {
			configuration.addIcon("libgdx" + size + ".png", Files.FileType.Internal);
		}
		return configuration;
	}
}