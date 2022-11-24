package gdx.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import gdx.editor.screens.EditorRootScreen;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneModel;
import utils.RuntimeLog;
import utils.SceneUtils;
import utils.SetupUtils;

import java.util.logging.Logger;

public class GunRenderer
{
    Scene gunScene;
    Model gunModel;
    SceneAsset gunAsset;
    ModelInstance gunInstance;
    PerspectiveCamera gunCamera;
    SceneManager sceneManager;
    Environment environment;
    FirstPersonCameraController controller;
    Matrix4 gunTransform = new Matrix4();
    Logger log = RuntimeLog.addLogger(GunRenderer.class);
    public GunRenderer()
    {
        GLTFLoader loader = new GLTFLoader();
        sceneManager = SetupUtils.EnvironmentFactory.quickSceneManagerSetup(log,true);
        gunAsset = loader.load(Gdx.files.internal("assets/models/FPS/pistol.gltf"));
        gunScene = new Scene(gunAsset.scene);
        gunModel = gunAsset.scene.model;
        gunInstance = new ModelInstance(gunModel);
        gunInstance.transform.translate(0, 0, 1);


        gunCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gunCamera.far= 100f;
        gunCamera.position.set(0, 0, 0);
        gunCamera.lookAt(0, 0, 1);
        sceneManager.setCamera(gunCamera);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));

        sceneManager.addScene(gunScene);
    }

    public void render(float delta)
    {
        //Clear depth buffer
        Gdx.gl.glClearDepthf(1f);
        gunCamera.update();
        sceneManager.update(delta);
        sceneManager.render();
    }

    public void dispose()
    {
        gunModel.dispose();
    }

    public void updateCameraView(PerspectiveCamera cam){
        gunTransform.set(gunInstance.transform);

    }



}

