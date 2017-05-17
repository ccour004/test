package systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import components.PrimitiveMotionState;
import components.PrimitiveShape;

import static com.badlogic.gdx.Gdx.gl;

public class PhysicsSystem extends BaseEntitySystem {
    public enum PRIMITIVE{BOX,SPHERE,CONE,CAPSULE};
    public static ComponentMapper<PrimitiveShape> primitives;

    static int MAX_SUB_STEPS = 5;
    static float FIXED_TIME_STEP = 1f/60f;
    final float GRAVITY_CONSTANT = -9.8f;

    btDynamicsWorld world;
    btConstraintSolver solver;

    btCollisionDispatcher dispatcher;
    btDbvtBroadphase broadphase;
    btDefaultCollisionConfiguration config;

    public PhysicsSystem(){
        super(Aspect.all(PrimitiveShape.class));

        Bullet.init();

        solver = new btSequentialImpulseConstraintSolver();
        config = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(config);
        broadphase = new btDbvtBroadphase();
        world = new btDiscreteDynamicsWorld(dispatcher,broadphase,solver,config);

        world.setGravity(new Vector3(0,GRAVITY_CONSTANT,0));
    }

    @Override
    protected void processSystem() {
        float delta = Math.min(1f/30f, Gdx.graphics.getDeltaTime());
        world.stepSimulation(delta,MAX_SUB_STEPS,FIXED_TIME_STEP);
    }

    @Override
    protected void inserted(int e){
        primitives.create(e);
        PrimitiveShape entity = primitives.get(e);

        float radius,height,diameter;
        switch (entity.prim){
            case BOX:
                entity.model = new ModelBuilder().createBox(entity.dim.x,entity.dim.y,entity.dim.z,entity.mat, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                entity.collisionShape = new btBoxShape(new Vector3(entity.dim.x/2.0f,entity.dim.y/2.0f,entity.dim.z/2.0f));
                break;
            case SPHERE:
                diameter = entity.dim.x;
                entity.model = new ModelBuilder().createSphere(diameter,diameter,diameter,20,20,entity.mat, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                entity.collisionShape = new btSphereShape(diameter/2.0f);
                break;
            case CONE:
                diameter = entity.dim.x;
                height = entity.dim.y;
                entity.model = new ModelBuilder().createCone(diameter,height,diameter,20,gl.GL_TRIANGLES,entity.mat, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                entity.collisionShape = new btConeShape(diameter/2.0f,height);
                break;
            case CAPSULE:
                radius = entity.dim.y / 4;
                height = entity.dim.y;
                entity.model = new ModelBuilder().createCapsule(radius,height,20,entity.mat, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                entity.collisionShape = new btCapsuleShape(radius,height);
                break;
        }

        entity.modelInstance = new ModelInstance(entity.model);
        entity.modelInstance.transform.trn(entity.pos);

        entity.localInertia = new Vector3(0,0,0);
        if(entity.mass > 0) entity.collisionShape.calculateLocalInertia(entity.mass,entity.localInertia);
        entity.motionState = new PrimitiveMotionState(entity.modelInstance);

        entity.constructionInfo =
                new btRigidBody.btRigidBodyConstructionInfo(entity.mass,entity.motionState,entity.collisionShape,entity.localInertia);
        entity.rigidBody = new btRigidBody(entity.constructionInfo);
        entity.rigidBody.setRollingFriction(0.2f);
        entity.rigidBody.setFriction(2);

        if(entity.mass == 0){
            entity.rigidBody.setCollisionFlags(btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
            entity.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
            entity.rigidBody.setFriction(2);
            entity.rigidBody.setRollingFriction(0.2f);
        }
        world.addRigidBody(entity.rigidBody);
    }

    public static void addObstruction(Vector3 pos,Vector3 dim,int id){
        PrimitiveShape primitiveShape = primitives.create(id);
        primitiveShape.pos = pos;
        primitiveShape.dim = dim;
        primitiveShape.prim = PhysicsSystem.PRIMITIVE.BOX;
        primitiveShape.mat = new Material(ColorAttribute.createDiffuse(1,1,1,1));
        primitiveShape.mass = 0;
    }

    @Override
    protected void dispose() {
        world.dispose();
        solver.dispose();
    }
}
