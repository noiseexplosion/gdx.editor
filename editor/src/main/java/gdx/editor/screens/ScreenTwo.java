package gdx.editor.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMeshShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import gdx.components.FirstPersonCameraController;
import gdx.components.GameModel;
import gdx.components.GunRenderer;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.animation.AnimationsPlayer;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import utils.ModelUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenTwo extends EditorRootScreen
{
    Vector3 rayFrom = new Vector3();
    Vector3 rayTo = new Vector3();
    Scene rayScene;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    GunRenderer gunRenderer;
    AnimationController animationController;



    public ScreenTwo(Game game) {
        super(game);
        ModelUtils.createRandomModel(gameModels, 20, physicsSystem,atlas);
        SceneAsset houseAsset = new GLTFLoader().load(Gdx.files.internal("assets\\models\\corbusier\\corbusier.gltf"));

        Array <MeshPart> meshParts = new Array<>();
        meshParts =houseAsset.scene.model.meshParts;
        btTriangleMeshShape meshShape = new btBvhTriangleMeshShape(meshParts);
        /*GameModel houseModel = new GameModel(houseAsset.scene.model, meshShape);
        sceneManager.addScene(houseModel.scene);*/



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

        gunAsset = new GLTFLoader().load(Gdx.files.internal("assets/rigged ak74/RIG_AK74v2.gltf"));
        gunModelScene = new Scene(gunAsset.scene);
        gunInstance = gunModelScene.modelInstance;
        gunInstance.transform.translate(0,0,0);

        gunRenderer = new GunRenderer(
        );
        sceneManager.addScene(gunModelScene);

        gunAnimationPlayer = new AnimationsPlayer(gunModelScene);
        animationController = new AnimationController(gunInstance);
        animationController.setAnimation("IdleSway", -1);
        animationController.animate("IdleSway", -1, 1f, null, 0.2f);
        //set camera position and gun transform to be in front of the camera
        camera.position.set(0, 0, 0);
        camera.lookAt(0, 0, 0);
        camera.update();
        //gunInstance.transform.set(camera.combined);
        gunInstance.transform.translate(0,0,-1);
        gunInstance.transform.setToWorld(new Vector3(0,1,0),camera.up,camera.direction);







        sceneManager.addScene(axesScene);
        updateModelArray();
        drawBoundingBoxes();
        createRayCast();
        //create an input prompt on another thread

    }

    @Override
    public void render(float delta) {

        super.render(delta);
        EditorRootScreen.gameModels.forEach(GameModel::syncBoundingBox);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sceneManager.removeScene(rayScene);
            Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
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

            //Ray ray2 = createRayCast();


            rayScene = new Scene(model);
            sceneManager.addScene(rayScene);
            //checkRayCastCollision();
            //checkRayCastCollision(ray2);
        }
        //flip the gun model 180 degrees to face the camera
        //gunInstance.transform.rotate(Vector3.X, 180);
        animationController.update(delta);
        gunInstance.transform.mul(camera.projection);

        gunInstance.transform.set(camera.view).inv();
        gunInstance.transform.trn(0, -1f, 0);
        //prevent the gun from moving



        //move the gun down to be in front of the camera



        //gunInstance.transform.set(camera.view).mul(camera.projection).inv();
        //gunInstance.transform.setToLookAt(camera.position, camera.up);
        //offset the gun to the right and forward

        //set a hotkey to log and print the transformation info of both the camera and the gun


            //format the text to be more readable
            StringBuilder sb = new StringBuilder();
            logWindow2.setText("Camera: "+ "\n"+ camera.position + "\n " + camera.direction + " \n" + camera.up);
            logWindow.setText("Gun: " + "\n" + gunInstance.transform);








    }
    @Override
    public void processInput() {
        super.processInput();

          Gdx.input.setCursorCatched(!showMenu);
            if(showMenu){
            Gdx.input.setInputProcessor(stage); }
            else {
                Gdx.input.setInputProcessor(fpsInputProcessor);
            }





            /*Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

            Vector3 direction = ray.direction.cpy().nor();
            float distance = 100;
            rayTo = rayFrom.cpy().add(direction.cpy().scl(distance));*/
//            ModelBuilder modelBuilder = new ModelBuilder();
//            Material mat = ModelUtils.getRandomTextureFromAtlas(atlas,20f);


//            ModelInstance model = new ModelInstance(modelBuilder.createArrow(ray.origin, rayTo,mat, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked));
//            log.info("material: " + model.materials.get(0).toString());
//        sceneManager.addScene(new Scene(model));
//            public void drawRayLine(Ray ray) {
            /*ModelInstance rayInstance;

            if (rayScene != null) {
                sceneManager.removeScene(rayScene);
            }

            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
            builder.setColor(Color.LIGHT_GRAY);

            builder = modelBuilder.part("axes", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked, new Material());
            builder.setColor(Color.RED);
            //builder.line(ray.origin, rayTo);
            builder.setColor(Color.GREEN);
           // builder.line(ray.origin,ray.direction);
            builder.setColor(Color.BLUE);
        builder.line(ray.origin,camera.position.cpy().add(rayTo).scl(100f));
        builder.setColor(Color.RED);
        builder.line(camera.position,camera.position.cpy().add(rayFrom).scl(100f));
            builder.setColor(Color.WHITE);
            //builder.line(ray.origin,camera.direction);
            builder.line(camera.direction.add(-5,0,0),rayTo);
        builder.line(camera.direction.add(5,0,0),rayTo);
        builder.line(camera.direction.add(0,0,5),rayTo);
        builder.line(camera.direction.add(0,0,-5),rayTo);
            Model rayModel = modelBuilder.end();
            rayInstance = new ModelInstance(rayModel);
            rayScene = new Scene(rayInstance);
            sceneManager.addScene(rayScene);*/
        }






    public Ray createRayCast() {
        rayFrom.set(camera.position);
        rayTo.set(camera.direction).scl(100).add(rayFrom);
        ModelInstance rayLine = ModelUtils.createRayModelInstance(rayFrom, rayTo, 100f, Color.MAGENTA);
        rayScene = new Scene(rayLine);
        /*GameModel rayModel = new GameModel("Ray", rayScene, rayLine.model, true);
        gameModels.add(rayModel);*/
        return new Ray(rayFrom, rayTo);
    }

    public void checkRayCastCollision() {
        Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        for(GameModel gameModel : gameModels) {
            if(Intersector.intersectRayBoundsFast(ray,gameModel.objBoundingBox)) {
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

    public void createInputPrompt() {
        //create a popup jframe with three number input fields
        JFrame frame = new JFrame("Input Dialog");
        String[] labels = {"X: ", "Y: ", "Z: "};
        int numPairs = labels.length;
        JPanel panel = new JPanel(new GridLayout(numPairs+1, 2+1));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            panel.add(l);
            JTextField textField = new JTextField(10);
            l.setLabelFor(textField);
            panel.add(textField);
        }
        //add a button to submit
        JButton submit = new JButton("Submit");
        panel.add(submit);

        //add an action listener that sets the camera position to the input values
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the text from the text fields

                String x = ((JTextField) panel.getComponent(1)).getText();
                String y = ((JTextField) panel.getComponent(3)).getText();
                String z = ((JTextField) panel.getComponent(5)).getText();
                //set the camera position to the input values
                //camera.position.set(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
                gunInstance.transform.trn(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
                //dispose of the frame

            }










        });
        //
        frame.add(panel);
        frame.setSize(150,150);
        frame.setVisible(true);

    }


}

