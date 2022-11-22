package gdx.editor;

import com.badlogic.gdx.Game;
import gdx.editor.screens.EditorScreenSelection;

public class EditorGame extends Game
{
    public EditorScreenSelection screen;

    @Override
    public void create() {

	    setScreen(new EditorScreenSelection(this));
    }

}
