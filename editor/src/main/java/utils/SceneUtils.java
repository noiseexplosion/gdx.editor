package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import gdx.components.GameModel;
import gdx.editor.screens.EditorRootScreen;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading creating scenes and tracking them as game objects
 */
public class SceneUtils {
    /**
     * Utility method to load a GLTF file directly into a scene and pass it into the gameModel array for serialization
     * @param name the name of the game model
     * @param path the path to the GLTF file
     * @param sceneManager root scene manager
     * @param gameModels List of renderable game assets/scenes we plan on rendering; usually the {@link EditorRootScreen#gameModels} array
     */
    public static void addSceneFromPath(String name, String path, SceneManager sceneManager, Array<GameModel> gameModels) {
        SceneAsset sceneAsset = new GLTFLoader().load(Gdx.files.internal(path));
        Scene scene = new Scene(sceneAsset.scene);
        sceneManager.addScene(scene);
        GameModel gameModel = new GameModel(name, scene, sceneAsset.scene.model, true);
        gameModels.add(gameModel);
    }

    /**
     * Retrieves a list of the names of all the game objects in the game model array. Necessary for serialization and generic accessing of game objects
     * @param gameModels The array of {@link GameModel} objects we want to get the names of; usually the {@link EditorRootScreen#gameModels} array
     * @return A list of the names of all the game objects in the game model array
     */

    public static List<String> getSceneNames(Array<GameModel> gameModels) {
        List<String> names = new ArrayList<>();
        for (GameModel gameModel : gameModels) {
            names.add(gameModel.name);
        }
        return names;
    }
}
