package gdx.editor.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import gdx.awt.gltf.terrain.HeightMapTerrain;
import gdx.awt.gltf.terrain.Terrain;
import gdx.components.GameModel;
import gdx.components.ThirdPersonPlayerController;
import gdx.editor.EditorRootScreen;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class ScreenOne extends EditorRootScreen implements AnimationController.AnimationListener {

    GameModel gunModel;
    Scene gunModelScene;
    Scene mageModelScene;


    Scene playerModelScene;
    GameModel mageModel;
    //ModelInstance mageModelInstance;

    GameModel playerModel;
    Terrain terrain;
    Scene terrainScene;

    GameModel houseModel;
    Scene houseScene;

    ThirdPersonPlayerController controller;

    public ScreenOne(Game game) {
        super(game);


        SceneAsset gunAsset = new GLTFLoader().load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\FPS\\Pistol.gltf"));
        SceneAsset mageAsset = new GLTFLoader().load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\Mage\\mage.gltf"));
        SceneAsset playerAsset = new GLTFLoader().load(Gdx.files.internal("C:\\Users\\Jamie\\Desktop\\Character\\Character.gltf"));
        SceneAsset houseAsset = new GLTFLoader().load(Gdx.files.internal("C:\\dev\\Assets\\.gltf\\corbusier\\corbusier.gltf"));
        controller=new ThirdPersonPlayerController(camera);
        Gdx.input.setInputProcessor(controller);


        mageModelScene = new Scene(mageAsset.scene);
        gunModelScene = new Scene(gunAsset.scene);
        playerModelScene = new Scene(playerAsset.scene);
        houseScene = new Scene(houseAsset.scene);


        mageModel = new GameModel("mage", mageModelScene, mageAsset.scene.model,true);
        //sceneManager.addScene(mageModel.scene);
        gameModels.add(mageModel);

        gunModel = new GameModel("fiveseven", gunModelScene, gunAsset.scene.model,true);
        //sceneManager.addScene(gunModel.scene);
        gameModels.add(gunModel);

        playerModel = new GameModel("Player", playerModelScene, playerAsset.scene.model,true);
        sceneManager.addScene(playerModel.scene);
        gameModels.add(playerModel);



        mageModel.translate(new Vector3(10, 0, 0));
        gunModel.translate(new Vector3(0, 0, 10));
        playerModel.translate(new Vector3(5, 0, 5));

        ModelInstance box = playerModel.box;
        //ModelInstance box2 = mageModel.box;





        playerModelScene.animationController.setAnimation("Idle", -1, 1f, this);
        gunModelScene.animationController.setAnimation("Idle", -1, 1f, this);



        terrain = new HeightMapTerrain(new Pixmap(Gdx.files.internal("C:\\dev\\libGDX\\gdx.awt\\assets\\heightmap.png")),20f);
        terrainScene = new Scene(terrain.getModelInstance());
        GameModel terrainModel = new GameModel("Terrain",terrainScene,terrain.getModelInstance().model, true);
        gameModels.add(terrainModel);




    }

    @Override
    public void render(float delta) {
        super.render(delta);
        controller.updateCamera();
        controller.processInput(playerModel.scene,terrain,camera);

        EditorRootScreen.gameModels.forEach(GameModel::syncBoundingBox);

    }
    @Override
    public void dispose() {
        super.dispose();


        sceneManager.dispose();
        for  (GameModel model:gameModels){
            model.dispose();
        }
    }


    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

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
    //method to sync position of hands and camera
   /* public void syncWorldPositions(float delta){
        playerTransform.set(gunModelScene.modelInstance.transform);
        Vector3 cameraOffset = new Vector3(0,0,0);
        cameraOffset.x = camera.position.x+1;
        cameraOffset.y = camera.position.y-1;
        cameraOffset.z = camera.position.z;

        gunModelScene.modelInstance.transform.setTranslation(cameraOffset);
        fpsCamera.update(delta);
        gunModelScene.modelInstance.transform.set(playerTransform);
        gunModelScene.modelInstance.transform.getTranslation(camera.position);
        gunModelScene.modelInstance.transform.setTranslation(camera.position);

        //get the transform of the camera
        //get the transform of the hand
        //set the transform of the hand to the camera transform
        //set the transform of the camera to the hand transform
        playerTransform.set(camera.projection);
        gunModelScene.modelInstance.transform.set(playerTransform);


        //make the FPS hands rotate with the camera



    }*/
}

