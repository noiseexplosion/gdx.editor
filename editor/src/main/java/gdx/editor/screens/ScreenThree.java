package gdx.editor.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.*;

public class ScreenThree extends EditorRootScreen
{
	Stage stage;
	VisTable rootTable;
	HorizontalCollapsibleWidget sidebar;
	VisTable sidebarTable;
	VisTable fboRenderTable;
	VisTable sidebarContainer;
	VisWindow window;
	VisTextButton collapseButton;
	MenuBar menuBar;
	Game game;
	public ScreenThree(Game game)
	{
		super(game);
		this.game = game;


	};


















	@Override
	public void render(float delta)
	{
		ScreenUtils.clear(Color.BLACK);
		stage.act(delta);
		stage.draw();
	}
}
