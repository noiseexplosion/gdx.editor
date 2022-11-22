package gdx.awt.gltf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import com.esotericsoftware.reflectasm.FieldAccess;
import gdx.awt.gltf.terrain.enums.CameraMode;
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

import java.util.Arrays;

public class GameCore extends ApplicationAdapter implements AnimationController.AnimationListener, InputProcessor
{
	private SceneManager sceneManager;
	private SceneAsset sceneAsset;
	private Scene playerScene;
	private PerspectiveCamera camera;
	private Cubemap diffuseCubemap;
	private Cubemap environmentCubemap;
	private Cubemap specularCubemap;
	private Texture brdfLUT;
	private float time;
	private SceneSkybox skybox;
	private DirectionalLightEx light;
	private FirstPersonCameraController cameraController;
	private Environment environment;
	private SceneAsset gameMap;
	private Scene gameMapScene;

	private final Vector3 moveTranslation = new Vector3();
	private final Vector3 currentPosition = new Vector3();
	private Matrix4 playerTransform = new Matrix4();
	float speed = 5f;
	float rotationSpeed = 80f;

	//camera
	private CameraMode cameraMode =CameraMode.FREE_LOOK;
	private float camPitch = Settings.CAMERA_START_PITCH;
	private float distanceFromPlayer = 10f;
	private float angleAroundPlayer = 0f;
	private float angleBehindPlayer = 0f;

	//terrain
	private Terrain terrain;
	private Scene terrainScene;
	public Logger logger = new Logger("GameCore", Logger.DEBUG);

