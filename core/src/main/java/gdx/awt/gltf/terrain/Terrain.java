package gdx.awt.gltf.terrain;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;

public abstract class Terrain implements Disposable {
    protected int size;
    protected int width;
    protected float heightMagnitude;
    protected ModelInstance modelInstance;

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    abstract public float getHeightAttribute(float worldX, float worldZ);

    abstract public void setHeightMagnitude(float magnitude);
}
