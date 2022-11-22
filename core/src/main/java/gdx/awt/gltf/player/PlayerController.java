package gdx.awt.gltf.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import gdx.awt.gltf.Settings;
import gdx.awt.gltf.terrain.enums.CameraMode;
import gdx.awt.gltf.terrain.Terrain;
import gdx.awt.gltf.GameCoreTest;
import net.mgsx.gltf.scene3d.scene.Scene;

public class PlayerController implements InputProcessor, AnimationController.AnimationListener {
    private final Vector3 moveTranslation = new Vector3();
    private final Vector3 currentPosition = new Vector3();
    private Matrix4 playerTransform = new Matrix4();
    float speed = 5f;
    float rotationSpeed = 80f;
    private final PerspectiveCamera camera;

    private CameraMode cameraMode =CameraMode.FREE_LOOK;
    private float camPitch = Settings.CAMERA_START_PITCH;
    private float distanceFromPlayer = 10f;
    private float angleAroundPlayer = 0f;
    private float angleBehindPlayer = 0f;

    public PlayerController(PerspectiveCamera camera) {
        this.camera = camera;
    }


    public void processInput(Scene playerScene, Terrain terrain, PerspectiveCamera camera) {
        playerTransform.set(playerScene.modelInstance.transform);
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveTranslation.z += speed * Gdx.graphics.getDeltaTime();
            playerScene.animationController.setAnimation("Run", -1);
        } else if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerScene.animationController.action("Idle", 1, 1f, this, 0.5f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveTranslation.z -= speed * Gdx.graphics.getDeltaTime();
            playerScene.animationController.setAnimation("RunBackwards", -1);
        } else if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerScene.animationController.action("Idle", 1, 1f, this, 0.5f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerTransform.rotate(Vector3.Y, rotationSpeed*Gdx.graphics.getDeltaTime());
            angleBehindPlayer += rotationSpeed*Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            playerTransform.rotate(Vector3.Y, -rotationSpeed*Gdx.graphics.getDeltaTime());
            angleBehindPlayer -= rotationSpeed*Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            playerScene.animationController.setAnimation("Jump", -1, 1f, this);
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            switch (cameraMode){
                case FREE_LOOK:
                    cameraMode = CameraMode.BEHIND_PLAYER;
                    angleAroundPlayer = angleBehindPlayer;
                    break;
                case BEHIND_PLAYER:
                    cameraMode = CameraMode.FREE_LOOK;
                    break;
            }
        }
        playerTransform.translate(moveTranslation);


        camera.update();
        playerScene.modelInstance.transform.set(playerTransform);
        playerScene.modelInstance.transform.getTranslation(currentPosition);
        float height = terrain.getHeightAttribute(currentPosition.x, currentPosition.z);
        currentPosition.y = height;
        playerScene.modelInstance.transform.setTranslation(currentPosition);

        moveTranslation.setZero();
    }
    public void updateCamera() {
        float horDistance = this.calculateHorizontalDistance(distanceFromPlayer);
        float vertDistance = this.calculateVerticalDistance(distanceFromPlayer);
        this.calculatePitch();
        this.calculateAngleAroundPlayer();
        this.calculateCameraPosition(currentPosition, horDistance, vertDistance);
        camera.up.set(Vector3.Y);
        camera.lookAt(currentPosition);
        GameCoreTest.setCamera(camera);
        camera.update();
    }

    private void calculateCameraPosition(Vector3 currentPosition, float horDistance, float vertDistance) {
        float offsetX = (float)(horDistance*Math.sin(Math.toRadians(angleAroundPlayer)));
        float offsetZ = (float)(horDistance*Math.cos(Math.toRadians(angleAroundPlayer)));
        camera.position.x = currentPosition.x - offsetX;
        camera.position.z = currentPosition.z - offsetZ;
        camera.position.y = currentPosition.y + vertDistance;
    }

    private float calculateHorizontalDistance(float distanceFromPlayer){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(camPitch)));
    }
    private float calculateVerticalDistance(float distanceFromPlayer){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(camPitch)));
    }
    private void calculatePitch(){
        float pitchChange = Gdx.input.getDeltaY() * Settings.CAMERA_PITCH_FACTOR;
        camPitch -= pitchChange;

        if(camPitch<Settings.CAMERA_MIN_PITCH)
            camPitch = Settings.CAMERA_MIN_PITCH;
        else if (camPitch>Settings.CAMERA_MAX_PITCH)
            camPitch = Settings.CAMERA_MAX_PITCH;
    }
    private void calculateAngleAroundPlayer(){
        if (cameraMode==CameraMode.FREE_LOOK){
            float angleChange = Gdx.input.getDeltaX() * Settings.CAMERA_ANGLE_AROUND_PLAYER_FACTOR;
            angleAroundPlayer -= angleChange;
        } else {
            angleAroundPlayer = angleBehindPlayer;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;}

    @Override
    public boolean keyUp(int keycode) {
        return false;}

    @Override
    public boolean keyTyped(char character) {
        return false;}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;}

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;}

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;}

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float zoomLevel = amountY * Settings.CAMERA_ZOOM_LEVEL_FACTOR;
        if(distanceFromPlayer<Settings.CAMERA_MIN_DISTANCE_FROM_PLAYER)
            distanceFromPlayer = Settings.CAMERA_MIN_DISTANCE_FROM_PLAYER;
        else if (distanceFromPlayer>Settings.CAMERA_MAX_DISTANCE_FROM_PLAYER)
            distanceFromPlayer = Settings.CAMERA_MAX_DISTANCE_FROM_PLAYER;
        else
            distanceFromPlayer -= zoomLevel;
        return false;
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}
