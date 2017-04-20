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

import components.PrimitiveShape;

import static com.badlogic.gdx.Gdx.gl;

public class RenderingSystem extends BaseEntitySystem {
    ComponentMapper<PrimitiveShape> primitives;

    ModelBatch batch;
    Camera camera;
    Environment environment;

    public RenderingSystem(){
        super(Aspect.all(PrimitiveShape.class));

        batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(50,10,50);
        camera.lookAt(0,0,0);
        camera.update();
    }

    @Override
    protected final void processSystem() {
        gl.glViewport(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
