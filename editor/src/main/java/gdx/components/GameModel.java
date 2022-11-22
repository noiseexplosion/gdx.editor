package gdx.components;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;
import gdx.physics.BulletPhysicsSystem;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneModel;
import utils.ModelUtils;
import utils.RuntimeLog;

import java.util.logging.Logger;

public class GameModel extends GameObject implements Disposable {
    //public Logger log = Logger.getLogger(GameModel.class.getName());
    public Logger log = RuntimeLog.addLogger(GameModel.class);
    public ModelInstance modelInstance;
    public BoundingBox objBoundingBox;
    public ModelInstance box;
    public String path;
    public Scene scene;
    protected btCollisionObject body;
    public btCollisionShape shape;
    public Matrix4 transform;


    public GameModel(String player) { }

    public GameModel(Model model, btCollisionShape shape) {
        this.name = "Bullet Physics Object";
        this.shape = shape;
        modelInstance = new ModelInstance(model);
        scene = new Scene(modelInstance);
        body = new btCollisionObject();
        body.setCollisionShape(shape);
        objBoundingBox = new BoundingBox();
        modelInstance.calculateBoundingBox(objBoundingBox);
        box = createBoundingBox();
        body.setWorldTransform(modelInstance.transform);
        transform=modelInstance.transform;
        setScene(scene);
    }

    public GameModel(String name,Scene scene, Model model,boolean createBox){
        modelInstance = new ModelInstance(model);
        this.scene = new Scene(modelInstance);
        this.name = name;
        objBoundingBox = new BoundingBox();
        objBoundingBox = modelInstance.calculateBoundingBox(objBoundingBox);
        box = createBoundingBox();
        transform=modelInstance.transform;
        setScene(scene);

        log.info("Name: " + name +"\n"+
                "Height: " + objBoundingBox.getHeight()+"\n"+
                "Width: " + objBoundingBox.getWidth()+"\n"+
                "Depth: " + objBoundingBox.getDepth()+"\n"+
                "Center: " + objBoundingBox.getCenter(new Vector3())+"\n"+
                "Min: " + objBoundingBox.getMin(new Vector3())+"\n"+
                "Max: " + objBoundingBox.getMax(new Vector3())+"\n");
    }


    @Override
    public Vector3 getPosition() {
        return scene.modelInstance.transform.getTranslation(new Vector3());
    }

    @Override
    public void rotateX(float degrees) {
        scene.modelInstance.transform.rotate(Vector3.X, degrees);
    }

    @Override
    public void rotateY(float degrees) {
        scene.modelInstance.transform.rotate(Vector3.Y, degrees);
    }

    @Override
    public void rotateZ(float degrees) {
        scene.modelInstance.transform.rotate(Vector3.Z, degrees);
    }

    public Matrix4 getTransform() {
        return transform;
    }

    @Override
    public void setPosition(Vector3 position) {
        scene.modelInstance.transform.setTranslation(position);
    }

    @Override
    public void translate(Vector3 translation) {
        scene.modelInstance.transform.translate(translation);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }


    @Override
    public void dispose() {
        modelInstance.model.dispose();
        body.dispose();

    }

    public ModelInstance createBoundingBox() {
        objBoundingBox= modelInstance.calculateBoundingBox(new BoundingBox());
        box = ModelUtils.createBoundingBoxRenderable(this);
        return box;
    }

    public void syncBoundingBox() {
        //sync the positions in space of the bounding box
        box.transform.set(transform);
    }



}
