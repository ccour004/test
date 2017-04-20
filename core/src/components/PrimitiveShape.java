package components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;

import systems.PhysicsSystem;

public class PrimitiveShape extends PooledComponent {
    public Vector3 pos,dim;
    public PhysicsSystem.PRIMITIVE prim;
    public Material mat;

    public Model model;
    public ModelInstance modelInstance;

    public btCollisionShape collisionShape;
    public btCollisionObject collisionObject;

    public btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    public PrimitiveMotionState motionState;
    public btTransform transform;
    public btRigidBody rigidBody;
    public Vector3 localInertia;
    public float mass = 5;

    public PrimitiveShape(){}
    public PrimitiveShape(Vector3 pos, Vector3 dim, PhysicsSystem.PRIMITIVE prim,Material mat){
        this.pos = pos;
        this.dim = dim;
        this.prim = prim;
        this.mat = mat;
    }

    @Override
    protected void reset() {
        model.dispose();
        collisionShape.dispose();
        collisionObject.dispose();
        rigidBody.dispose();
        motionState.dispose();
        constructionInfo.dispose();
        transform.dispose();
        modelInstance = null;
    }
}