	@Override
	public void create() {
		sceneAsset = new GLTFLoader().load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\Character.gltf"));
		brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));
		camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		light = new DirectionalLightEx();
		cameraController = new FirstPersonCameraController(camera);
		environment = new Environment();
		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(this);

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


		//player
		playerScene = new Scene(sceneAsset.scene);
		sceneManager.addScene(playerScene);

		InitGameEnv.createEnv(environment, camera, cameraController, sceneManager, light);
		InitGameEnv.applyLighting(sceneManager,diffuseCubemap, specularCubemap, environmentCubemap, brdfLUT);

		this.createTerrain();
		skybox = new SceneSkybox(environmentCubemap);
		sceneManager.setSkyBox(skybox);
		//playerScene.animationController.setAnimation("Idle", -1);
		FieldAccess access = FieldAccess.get(getClass());
		access.getFieldNames();
		//print all fields
		for (String fieldName : access.getFieldNames()) {
			System.out.println(fieldName);
		}
		logger.info(Arrays.toString(access.getFieldNames()));
		logger.info("GameCore created");
	}

	@Override
	public void resize(int width, int height) {
		sceneManager.updateViewport(width, height);
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		time += deltaTime;
		this.updateCamera();

		//cameraController.update();
//		scene.modelInstance.transform.rotate(Vector3.Y, 10f * deltaTime);
		this.processInput();
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			playerScene.animationController.setAnimation("Jump", -1, 1f, this);

		// render
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		sceneManager.update(deltaTime);
		sceneManager.render();
	}

	private void updateCamera() {
		float horDistance = this.calculateHorizontalDistance(distanceFromPlayer);
		float vertDistance = this.calculateVerticalDistance(distanceFromPlayer);
		this.calculatePitch();
		this.calculateAngleAroundPlayer();
		this.calculateCameraPosition(currentPosition, horDistance, vertDistance);
		camera.up.set(Vector3.Y);
		camera.lookAt(currentPosition);
		camera.update();
	}

	private void calculateCameraPosition(Vector3 currentPosition, float horDistance, float vertDistance) {
		float offsetX = (float)(horDistance*Math.sin(Math.toRadians(angleAroundPlayer)));
		float offsetZ = (float)(horDistance*Math.cos(Math.toRadians(angleAroundPlayer)));
		camera.position.x = currentPosition.x - offsetX;
		camera.position.z = currentPosition.z - offsetZ;
		camera.position.y = currentPosition.y + vertDistance;
	}

	private float calculateHorizontalDistance(float distanceFromPlayer){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(camPitch)));
	}
	private float calculateVerticalDistance(float distanceFromPlayer){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(camPitch)));
	}
	private void calculatePitch(){
		float pitchChange = Gdx.input.getDeltaY() * Settings.CAMERA_PITCH_FACTOR;
		camPitch -= pitchChange;

		if(camPitch<Settings.CAMERA_MIN_PITCH)
			camPitch = Settings.CAMERA_MIN_PITCH;
		else if (camPitch>Settings.CAMERA_MAX_PITCH)
		camPitch = Settings.CAMERA_MAX_PITCH;
	}
	private void calculateAngleAroundPlayer(){
		if (cameraMode==CameraMode.FREE_LOOK){
			float angleChange = Gdx.input.getDeltaX() * Settings.CAMERA_ANGLE_AROUND_PLAYER_FACTOR;
			angleAroundPlayer -= angleChange;
		} else {
			angleAroundPlayer = angleBehindPlayer;
		}
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
		playerScene.animationController.setAnimation("Idle", -1);

	}

	@Override
	public void onLoop(AnimationController.AnimationDesc animation) {

	}
	private void processInput() {
		playerTransform.set(playerScene.modelInstance.transform);
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			moveTranslation.z += speed * Gdx.graphics.getDeltaTime();
			playerScene.animationController.setAnimation("Run", -1);
		} else if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			playerScene.animationController.action("Idle", 1, 1f, this, 0.5f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			moveTranslation.z -= speed * Gdx.graphics.getDeltaTime();
			playerScene.animationController.setAnimation("RunBackwards", -1);
		} else if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			playerScene.animationController.action("Idle", 1, 1f, this, 0.5f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
		playerTransform.rotate(Vector3.Y, rotationSpeed*Gdx.graphics.getDeltaTime());
		angleBehindPlayer += rotationSpeed*Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {

			playerTransform.rotate(Vector3.Y, -rotationSpeed*Gdx.graphics.getDeltaTime());
			angleBehindPlayer -= rotationSpeed*Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			playerScene.animationController.setAnimation("Jump", -1, 1f, this);
		}
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
			switch (cameraMode){
				case FREE_LOOK:
					cameraMode = CameraMode.BEHIND_PLAYER;
					angleAroundPlayer = angleBehindPlayer;
					break;
				case BEHIND_PLAYER:
					cameraMode = CameraMode.FREE_LOOK;
					break;
			}
		}
		playerTransform.translate(moveTranslation);


		camera.update();
		playerScene.modelInstance.transform.set(playerTransform);
		playerScene.modelInstance.transform.getTranslation(currentPosition);
		float height = terrain.getHeightAttribute(currentPosition.x, currentPosition.z);
		currentPosition.y = height;
		playerScene.modelInstance.transform.setTranslation(currentPosition);

		moveTranslation.setZero();
	}

	private void createTerrain(){
		if (terrain != null) {
			terrain.dispose();
			sceneManager.removeScene(terrainScene);
		}
		terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("C:\\dev\\libGDX\\gdx.game\\assets\\heightmap.png")), 20f);
		terrainScene = new Scene(terrain.getModelInstance());
		terrainScene.modelInstance.transform.translate(0,0,0);
		sceneManager.addScene(terrainScene);
	}

	private void terrainCollisionCheck(){


	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		float zoomLevel = amountY * Settings.CAMERA_ZOOM_LEVEL_FACTOR;
		if(distanceFromPlayer<Settings.CAMERA_MIN_DISTANCE_FROM_PLAYER)
			distanceFromPlayer = Settings.CAMERA_MIN_DISTANCE_FROM_PLAYER;
		else if (distanceFromPlayer>Settings.CAMERA_MAX_DISTANCE_FROM_PLAYER)
			distanceFromPlayer = Settings.CAMERA_MAX_DISTANCE_FROM_PLAYER;
		else
			distanceFromPlayer -= zoomLevel;
		return false;
	}
}