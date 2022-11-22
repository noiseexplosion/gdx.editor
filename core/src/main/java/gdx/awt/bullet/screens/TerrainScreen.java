package gdx.awt.bullet.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import gdx.awt.gltf.InitGameEnv;
import gdx.awt.gltf.player.Player;
import gdx.awt.gltf.player.PlayerController;
import gdx.awt.gltf.terrain.HeightMapTerrain;
import gdx.awt.gltf.terrain.Terrain;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class TerrainScreen extends BaseScreen implements AnimationController.AnimationListener
{
	private SceneManager sceneManager;
	private SceneAsset sceneAsset;
	private Scene playerScene;
	private static PerspectiveCamera camera;
	private Cubemap diffuseCubemap;
	private Cubemap environmentCubemap;
	private Cubemap specularCubemap;
	private Texture brdfLUT;
	private float time;
	private SceneSkybox skybox;
	private DirectionalLightEx light;
	private FirstPersonCameraController cameraController;
	private Environment environment;
	private PlayerController playerController;
	private Terrain terrain;
	private Scene terrainScene;
	public Player player;
	public Game game;

	public TerrainScreen(Game game) {
		super(game);
		this.game = game;

		sceneAsset = new GLTFLoader().load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\Character.gltf"));
		//sceneAsset = new Player().sceneAsset;
		brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));


		TerrainScreen.camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		light = new DirectionalLightEx();
		cameraController = new FirstPersonCameraController(TerrainScreen.camera);
		environment = new Environment();
		playerController = new PlayerController(TerrainScreen.camera);
		Gdx.input.setInputProcessor(playerController);
		Gdx.input.setCursorCatched(true);


		IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
		Cubemap environmentCubemap = iblBuilder.buildEnvMap(1024);
		Cubemap diffuseCubemap = iblBuilder.buildIrradianceMap(256);
		Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);
		iblBuilder.dispose();

		PBRShaderConfig config = new PBRShaderConfig();
		config.numBones = 128;
		DepthShaderProvider depthShaderProvider = new DepthShaderProvider();
		depthShaderProvider.config.numBones = 128;
		sceneManager = new SceneManager(new PBRShaderProvider(config), depthShaderProvider);
		playerScene = new Scene(sceneAsset.scene);
		sceneManager.addScene(playerScene);
		this.createTerrain();

		InitGameEnv.createEnv(environment, TerrainScreen.camera, cameraController, sceneManager, light);
		InitGameEnv.applyLighting(sceneManager,diffuseCubemap, specularCubemap, environmentCubemap, brdfLUT);

		skybox = new SceneSkybox(environmentCubemap);
		sceneManager.setSkyBox(skybox);

		playerScene.animationController.setAnimation("Idle", -1);
	}
	private void createTerrain(){
		if (terrain != null) {
			terrain.dispose();
			sceneManager.removeScene(terrainScene);
		}
		terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("heightmap.png")), 40f);
		terrainScene = new Scene(terrain.getModelInstance());
		terrainScene.modelInstance.transform.translate(0,0,0);
		sceneManager.addScene(terrainScene);
	}
	public static PerspectiveCamera getCameraObj(){
		return TerrainScreen.camera;
	}
	public static void setCamera(PerspectiveCamera camera){
		TerrainScreen.camera = camera;
	}

	@Override
	public void resize(int width, int height) {
		sceneManager.updateViewport(width, height);
	}

	@Override
	public void render(float delta) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		time += deltaTime;

		cameraController.update();
		playerController.processInput(playerScene, terrain, TerrainScreen.camera);
		playerController.updateCamera();
//		scene.modelInstance.transform.rotate(Vector3.Y, 10f * deltaTime);

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			playerScene.animationController.action("Jump", 1, 1f, this, 0.5f);

		// render
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		sceneManager.update(deltaTime);
		sceneManager.render();
	}

	@Override
	public void dispose() {
		sceneManager.dispose();
		sceneAsset.dispose();
		environmentCubemap.dispose();
		diffuseCubemap.dispose();
		specularCubemap.dispose();
		brdfLUT.dispose();
		skybox.dispose();
	}

	@Override
	public void onEnd(AnimationController.AnimationDesc animation) {

	}

	@Override
	public void onLoop(AnimationController.AnimationDesc animation) {

	}
}