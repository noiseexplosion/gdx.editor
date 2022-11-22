package gdx.awt.bullet;

import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.utils.Disposable;

public class BulletPhysicsSystem implements Disposable {
    private final btDynamicsWorld dynamicsWorld;
    private final btDispatcher dispatcher;
    private final btCollisionConfiguration collisionConfig;
    private final btBroadphaseInterface broadphase;
    private final btConstraintSolver constraintSolver;
    private final float fixedTimeStep = 1f / 60f;

    public BulletPhysicsSystem() {
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
    }
    public void update(float delta) {
        dynamicsWorld.stepSimulation(delta,1,fixedTimeStep);
    }
    public void addBody(btRigidBody body) {
        dynamicsWorld.addRigidBody(body);
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
