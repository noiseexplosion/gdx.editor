package gdx.awt;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Editor extends Game {
	@Override
	public void create() {
		this.setScreen(new RenderContext());
	}
}