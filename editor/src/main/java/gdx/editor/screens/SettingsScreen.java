package gdx.editor.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import gdx.editor.EditorScreenSelection;

public class SettingsScreen extends EditorScreenSelection
{
	private final Stage stage;
	private final VisTable table;
	private final VisWindow window;
	Game game;

	public SettingsScreen(Game game)
	{
		super(game);
		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		table = new VisTable();
		table.pad(30);
		window = new VisWindow("Settings");
		window.setFillParent(true);
		VisCheckBox checkBox = new VisCheckBox("Show FPS");
		VisTextButton backButton = new VisTextButton("Back");
		VisCheckBox flag1 = new VisCheckBox("Flag 1");
		VisCheckBox flag2 = new VisCheckBox("Flag 2");
		VisCheckBox flag3 = new VisCheckBox("Flag 3");
		VisSlider fovSlider = new VisSlider(1, 180, 1, false);
		VisLabel fovLabel = new VisLabel("FOV: ");
		VisLabel fovValue = new VisLabel(Float.toString(fovSlider.getValue()));

		backButton.align(Align.bottom).pad(10f);
		backButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				game.setScreen(new EditorScreenSelection(game));
			}
		});
		checkBox.setChecked(true);
		table.add(checkBox).pad(10f);
		table.add(flag1).pad(10f);
		table.add(flag2).pad(10f);
		table.add(flag3).pad(10f);
		table.row();
		table.add(fovLabel).pad(5f);
		table.add(fovSlider).colspan(2).pad(5f).growX();
		table.add(fovValue).pad(5f);
		table.row();
		table.addSeparator().colspan(4).pad(10f);
		table.add(backButton).colspan(4).align(Align.bottom).pad(20f);
		window.add(table);
		stage.addActor(window);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta)
	{
		ScreenUtils.clear(Color.BLACK);
		stage.act(delta);
		stage.draw();
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(new EditorScreenSelection(game));
		}
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}
}
	/*Game game;
	VisWindow window;
	public SettingsScreen(Game game)
	{
		super(game);

		if (! VisUI.isLoaded())
			VisUI.load();
		this.game = game;

		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		VisTable settingsTable = new VisTable();
		VisCheckBox checkBox = new VisCheckBox("Test");
		settingsTable.add(checkBox);
		settingsTable.setFillParent(true);
		stage.addActor(settingsTable);
		Gdx.input.setInputProcessor(stage);
		window = new VisWindow("Window");

	}
	@Override
	public void render(float delta) {


		ScreenUtils.clear(Color.BLACK);
		stage.act();
		stage.draw();
	}
}*/
