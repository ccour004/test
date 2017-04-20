package components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class PrimitiveMotionState extends btMotionState {
    ModelInstance modelInstance;
    public PrimitiveMotionState(ModelInstance modelInstance){
        this.modelInstance = modelInstance;
    }

    @Override public void getWorldTransform(Matrix4 worldTrans){worldTrans.set(modelInstance.transform);}
    @Override public void setWorldTransform(Matrix4 worldTrans){modelInstance.transform.set(worldTrans);}
}
