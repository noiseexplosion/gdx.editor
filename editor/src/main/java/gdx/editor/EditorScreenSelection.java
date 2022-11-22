package gdx.editor;

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
import gdx.editor.screens.ScreenThree;
import gdx.editor.screens.ScreenOne;
import gdx.editor.screens.ScreenTwo;
import gdx.editor.screens.SettingsScreen;

import java.util.logging.Logger;

public class EditorScreenSelection extends ScreenAdapter {
    final Game game;
    private final Stage stage;
    private final VisSelectBox<EditorScreenSelection.ScreenEnum> screenSelect;
    private VisWindow window;
    private VisTextButton exitButton;
    private VisTextButton settingsButton;
    private VisSplitPane splitPane;
    ButtonBar buttonBar;
    private Logger log = Logger.getLogger(EditorScreenSelection.class.getName());

    enum ScreenEnum {
        Empty,
        FirstScreen,
        SecondScreen,
        ThirdScreen
    }

    public EditorScreenSelection(Game game) {
        if (!VisUI.isLoaded())
            VisUI.load();
        log.info("VisUI loaded");

        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        VisTable selectTable = new VisTable();
        VisTable buttonTable = new VisTable();



        selectTable.pad(30);

        exitButton = new VisTextButton("Exit");
        exitButton.setWidth(100f);
        exitButton.align(Align.left);



        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        settingsButton = new VisTextButton("Settings");
        settingsButton.setWidth(100f);
        settingsButton.align(Align.right);

        settingsButton.setWidth(100f);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SettingsScreen(game));
            }
        });


        buttonTable.setHeight(100);
        buttonTable.setWidth(250);
        buttonTable.add(exitButton).pad(10).align(Align.left);

        buttonTable.add(settingsButton).pad(10).align(Align.right);
        buttonTable.scaleBy(1.25f);



        screenSelect = new VisSelectBox<>();
        screenSelect.setItems(EditorScreenSelection.ScreenEnum.values());
        screenSelect.setSelected(ScreenEnum.Empty);
        screenSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (screenSelect.getSelected()) {
                    case FirstScreen:
                        game.setScreen(new ScreenOne(game));
                        log.info("FirstScreen selected");
                        break;
                    case SecondScreen:
                        //game.setScreen(new RigidBodyPhysics(game));
                        log.info("SecondScreen selected");
                        game.setScreen(new ScreenTwo(game));
                        break;
                    case ThirdScreen:
                        //game.setScreen(new TerrainScreen(game));
                        log.info("ThirdScreen selected");
                        game.setScreen(new ScreenThree(game));
                        break;

                }
            }
        });

        selectTable.add(new VisLabel("Screen select: ")).colspan(2);
        selectTable.row().pad(10, 0, 10, 0);
        selectTable.add(screenSelect).colspan(2).fillX().expandX();
        selectTable.row();





        selectTable.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        //buttonTable.setPosition(stage.getWidth() / 2, stage.getHeight() / 3);
        buttonTable.setWidth(250);

        buttonTable.align(Align.center);
        selectTable.row();
        selectTable.add(exitButton).pad(10);

        selectTable.add(settingsButton).pad(10);
        //selectTable.add(buttonTable);
        window = new VisWindow("Pause");
        window.setFillParent(true);
        window.add(selectTable);

        stage.addActor(window);




        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        ScreenUtils.clear(Color.BLACK);
        stage.act();
        stage.draw();
        if (Gdx.input.isCursorCatched()) {
            Gdx.input.setCursorCatched(false);
        }
    }
}
