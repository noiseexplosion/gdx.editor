package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ConeShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CylinderShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import gdx.editor.EditorRootScreen;
import gdx.physics.BulletPhysicsSystem;
import gdx.awt.bullet.screens.BasicCollisionDetection;
import gdx.components.GameModel;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utilities for creating models and working with physics bodies.
 */

public class ModelUtils {
    static Logger log = Logger.getLogger(ModelUtils.class.getName());
    public static final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.PURPLE, Color.ORANGE, Color.BROWN, Color.PINK, Color.LIME, Color.TEAL, Color.NAVY, Color.MAROON, Color.OLIVE, Color.GRAY, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.WHITE, Color.BLACK};


    /**
     * Retrieve the list of animations from a model.
     * @param gameModel the model to retrieve the animations from
     */
    public static List<String> getAnimationNames(ModelInstance gameModel) {
        List<String> animationNames = new ArrayList<>();
        for (Animation animation : gameModel.animations) {
            animationNames.add(animation.id);
        }
        if (animationNames.isEmpty()) {
            animationNames.add("No animations");
        }
        return animationNames;
    }

    /**
     * Creates axes to be used as a visual aid for debugging.
     * Uses default parameters.
     *
     * @return Renderable axes model instance.
     */
    public static ModelInstance createAxes() {
        final float GRID_MIN = -100f;
        final float GRID_MAX = 100f;
        final float GRID_STEP = 10f;
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

        return axesInstance;
    }

    /**
     * Creates axes as a {@link ModelInstance} from user-provided parametersl.
     *
     * @param min Minimum grid value, or extant of grid.
     * @param max Maximum grid value, or extant of grid.
     * @param step How many units between each grid line.
     * @return Model instance.
     */

    public static ModelInstance createAxes(float min, float max, float step) {
        final float GRID_MIN = min;
        final float GRID_MAX = max;
        final float GRID_STEP = step;
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

        return axesInstance;
    }

    /**
     * Creates a graphical representation of a bounding box for debugging collision detection. Will not automatically update position.
     * @param gameModel the model to create the bounding box for
     * @return a model instance of the bounding box
     */

    public static ModelInstance createBoundingBoxRenderable(GameModel gameModel) {
    BoundingBox boundingBox = new BoundingBox();
    boundingBox = gameModel.modelInstance.calculateBoundingBox(boundingBox);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("BoundingBox", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.RED);
        BoxShapeBuilder.build(builder, boundingBox);
        Model boundingBoxModel = modelBuilder.end();

        return new ModelInstance(boundingBoxModel);
    }

    public static ModelInstance createBoundingBoxRenderable(BoundingBox boundingBox) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("BoundingBox", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
        builder.setColor(Color.RED);
        BoxShapeBuilder.build(builder, boundingBox);
        Model boundingBoxModel = modelBuilder.end();

        return new ModelInstance(boundingBoxModel);
    }

    /**
     * Creates a {@link ModelInstance} which represents a basic floor object.
     * @param width the width of the floor
     * @param height the length of the floor
     * @param depth the depth of the floor
     * @return a {@link ModelInstance} of the floor
     */


    public static ModelInstance createFloor(int width, int height, int depth) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        Material material = loadTextureIntoMaterial("C:\\dev\\libGDX\\gdx.awt\\assets\\images\\Grid1024.png");
        material.set(ColorAttribute.createDiffuse(Color.BLUE));
        MeshPartBuilder meshBuilder = modelBuilder.part("floor", GL20.GL_TRIANGLES, VertexAttribute.Position().usage |VertexAttribute.Normal().usage | VertexAttribute.TexCoords(0).usage, material);

        BoxShapeBuilder.build(meshBuilder, width, height, depth);
        Model floor = modelBuilder.end();

        ModelInstance floorInstance = new ModelInstance(floor);
        floorInstance.transform.trn(0, -0.5f, 0f);

        return floorInstance;
    }

    /**
     * Utility method to create a model instance and associate it with a collision shape. Must seperatly add to the world.
     * @param gameModels Array we use for tracking our game objects and their associated collision shapes.
     */
    public static GameModel createRandomModel(Array<GameModel> gameModels){
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        Material material = loadTextureIntoMaterial("C:\\dev\\libGDX\\gdx.awt\\assets\\images\\Grid1024.png");
        material.set(ColorAttribute.createDiffuse(getRandomColor()));
        MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        GameModel gameModel;
        btCollisionShape shape;

            int random = MathUtils.random(1, 4);
            switch (random) {
                case 1:
                    BoxShapeBuilder.build(builder, 0, 0, 0, 1f, 1f, 1f);
                    shape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));
                    break;
                case 2:
                    ConeShapeBuilder.build(builder, 1, 1, 1, 8);
                    shape = new btConeShape(0.5f, 1f);
                    break;
                case 3:
                    SphereShapeBuilder.build(builder, 1, 1, 1, 8, 8);
                    shape = new btSphereShape(0.5f);
                    break;
                case 4:
                default:
                    CylinderShapeBuilder.build(builder, 1, 1, 1, 8);
                    shape = new btCylinderShape(new Vector3(0.5f, 0.5f, 0.5f));
                    break;
            }
            Model model = modelBuilder.end();
            ModelInstance modelInstance = new ModelInstance(model);


            modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(ModelUtils.getRandomColor()), ColorAttribute.createSpecular(ModelUtils.getRandomColor()));
            modelInstance.transform.setToTranslation(MathUtils.random(-10, 10), MathUtils.random(10, 20), MathUtils.random(-10, 10));
            gameModel = new GameModel(model, shape);
            gameModels.add(gameModel);

        return gameModel;
    }

    /**
     *
     * Creates a model instance, {@link GameModel}, and collision shape and keeps track of it all; it will create X amount of these random objects and add encapsulate each of them into a {@link GameModel} array for iterative access
     *
     * @param number The number of models & physics objects to create
     */

    public static void createRandomModel(Array<GameModel> gameModels, int number,BulletPhysicsSystem physicsSystem)
    {
        log.info("creating game models; "+gameModels.size+" to begin");
        for(int i = 0; i < number; i++){
            GameModel model = ModelUtils.createRandomModel(gameModels);
            replaceTexture(model.modelInstance, "C:\\dev\\libGDX\\gdx.awt\\assets\\images\\Grid8.png");
            model.modelInstance.transform.translate(MathUtils.random(-50, 50), MathUtils.random(10, 20), MathUtils.random(-50, 50));
            ModelUtils.loadIntoPhysicsWorld(model.modelInstance, physicsSystem, model.shape);

        }
        ModelUtils.log.info(number+" "+" random models have been created and initialized into physics objects; access them within Array gameModels"+"\n"+
                "gameModels size at completion: "+ gameModels.size);
    }

    /**
     * Loads a model instance and an associated collision shape into the bullet environment.
     * @param modelInstance
     * @param physicsSystem
     * @param collisionShape
     */

    public static void loadIntoPhysicsWorld(ModelInstance modelInstance, BulletPhysicsSystem physicsSystem,btCollisionShape collisionShape)
    {
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(0, null, collisionShape, Vector3.Zero);
        btRigidBody body = new btRigidBody(info);
        body.setWorldTransform(modelInstance.transform);
        physicsSystem.addBody(body);
    }

    /**
     * Loads a texture into a material and returns it.
     * @param filepath
     * @return A material with a texture attribute
     */

    public static Material loadTextureIntoMaterial(String filepath){
        Texture texture= new Texture(Gdx.files.internal(filepath));
        //Set uv scaling
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        PBRTextureAttribute pbrTextureAttribute = new PBRTextureAttribute(PBRTextureAttribute.BaseColorTexture,texture);
        pbrTextureAttribute.scaleU=10f;
        pbrTextureAttribute.scaleV=10f;

        return new Material(pbrTextureAttribute);
    }

    /**
    *Creates a renderable line out of a raycast for debugging and object picking. Use {@link Camera#getPickRay(float, float)} to create a ray.
     */

    public static ModelInstance createRayModelInstance(Vector3 origin, Vector3 direction, float length, Color color){
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        Material material = new Material(ColorAttribute.createDiffuse(color));
        MeshPartBuilder builder = modelBuilder.part("ray", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        builder.setColor(color);
        builder.line(origin, direction.scl(length).add(origin));
        Model model = modelBuilder.end();
        return new ModelInstance(model);
    }
    /**
     * Returns a random color.
     */
    public static Color getRandomColor()
    {
        Array <Color> colors = new Array <>();
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.GOLD);
        colors.add(Color.FIREBRICK);
        int r = MathUtils.random(0,255);
        int g = MathUtils.random(0,255);
        int b = MathUtils.random(0,255);
        //return a random color
        return new Color(r,g,b,1f);
    }

    /**
     * Removes and replaces a models texture with a new one.
     */
    public static void replaceTexture(ModelInstance modelInstance, String filepath) {
        Texture texture = new Texture(Gdx.files.internal(filepath));
        //Set uv scaling
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        PBRTextureAttribute pbrTextureAttribute = new PBRTextureAttribute(PBRTextureAttribute.BaseColorTexture, texture);
        pbrTextureAttribute.scaleU = 40f;
        pbrTextureAttribute.scaleV = 40f;
        Material material = new Material(pbrTextureAttribute);
        modelInstance.materials.set(0, material);
    }
        public static void replaceTexture(Color color, ModelInstance modelInstance){
        Material material = new Material(ColorAttribute.createDiffuse(color));
        modelInstance.materials.set(0, material);


}}

