package gdx.awt.gltf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class InitGameEnv {
    public Environment environment;
    public PerspectiveCamera camera;
    public FirstPersonCameraController cameraController;
    public static DirectionalLightEx light;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private SceneManager sceneManager;
    public InitGameEnv(SceneManager sceneManager, Texture brdfLUT) {

    }

    public PerspectiveCamera getCamera() {
        return camera;
    }
    public Environment getEnvironment() {
        return environment;
    }
    public FirstPersonCameraController getCameraController() {
        return cameraController;
    }
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public static void createEnv(Environment environment, PerspectiveCamera camera, FirstPersonCameraController cameraController, SceneManager sceneManager, DirectionalLightEx light) {
        float d = .02f;
        camera.near =.1f;
        camera.far = 5000;
        sceneManager.setCamera(camera);
        camera.position.set(0,0.5f, 4f);
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);
    }
    public static void applyLighting(SceneManager sceneManager, Cubemap diffuseCubemap, Cubemap environmentCubemap, Cubemap specularCubemap, Texture brdfLUT) {
        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));
    }


}
