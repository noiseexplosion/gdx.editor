package gdx.editor.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMeshShape;
import com.badlogic.gdx.utils.Array;
import gdx.components.GameModel;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import utils.ModelUtils;

public class ScreenTwo extends EditorRootScreen
{
    Vector3 rayFrom = new Vector3();
    Vector3 rayTo = new Vector3();
    Scene rayScene;



    public ScreenTwo(Game game) {
        super(game);
        ModelUtils.createRandomModel(gameModels, 20, physicsSystem);
        SceneAsset houseAsset = new GLTFLoader().load(Gdx.files.internal("assets\\models\\corbusier\\corbusier.gltf"));

        Array <MeshPart> meshParts = new Array<>();
        meshParts =houseAsset.scene.model.meshParts;
        btTriangleMeshShape meshShape = new btBvhTriangleMeshShape(meshParts);
        GameModel houseModel = new GameModel(houseAsset.scene.model, meshShape);
        sceneManager.addScene(houseModel.scene);


        Gdx.input.setInputProcessor(fpsInputProcessor);


        ModelInstance floor = ModelUtils.createFloor(100, 1, 100);
        Scene floorScene = new Scene(floor);
        GameModel floorModel = new GameModel("Floor", floorScene, floor.model, true);

        gameModels.add(floorModel);
        sceneManager.addScene(floorScene);

        ModelInstance axes = ModelUtils.createAxes();
        Scene axesScene = new Scene(axes);
        GameModel axesModel = new GameModel("Axes",axesScene,axes.model,true);

        gameModels.add(axesModel);
        //sceneManager.addScene(axesScene);
        updateModelArray();
        drawBoundingBoxes();
        createRayCast();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        EditorRootScreen.gameModels.forEach(GameModel::syncBoundingBox);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sceneManager.removeScene(rayScene);
            Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            rayFrom.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            rayTo.set(Gdx.input.getX(), Gdx.input.getY(), 1);
            camera.unproject(rayFrom);
            camera.unproject(rayTo);
            Vector3 direction = rayTo.cpy().sub(rayFrom).nor();
            float distance = 100;
            rayTo = rayFrom.cpy().add(direction.cpy().scl(distance));
            ModelInstance model = ModelUtils.createRayModelInstance(ray.origin, ray.direction, distance, Color.RED);
            if (model != null) {
                model.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
            }

            Ray ray2 = createRayCast();


            rayScene = new Scene(model);
            sceneManager.addScene(rayScene);
            checkRayCastCollision();
            checkRayCastCollision(ray2);
        }
    }
    @Override
    public void processInput() {
        super.processInput();
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            Gdx.input.setCursorCatched(!showMenu);
            if(showMenu){
            Gdx.input.setInputProcessor(stage); }
            else {
                Gdx.input.setInputProcessor(fpsInputProcessor);
            }
    }}

    public Ray createRayCast() {
        rayFrom.set(camera.position);
        rayTo.set(camera.direction).scl(100).add(rayFrom);
        ModelInstance rayLine = ModelUtils.createRayModelInstance(rayFrom, rayTo, 100f, Color.MAGENTA);
        rayScene = new Scene(rayLine);
        GameModel rayModel = new GameModel("Ray", rayScene, rayLine.model, true);
        gameModels.add(rayModel);
        return new Ray(rayFrom, rayTo);
    }

    public void checkRayCastCollision() {
        Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        for(GameModel gameModel : gameModels) {
            if(Intersector.intersectRayBoundsFast(ray,gameModel.objBoundingBox)&&gameModel.name!="Ray") {
                log.info("Ray hit: " + gameModel.name);
                gameModel.scene.modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));
            }
        }

    }
    public void checkRayCastCollision(Ray ray) {
        for(GameModel gameModel : gameModels) {
            if(Intersector.intersectRayBoundsFast(ray,gameModel.objBoundingBox)){
                log.info("Ray hit: " + gameModel.name);
                gameModel.scene.modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));

            }
        }

    }
    @Override
    public void dispose() {
        super.dispose();

    }

}

