package gdx.awt.bullet.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import gdx.awt.bullet.BulletPhysicsSystem;
import gdx.awt.bullet.CameraController;
import gdx.awt.utils.EnvironmentBuilder;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class BaseScreen extends ScreenAdapter {
    protected PerspectiveCamera camera;
    protected CameraController cameraController;
    protected ModelBatch modelBatch;
    protected ModelBatch shadowBatch;
    protected Array<ModelInstance> renderInstances;
    protected FirstPersonCameraController camController;
    protected Environment environment;
    protected DirectionalShadowLight shadowLight;
    protected EnvironmentBuilder environmentBuilder;
    protected BulletPhysicsSystem physicsSystem;
    public SceneManager sceneManager;
    public SceneAsset renderableAssets;
    public Scene gameScene;
    protected Game game;
    protected Stage stage;
    private final VisLabel fpsLabel;
    private VisCheckBox debugCheckBox;
    private boolean debug = false;

    private final Array<Color> colors;
    final float GRID_MIN = -100f;
    final float GRID_MAX = 100f;
    final float GRID_STEP = 10f;
    public BaseScreen (Game game) {
        this.game = game;
        physicsSystem = new BulletPhysicsSystem();
        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 1f;
        camera.far = 500;
        camera.position.set(0,10, 50f);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 2f));
        environment.add((shadowLight = new DirectionalShadowLight(2048, 2048, 30f, 30f, 1f, 100f)).set(0.8f, 0.8f, 0.8f, -.4f, -.4f, -.4f));
        environment.shadowMap = shadowLight;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        debugCheckBox = new VisCheckBox("Debug");
        fpsLabel = new VisLabel("FPS: ");
        fpsLabel.setPosition(10, 10);
        debugCheckBox.setChecked(debug);

        debugCheckBox.setPosition(50,10);
        stage.addActor(debugCheckBox);

        stage.addActor(fpsLabel);
        modelBatch = new ModelBatch();
        shadowBatch = new ModelBatch(new DepthShaderProvider());
        renderInstances = new Array<>();
        camController = new FirstPersonCameraController(camera);
        camController.setVelocity(50f);
        camController.setDegreesPerPixel(0.2f);
        Gdx.input.setInputProcessor(camController);
        colors = new Array<>();
        colors.add(Color.PURPLE);
        colors.add(Color.BLUE);
        colors.add(Color.TEAL);
        colors.add(Color.BROWN);
        colors.add(Color.FIREBRICK);
        renderableAssets = new SceneAsset();
        sceneManager = new SceneManager();
        //gameScene = new Scene(renderableAssets.scene);

        //load renderInstnces into sceneAssets
    }
    public void processUIInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
            debugCheckBox.setChecked(debug);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            debug = !debug;
            debugCheckBox.setChecked(debug);
        }
    }
    @Override
    public void render (float delta) {

        debugCheckBox.addListener(event -> {
            debug = debugCheckBox.isChecked();
            return false;
        });
        camController.update(delta);
        ScreenUtils.clear(Color.BLACK, true);
        shadowLight.begin();
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(renderInstances);
        shadowBatch.end();
        shadowLight.end();

        modelBatch.begin(camera);
        modelBatch.render(renderInstances, environment);
        modelBatch.end();



        stage.act();
        stage.draw();

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }
    void createAxes() {
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

        renderInstances.add(axesInstance);
    }
    protected void createFloor(float width, float height, float depth) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder = modelBuilder.part("floor", GL20.GL_TRIANGLES, VertexAttribute.Position().usage |VertexAttribute.Normal().usage | VertexAttribute.TexCoords(0).usage, new Material());

        BoxShapeBuilder.build(meshBuilder, width, height, depth);
        btBoxShape btBoxShape = new btBoxShape(new Vector3(width/2f, height/2f, depth/2f));
        Model floor = modelBuilder.end();

        ModelInstance floorInstance = new ModelInstance(floor);
        floorInstance.transform.trn(0, -0.5f, 0f);

        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(0, null, btBoxShape, Vector3.Zero);
        btRigidBody body = new btRigidBody(info);

        body.setWorldTransform(floorInstance.transform);

        renderInstances.add(floorInstance);
        physicsSystem.addBody(body);
    }
    protected Color getRandomColor(){
        return colors.get(MathUtils.random(0, colors.size-1));
    }
}
