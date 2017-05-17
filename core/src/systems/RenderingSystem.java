package systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import components.PrimitiveShape;

import static com.badlogic.gdx.Gdx.gl;

public class RenderingSystem extends BaseEntitySystem {
    ComponentMapper<PrimitiveShape> primitives;

    int zoomTernary = 0;
    float zoomLevel = 0,zoomAmount = 0.3f;
    Vector3 initialPosition,tmpVec = new Vector3();

    ModelBatch batch;
    Camera camera;
    Vector3 center;
    Environment environment;

    public RenderingSystem(){
        super(Aspect.all(PrimitiveShape.class));

        batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(50,10,50);
        initialPosition = camera.position.cpy();
        center = new Vector3(0,0,0);
        camera.lookAt(center);
        camera.far = 200;
        camera.update();
        System.out.println(camera.near+","+camera.far);
    }

    public void resize(int width, int height){
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.lookAt(center);
        camera.update();
    }

    public void rotate(Vector3 axis, float angle){
        camera.rotateAround(center,axis,angle);

        tmpVec.set(center).sub(initialPosition);
        initialPosition.add(tmpVec).rotate(axis, angle);
        tmpVec.rotate(axis, angle);
        initialPosition.add(tmpVec.scl(-1));

        camera.lookAt(center);
        camera.update();
    }

    public void zoom(float amount){
        zoomLevel = MathUtils.clamp(zoomLevel + amount,0,0.5f);
        camera.position.set(initialPosition.cpy().interpolate(center,zoomLevel, Interpolation.linear));
        camera.update();
    }

    public void zoomIn(){zoomTernary = 1;}
    public void zoomOut(){zoomTernary = -1;}
    public void noZoom(){zoomTernary = 0;}

    @Override
    protected final void processSystem() {
        gl.glViewport(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        zoom(zoomTernary * zoomAmount * Gdx.graphics.getDeltaTime());

        batch.begin(camera);
        for(int id:subscription.getEntities().getData())
            batch.render(primitives.get(id).modelInstance,environment);
        batch.end();
    }

    @Override
    protected void dispose() {
        batch.dispose();
    }
}
