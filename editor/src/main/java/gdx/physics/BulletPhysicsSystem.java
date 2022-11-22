package gdx.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;
import utils.RuntimeLog;

import java.util.logging.Logger;

public class BulletPhysicsSystem implements Disposable {
    private final Logger log = RuntimeLog.addLogger(getClass());
    private final btDynamicsWorld dynamicsWorld;
    private final btDispatcher dispatcher;
    private final btCollisionConfiguration collisionConfig;
    private final btBroadphaseInterface broadphase;
    private final btConstraintSolver constraintSolver;
    private final DebugDrawer debugDrawer;
    private final float fixedTimeStep = 1f / 60f;
    private final Vector3 gravity = new Vector3(0, - 9.81f, 0);
    private final Vector3 lastRayFrom = new Vector3();
    private final Vector3 lastRayTo = new Vector3();
    private final Vector3 rayColor = new Vector3(1, 0, 1);
    public BulletPhysicsSystem() {
        Bullet.init();
        log.info("Bullet initialized.");
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);

        dynamicsWorld.setDebugDrawer(debugDrawer);
    }
    public void update(float delta) {
        dynamicsWorld.stepSimulation(delta,1,fixedTimeStep);
    }
    public void addBody(btRigidBody body) {
        dynamicsWorld.addRigidBody(body);
    }
    public void render(Camera camera) {
        debugDrawer.begin(camera);
        debugDrawer.drawLine(lastRayFrom, lastRayTo, rayColor);
        dynamicsWorld.debugDrawWorld();
        debugDrawer.end();
    }

    @Override
    public void dispose() {
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
    }
}
