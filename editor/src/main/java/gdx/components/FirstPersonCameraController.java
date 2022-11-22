package gdx.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntIntMap;

import static gdx.editor.screens.EditorRootScreen.gameModels;

public class FirstPersonCameraController extends CameraController
{
	private final Camera camera;
	private final IntIntMap keys = new IntIntMap();
	private int STRAFE_LEFT = Input.Keys.A;
	private int STRAFE_RIGHT = Input.Keys.D;
	private int FORWARD = Input.Keys.W;
	private int BACKWARD = Input.Keys.S;
	private int UP = Input.Keys.Q;
	private int DOWN = Input.Keys.E;
	private float velocity = 5;
	private float degreesPerPixel = 0.5f;
	private final Vector3 tmp = new Vector3();
	private final Vector3 rayFrom = new Vector3();
	private final Vector3 rayTo = new Vector3();




	public FirstPersonCameraController(Camera camera)
	{
		this.camera = camera;
	}
	@Override
	public boolean keyDown(int keycode) {
		keys.put(keycode, keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys.remove(keycode, 0);
		return true;
	}

	public void setVelocity(float velocity) {
	this.velocity = velocity;
	}

	public void setDegreesPerPixel(float degreesPerPixel) {
		this.degreesPerPixel = degreesPerPixel;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
		float deltaY = - Gdx.input.getDeltaY() * degreesPerPixel;
		camera.direction.rotate(camera.up, deltaX);
		tmp.set(camera.direction).crs(camera.up).nor();
		camera.direction.rotate(tmp, deltaY);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
		float deltaY = - Gdx.input.getDeltaY() * degreesPerPixel;
		camera.direction.rotate(camera.up, deltaX);
		tmp.set(camera.direction).crs(camera.up).nor();
		camera.direction.rotate(tmp, deltaY);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		if (keys.containsKey(FORWARD)) {
			tmp.set(camera.direction).nor().scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(BACKWARD)) {
			tmp.set(camera.direction).nor().scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(STRAFE_LEFT)) {
			tmp.set(camera.direction).crs(camera.up).nor().scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(STRAFE_RIGHT)) {
			tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(UP)) {
			tmp.set(camera.up).nor().scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(DOWN)) {
			tmp.set(camera.up).nor().scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		camera.update(true);
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Ray ray = camera.getPickRay(screenX, screenY);
		rayFrom.set(ray.origin);
		rayTo.set(ray.direction).scl(50f).add(rayFrom);
		System.out.println(screenX + " " + screenY);
		for(GameModel gameModel : gameModels) {
			if(Intersector.intersectRayBoundsFast(ray, gameModel.objBoundingBox)){
				System.out.println("Intersected with " + gameModel.name);

				gameModel.scene.modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));

			}

	}
		return false;
	}}


