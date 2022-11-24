package gdx.editor.screens;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.*;
import gdx.components.FirstPersonCameraController;
import gdx.components.GameModel;
import gdx.physics.BulletPhysicsSystem;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.animation.AnimationsPlayer;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.lights.PointLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import ui.CollapsibleTextWindow;
import utils.ModelUtils;
import utils.RuntimeLog;
import utils.SetupUtils;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.logging.Logger;

/**
 * Base screen which all other screens extend from. Minimizes the amount of boilerplate code needed to build a basic
 * scene.
 */
public class EditorRootScreen extends com.badlogic.gdx.ScreenAdapter {
    public static Array<GameModel> gameModels = new Array<>();
    protected final Vector3 tmp = new Vector3();
    final float GRID_MIN = -100f;
    final float GRID_MAX = 100f;
    final float GRID_STEP = 10f;
    private final Game game;
    public BulletPhysicsSystem physicsSystem;
    public FirstPersonCameraController fpsInputProcessor;
    //public FirstPersonCameraController cameraController;
    public SceneManager sceneManager;
    public DirectionalShadowLight shadowLight;
    public DirectionalLightEx light;
    public Logger log = RuntimeLog.addLogger(getClass());
    public Stage stage;
    public HorizontalCollapsibleWidget sidebar;
    public VisTable table;
    public boolean showMenu = false;
    public boolean enableAxes;
    public boolean enableCrosshair = true;
    public TextureAtlas atlas;
    protected PerspectiveCamera camera, gunCamera;
    protected boolean enableDebugDraw = true;
    protected boolean toggleLogging = true;
    protected FitViewport viewport;
    VisTable windowMenu;
    VisWindow window;
    PointLightEx pointLight;
    VisLabel fpsLabel;
    Sprite crosshair;
    SpriteBatch batch;
    GameModel gunModel;
    Scene gunModelScene;
    SceneAsset gunAsset;
    public ModelInstance gunInstance;
    Logger logger;
    CollapsibleTextWindow logWindow;
    CollapsibleTextWindow logWindow2;
    long startTimeMillis;
    float codeChangedTimer = -1f;
    AnimationsPlayer gunAnimationPlayer;


    public EditorRootScreen(Game game) {
        Bullet.init();
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        physicsSystem = new BulletPhysicsSystem();
        atlas = new TextureAtlas(Gdx.files.internal("assets/Prototype.atlas"));
        log.info("TextureAtlas: " + atlas.getRegions());
        sceneManager = SetupUtils.EnvironmentFactory.quickSceneManagerSetup(log, toggleLogging);
        Texture texture = new Texture("assets\\crosshairs\\Outline\\crosshair002.png");



        crosshair = new Sprite(texture);
        batch = new SpriteBatch();

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gunCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fpsInputProcessor = new FirstPersonCameraController(camera,gunInstance);
        fpsInputProcessor.setVelocity(50f);
        fpsInputProcessor.setDegreesPerPixel(0.5f);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        //Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        //light = new DirectionalLightEx();
        pointLight = new PointLightEx();
        pointLight.set(ModelUtils.getRandomColor(), camera.position, 100f);
        shadowLight = new DirectionalShadowLight(2048, 2048, 30f, 30f, 1f, 100f);
        SetupUtils.EnvironmentFactory.createEnv(camera, sceneManager, shadowLight, log, toggleLogging);
        Gdx.input.setInputProcessor(fpsInputProcessor);
        sceneManager.environment.add((shadowLight).set(0.8f, 0.8f, 0.8f, -.4f, -.4f, -.4f));
        sceneManager.environment.shadowMap = shadowLight;

        SetupUtils.CubemapFactory cubemaps = new SetupUtils.CubemapFactory(shadowLight, log, toggleLogging);
        SetupUtils.EnvironmentFactory.applyLighting(sceneManager, cubemaps.getDiffuseCubemap(), cubemaps.getSpecularCubemap(), cubemaps.getEnvironmentCubemap(), shadowLight, log, toggleLogging);
        EditorRootScreen.gameModels = new Array<GameModel>();



        updateModelArray();
        drawBoundingBoxes();
        createUI();

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.BLACK, true);
        physicsSystem.update(delta);
        processInput();
        fpsInputProcessor.update(delta);
        gunInstance.transform.mul(camera.projection);

        gunInstance.transform.set(camera.view).inv();
        gunInstance.transform.trn(.01f, -0f, 0);
        sceneManager.update(delta);
        sceneManager.render();
        enableCrosshair = Gdx.input.isCursorCatched();
        drawCrosshair();

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

