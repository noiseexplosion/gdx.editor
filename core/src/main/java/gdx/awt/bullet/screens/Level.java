package gdx.awt.bullet.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Level extends BaseScreen {
    ModelInstance gameModel;
    btCollisionShape collisionShape;
    btRigidBody rigidBody;
    Scene level;
    SceneAsset levelAsset;
    public Level(Game game) {
        super(game);
        levelAsset = new GLTFLoader().load(Gdx.files.internal("C:\\dev\\Assets\\Level\\Dungeon 2.gltf"));
        gameModel = new ModelInstance(levelAsset.scene.model);
        renderInstances.add(gameModel);
    }
}
