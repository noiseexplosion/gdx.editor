package gdx.editor.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.*;
import gdx.editor.EditorRootScreen;

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
