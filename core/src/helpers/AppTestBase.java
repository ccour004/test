package helpers;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import systems.PhysicsSystem;
import systems.RenderingSystem;

public class AppTestBase extends ApplicationAdapter {
    protected World world;

    @Override
    public void create () {
        world = new World(new WorldConfigurationBuilder()
                .with(new RenderingSystem(),new PhysicsSystem())
                .build());
    }

    @Override
    public void render () {
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();
    }

    @Override
    public void dispose () {
        world.dispose();
    }
}
