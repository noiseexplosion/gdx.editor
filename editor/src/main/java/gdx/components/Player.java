package gdx.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import utils.ModelUtils;

import java.util.Arrays;
import java.util.logging.Logger;

public class Player extends GameModel {
    public Scene playerScene;
    public SceneAsset sceneAsset;
    public ModelInstance modelInstance;
    static
    {
        SceneAsset sceneAsset = new SceneAsset();
        GLTFLoader loader = new GLTFLoader();
        sceneAsset = loader.load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\Character.gltf"));

    }



    /**
     * Uses default model; loads the player into a scene and adds it to scene manager.
     *
     * @param printLog whether to print log messages
     */

    public Player(SceneManager sceneManager, Logger log, boolean printLog) {
        super("Player");

        sceneAsset = new GLTFLoader().load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\Character.gltf"));
        playerScene = new Scene(sceneAsset.scene);
        sceneManager.addScene(playerScene);
        modelInstance = new ModelInstance(playerScene.modelInstance);
        if (printLog) {
            log.info
                    ("Player model loaded from file; using default character model. Initialized into scene manager as renderable." + "\n" +
                            "Animation count: " + sceneAsset.animations.size + "\n" +
                            "Animation names: " + ModelUtils.getAnimationNames(modelInstance));
        }
    }

    /**
     * Uses custom model; loads the player into a scene and adds it to scene manager.
     *
     * @param printLog whether to print log messages
     * @param path     path to model file
     */


    public Player(SceneManager sceneManager, Logger log, boolean printLog, String path) {
        super("Player");
        sceneAsset = new GLTFLoader().load(Gdx.files.internal(path));
        playerScene = new Scene(sceneAsset.scene);
        sceneManager.addScene(playerScene);
        modelInstance = new ModelInstance(playerScene.modelInstance);
        if (printLog) {
            log.info
                    ("Player model loaded from file using the following path; " + path + "\n" +
                            "Initialized into scene manager as renderable." + "\n" +
                            "Animation count: " + sceneAsset.animations.size + "\n" +
                            "Animation names: " + ModelUtils.getAnimationNames(modelInstance));
        }
    }


    public Scene getAsScene(GameModel gameModel) {
        return playerScene;
    }


    public SceneAsset getSceneAsset() {
        return sceneAsset;
    }



}

