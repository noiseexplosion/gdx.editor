package gdx.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.bullet.Bullet;
import gdx.awt.bullet.screens.SelectScreen;

public class EditorGame extends Game
{
    public EditorScreenSelection screen;

    @Override
    public void create() {

	    setScreen(new EditorScreenSelection(this));
    }

}
