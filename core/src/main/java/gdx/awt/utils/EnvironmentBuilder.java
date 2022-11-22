package gdx.awt.utils;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;

public class EnvironmentBuilder {
    DirectionalShadowLight shadowLight;

    public EnvironmentBuilder(Environment environment, PerspectiveCamera camera) {
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add((shadowLight = new DirectionalShadowLight(2048, 2048, 30f, 30f, 1f, 100f)).set(0.8f, 0.8f, 0.8f, -.4f, -.4f, -.4f));
        environment.shadowMap = shadowLight;
        camera.position.set(0f, 10f, 50f);
        camera.near=1f;
        camera.far=500f;

    }
}
