package gdx.awt.gltf.player;

import com.badlogic.gdx.Gdx;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Player {
    private Scene playerScene;
    public SceneAsset playerAsset = new GLTFLoader().load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\Character.gltf"));

    public Player(SceneManager sceneManager) {
        playerScene = new Scene(playerAsset.scene);
        sceneManager.addScene(playerScene);

    }


    public Scene getPlayerScene() {
        return playerScene;
    }



    public SceneAsset getPlayerAsset() {
        return playerAsset;
    }


}