        stage.act();
        stage.draw();
        window.setVisible(showMenu);

    }

    public void updateModelArray() {
        for (GameModel model : gameModels) {
            ModelInstance modelInstance = model.modelInstance;
            sceneManager.addScene(model.scene);
        }
    }

    public void drawBoundingBoxes() {
        if (enableDebugDraw) {
            for (GameModel model : gameModels) {
                Scene scene = new Scene(model.box);
                sceneManager.addScene(scene);
            }
        } else if (!enableDebugDraw) {
            for (GameModel model : gameModels) {
                Scene scene = new Scene(model.box);
                sceneManager.removeScene(scene);
            }
        }
    }

    public void processInput() {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.setScreen(new EditorScreenSelection(game));
            Gdx.input.setCursorCatched(false);
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F1)) {
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            showMenu = !showMenu;

        }

        //cameraController.setVelocity(50f);
    }

    public void drawCrosshair() {
        if (!Gdx.input.isCursorCatched()) {
            enableCrosshair = false;
        }
        crosshair.setPosition(Gdx.graphics.getWidth() / 2 - crosshair.getWidth() / 2, Gdx.graphics.getHeight() / 2 - crosshair.getHeight() / 2);
        if (enableCrosshair) {
            batch.begin();
            crosshair.draw(batch);
            batch.end();
        }
    }

    public void createUI() {
        window = new VisWindow("Controls");
        window.setY(Gdx.graphics.getHeight() / 2f);
        window.setX(Gdx.graphics.getWidth() - 285);
        windowMenu = new VisTable();
        windowMenu.debugAll();
        window.setWidth(275f);
        table = new VisTable();
        table.setWidth(250);
        table.align(Align.left);
        table.pack();

        table.pad(0);

        VisCheckBox fpsFlag = new VisCheckBox("Show FPS");
        fpsFlag.setChecked(true);
        fpsFlag.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fpsLabel.setVisible(fpsFlag.isChecked());
            }
        });

        VisTextButton backButton = new VisTextButton("Back");

        VisCheckBox axesFlag = new VisCheckBox("Enable Axes");
        axesFlag.setChecked(false);
        enableAxes = axesFlag.isChecked();
        axesFlag.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                enableAxes = axesFlag.isChecked();
                //iterate through gameModels and look for one with th name "Axes"
                for (GameModel model : gameModels) {
                    if (model.name.equals("Axes") && enableAxes) {
                        sceneManager.removeScene(model.scene);
                    } else if (model.name.equals("Axes") && !enableAxes) {
                        sceneManager.addScene(model.scene);
                    }
                }
            }
        });

        VisCheckBox debugDrawFlag = new VisCheckBox("Enable Debug Draw");
        VisCheckBox flag3 = new VisCheckBox("Flag 3");
        VisLabel fovValue = new VisLabel();

        VisSlider fovSlider = new VisSlider(1, 180, 1, false);
        fovSlider.setValue(67);
        fovSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                camera.fieldOfView = fovSlider.getValue();
                fovValue.setText(fovSlider.getValue() + "");

            }
        });
        VisLabel fovLabel = new VisLabel("FOV: ");
        VisTextArea xx = new VisTextArea();
        xx.setText("0");
        VisTextArea xy = new VisTextArea();
        xy.setText("0");
        VisTextArea xz = new VisTextArea();
        xz.setText("0");
        MultiSplitPane xPane = new MultiSplitPane(false);
        xPane.setWidgets(xx, xy, xz);
        //add a listener where the gun transforms are updated


        MultiSplitPane yPane = new MultiSplitPane(false);
        VisTextArea yx = new VisTextArea();
        VisTextArea yy = new VisTextArea();
        VisTextArea yz = new VisTextArea();
        yPane.setWidgets(yx, yy, yz);
        MultiSplitPane zPane = new MultiSplitPane(false);
        VisTextArea zx = new VisTextArea();
        VisTextArea zy = new VisTextArea();
        VisTextArea zz = new VisTextArea();
        zPane.setWidgets(zx, zy, zz);


        fpsFlag.setChecked(true);
        table.add(fpsFlag).pad(2f);
        table.add(axesFlag).pad(2f);
        table.row();
        table.add(debugDrawFlag).pad(2f);
        table.add(flag3).pad(2f);
        table.row();
        table.add(fovLabel).pad(2f);
        table.add(fovValue).pad(2f);
        table.row();

        table.add(fovSlider).colspan(2).pad(2f).growX();
        table.row();
        table.addSeparator().colspan(2).pad(5f);
        table.row();
        table.add(xPane).colspan(2).pad(2f).growX();
        table.row();
        table.add(yPane).colspan(2).pad(2f).growX();
        table.row();
        table.add(zPane).colspan(2).pad(2f).growX();
        table.row();
		window.setFillParent(false);
        window.add(table);
        window.setHeight(Gdx.graphics.getHeight() / 2.5f);
        fpsLabel = new VisLabel("FPS: " + Gdx.graphics.getFramesPerSecond());
        fpsLabel.setPosition(80, 210);
        stage.addActor(fpsLabel);
        stage.addActor(window);

        logger = Logger.getLogger("TEST");

        float w = Gdx.graphics.getWidth()/2f;
        float h = Gdx.graphics.getHeight();
        float hw = w * 0.5f;
        logWindow = new CollapsibleTextWindow("Log", 0, 0, w-25, 200f);

        logWindow.setX(25);
        logWindow.setY(5);
        stage.addActor(logWindow);
        logWindow2 = new CollapsibleTextWindow("Log2", 0, 0, w-25, 200f);
        logWindow2.setX(w+10);
        logWindow2.setY(5);
        stage.addActor(logWindow2);
        VisWindow sliderWindow = new VisWindow("Slider");
        VisSlider xSlider = new VisSlider(0, 100, 1, false);
        VisSlider ySlider = new VisSlider(0, 100, 1, false);
        VisSlider zSlider = new VisSlider(0, 100, 1, false);
        float x,y,z;
        sliderWindow.add(xSlider);
        sliderWindow.add(ySlider);
        sliderWindow.add(zSlider);
        sliderWindow.pack();
        //action listener for the sliders
        xSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
               float x = xSlider.getValue();
               float y = ySlider.getValue();
                float z = zSlider.getValue();
                //update the gun transforms
                gunInstance.transform.trn(x,y,z);             }
        });
        ySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                float x = xSlider.getValue();
                float y = ySlider.getValue();
                float z = zSlider.getValue();
                //update the gun transforms
                gunInstance.transform.trn(x,y,z);            }
        });
        zSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                float x = xSlider.getValue();
                float y = ySlider.getValue();
                float z = zSlider.getValue();
                //update the gun transforms
                gunInstance.transform.trn(x,y,z);             }
        });


        window.setVisible(false);

    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        physicsSystem.dispose();
        stage.dispose();
        batch.dispose();
    }

}






