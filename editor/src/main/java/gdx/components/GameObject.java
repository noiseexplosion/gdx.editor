package gdx.components;

import com.badlogic.gdx.math.Vector3;

public abstract class GameObject {
    //game objects will have a name, a position, a rotation, and a scale
    public String name;
    public float x;
    public float y;
    public float z;
    public float rotationX;
    public float rotationY;
    public float rotationZ;
    public float scaleX;
    public float scaleY;

    public String getName() {
        return name;
    }
    public abstract Vector3 getPosition();


    public abstract void rotateX(float degrees);
    public abstract void rotateY(float degrees);
    public abstract void rotateZ(float degrees);
    public abstract void setPosition(Vector3 position);
    public abstract void translate(Vector3 translation);

}
