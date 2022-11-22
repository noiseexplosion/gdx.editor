package gdx.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.*;
import gdx.components.FirstPersonCameraController;
import gdx.components.GameModel;
import gdx.physics.BulletPhysicsSystem;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.lights.PointLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import utils.ModelUtils;
import utils.RuntimeLog;
import utils.SetupUtils;

import java.util.logging.Logger;

/**
 * Base screen which all other screens extend from. Minimizes the amount of boilerplate code needed to build a basic
 * scene.
 */
public class EditorRootScreen extends com.badlogic.gdx.ScreenAdapter
{
	private final Game game;
	public BulletPhysicsSystem physicsSystem;
	protected PerspectiveCamera camera;
	public FirstPersonCameraController fpsInputProcessor;
	//public FirstPersonCameraController cameraController;
	public SceneManager sceneManager;
	public static Array <GameModel> gameModels = new Array <>();
	public DirectionalShadowLight shadowLight;
	public DirectionalLightEx light;
	final float GRID_MIN = - 100f;
	final float GRID_MAX = 100f;
	final float GRID_STEP = 10f;
	protected boolean enableDebugDraw = true;
	protected boolean toggleLogging = true;
	public Logger log = RuntimeLog.addLogger(getClass());
	protected FitViewport viewport;
	public Stage stage;
	public HorizontalCollapsibleWidget sidebar;
	VisTable windowMenu;
	VisWindow window;
	PointLightEx pointLight;
	public VisTable table;
	public boolean showMenu = false;
	VisLabel fpsLabel;
	public boolean enableAxes;
	protected final Vector3 tmp = new Vector3();
	Sprite crosshair;
	SpriteBatch batch;
	public boolean enableCrosshair = true;
	public EditorRootScreen(Game game) {
		Bullet.init();
		this.game = game;
		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		physicsSystem = new BulletPhysicsSystem();
		sceneManager = SetupUtils.EnvironmentFactory.quickSceneManagerSetup(log, toggleLogging);
		Texture texture = new Texture("assets\\crosshairs\\Outline\\crosshair002.png");
		crosshair = new Sprite(texture);
		batch = new SpriteBatch();


		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fpsInputProcessor = new FirstPersonCameraController(camera);
		fpsInputProcessor.setVelocity(50f);
		fpsInputProcessor.setDegreesPerPixel(0.5f);
		Gdx.input.setCursorCatched(true);
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		//Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

		//light = new DirectionalLightEx();
		pointLight = new PointLightEx();
		pointLight.set(ModelUtils.getRandomColor(),camera.position, 100f);
		shadowLight = new DirectionalShadowLight(2048, 2048, 30f, 30f, 1f, 100f);
		SetupUtils.EnvironmentFactory.createEnv(camera, sceneManager, shadowLight, log, toggleLogging);

		sceneManager.environment.add((shadowLight).set(0.8f, 0.8f, 0.8f, -.4f, -.4f, -.4f));
		sceneManager.environment.shadowMap = shadowLight;


		SetupUtils.CubemapFactory cubemaps = new SetupUtils.CubemapFactory(shadowLight, log, toggleLogging);
		SetupUtils.EnvironmentFactory.applyLighting(sceneManager, cubemaps.getDiffuseCubemap(), cubemaps.getSpecularCubemap(), cubemaps.getEnvironmentCubemap(), shadowLight, log, toggleLogging);
		EditorRootScreen.gameModels = new Array <GameModel>();

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
		sceneManager.update(delta);
		sceneManager.render();
		enableCrosshair= Gdx.input.isCursorCatched();
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
		}
		else if (! enableDebugDraw) {
			for (GameModel model : gameModels) {
				Scene scene = new Scene(model.box);
				sceneManager.removeScene(scene);
			}
		}
	}
	public void processInput(){
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
			game.setScreen(new EditorScreenSelection(game));
			Gdx.input.setCursorCatched(false);
		}
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F1)) {
			enableDebugDraw = ! enableDebugDraw;
			drawBoundingBoxes();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
			showMenu = ! showMenu;


	}


			//cameraController.setVelocity(50f);
		}
		public void drawCrosshair(){
			if (!Gdx.input.isCursorCatched()) {
				enableCrosshair=false;
			}
		crosshair.setPosition(Gdx.graphics.getWidth() / 2 - crosshair.getWidth() / 2, Gdx.graphics.getHeight() / 2 - crosshair.getHeight() / 2);
			if (enableCrosshair) {
				batch.begin();
				crosshair.draw(batch);
				batch.end();
			}
		}





	public void createUI(){
		window = new VisWindow("Controls");
		window.setY(Gdx.graphics.getHeight()/2f);
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
					if (model.name.equals("Axes")&& enableAxes) {
						sceneManager.removeScene(model.scene);
					}
					else if (model.name.equals("Axes")&& ! enableAxes) {
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
		VisTextArea xy = new VisTextArea();
		VisTextArea xz = new VisTextArea();
		MultiSplitPane xPane = new MultiSplitPane(false);
		xPane.setWidgets(xx, xy, xz);
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
		/*table.add(x).pad(2f);
		table.row();
		table.add(y).pad(2f);
		table.row();
		table.add(z).pad(2f);
		table.row();*/



		window.setFillParent(false);
		window.add(table);
		window.setHeight(Gdx.graphics.getHeight()/2.5f);
		fpsLabel = new VisLabel("FPS: " + Gdx.graphics.getFramesPerSecond());
		fpsLabel.setPosition(20,20);
		stage.addActor(fpsLabel);
		stage.addActor(window);
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






