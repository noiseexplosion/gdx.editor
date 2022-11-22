package gdx.editor;

import by.fxg.gdxpsx.postprocessing.PSXPostProcessing;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

import gdx.awt.gltf.InitGameEnv;
import gdx.awt.gltf.Settings;
import gdx.awt.gltf.terrain.enums.CameraMode;
import gdx.awt.gltf.terrain.HeightMapTerrain;
import gdx.awt.gltf.terrain.Terrain;
import gdx.physics.BulletPhysicsSystem;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class EditorApplicationAdapter extends ApplicationAdapter implements AnimationController.AnimationListener {
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene playerScene;
    public PerspectiveCamera camera;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private SceneSkybox skybox;
    public DirectionalLightEx light;
    private FirstPersonCameraController cameraController;
    private Environment environment;
    private SceneAsset gameMap;
    private Scene gameMapScene;
    private Scene axesScene;
    public BulletPhysicsSystem physicsSystem;
    protected Array<ModelInstance> renderInstances;



    public boolean axesFlag = true;
    public boolean skyboxFlag = true;
    public boolean debugDrawFlag = true;
    public boolean psxFlag = false;

    final float GRID_MIN = -1000f;
    final float GRID_MAX = 1000f;
    final float GRID_STEP = 5f;

    private final Vector3 moveTranslation = new Vector3();
    private final Vector3 currentPosition = new Vector3();
    private Matrix4 playerTransform = new Matrix4();
    float speed = 5f;
    float rotationSpeed = 80f;
    btHeightfieldTerrainShape terrainShape;
    PSXPostProcessing postProcessing;
    //camera
    private CameraMode cameraMode = CameraMode.FREE_LOOK;
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
        Bullet.init();
        postProcessing = new PSXPostProcessing(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        postProcessing.setDownscalingIntensity(4f);
        postProcessing.setColorDepth(32f, 32f, 32f);
        postProcessing.setDitheringMatrix(PSXPostProcessing.DitherMatrix.Dither2x2);
        physicsSystem = new BulletPhysicsSystem();
        renderInstances = new Array<>();
        //sceneAsset = new Player().sceneAsset;
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));
        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        light = new DirectionalLightEx();
        cameraController = new FirstPersonCameraController(camera);
        environment = new Environment();
        //Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(cameraController);
        cameraController.setVelocity(30f);

        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        Cubemap environmentCubemap = iblBuilder.buildEnvMap(1024);
        Cubemap diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        PBRShaderConfig config = new PBRShaderConfig();
        config.numBones = 340;
        DepthShaderProvider depthShaderProvider = new DepthShaderProvider();
        depthShaderProvider.config.numBones = 340;

        sceneManager = new SceneManager(new PBRShaderProvider(config), depthShaderProvider);
        //initiate terrain so we can edit it later
	    this.createTerrain();


        //player
        //playerScene = new Scene(sceneAsset.scene);
        //sceneManager.addScene(playerScene);

        InitGameEnv.createEnv(environment, camera, cameraController, sceneManager, light);
        InitGameEnv.applyLighting(sceneManager, diffuseCubemap, specularCubemap, environmentCubemap, brdfLUT);


	    this.createAxes();
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        //print all fields

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
        physicsSystem.update(deltaTime);
        cameraController.update();
        sceneManager.update(deltaTime);




        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if (psxFlag) {
            postProcessing.capture();
            sceneManager.render();
            postProcessing.endCapture();
            postProcessing.drawFrame();
        }
        else {
            sceneManager.render();
        }




        if(debugDrawFlag) {
            physicsSystem.render(camera);
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
        terrain.dispose();
        physicsSystem.dispose();

    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {
        playerScene.animationController.setAnimation("Idle", -1);

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }

    private void createTerrain() {
        if (terrain != null) {
            terrain.dispose();
            sceneManager.removeScene(terrainScene);
        }
        terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("C:\\dev\\libGDX\\gdx.game\\assets\\heightmap.png")), 20f);
        terrainScene = new Scene(terrain.getModelInstance());
        terrainScene.modelInstance.transform.translate(0, 0, 0);

    }
    public void createTerrain(float[] data,int magnitude) {
        if (terrain != null) {

            sceneManager.removeScene(terrainScene);
        }
        terrain = new HeightMapTerrain(data,magnitude);

        terrainScene.modelInstance = terrain.getModelInstance();



        terrainScene.modelInstance.transform.translate(0, 0, 0);
        sceneManager.addScene(terrainScene);
    }
    public void setTerrainMagnitude(float magnitude) {
        terrain.setHeightMagnitude(magnitude);
    }

    public void createAxes() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.LIGHT_GRAY);
        for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
            builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
            builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
        }
        builder = modelBuilder.part("axes", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.RED);
        builder.line(0, .1f, 0, 100, 0, 0);
        builder.setColor(Color.GREEN);
        builder.line(0, .1f, 0, 0, 100, 0);
        builder.setColor(Color.BLUE);
        builder.line(0, .1f, 0, 0, 0, 100);
        Model axesModel = modelBuilder.end();
        ModelInstance axesInstance = new ModelInstance(axesModel);

        axesScene = new Scene(axesInstance);
        sceneManager.addScene(axesScene);
        renderInstances.add(axesInstance);
    }

    public void createModel(String path) {
        SceneAsset sceneAsset = new GLTFLoader().load(Gdx.files.internal(path));
        Scene scene = new Scene(sceneAsset.scene);
        sceneManager.addScene(scene);
        renderInstances.add(scene.modelInstance);
    }
    public void toggleAxes() {
        if (axesFlag) {

            sceneManager.removeScene(axesScene);
            axesScene = null;

            axesFlag = false;

        } else {
	        this.createAxes();
            axesFlag = true;
        }

    }
    public void toggleSkybox() {
        if (skyboxFlag) {
            sceneManager.setSkyBox(null);
            skyboxFlag = false;
        } else {
            sceneManager.setSkyBox(skybox);
            skyboxFlag = true;
        }
    }
    public void setLightColor(int r, int g, int b) {
        Color color = new Color(r,g,b,1);
        sceneManager.environment.remove(light);

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(color, -1f, -0.8f, -0.2f));
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

    }
}