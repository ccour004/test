package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

import components.PrimitiveShape;
import systems.PhysicsSystem;

public class CameraExample extends helpers.AppTestBase{
    PrimitiveShape groundShape;

	@Override
	public void create () {
        super.create();
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                groundShape.modelInstance.transform.rotate(0,0,1,5);
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button){
                groundShape.modelInstance.transform.rotate(0,0,1,5);
                return true;
            }
        });

        int STEP_X = 5,STEP_Z = 5;

        //Create ground.
        int e_ground = world.create();
        groundShape = PhysicsSystem.primitives.create(e_ground);
        groundShape.pos = new Vector3((10 * STEP_X) / 2.0f, -10, (10 * STEP_Z) / 2.0f);
        groundShape.dim = new Vector3(100, 2, 100);
        groundShape.prim = PhysicsSystem.PRIMITIVE.BOX;
        groundShape.mat = new Material(ColorAttribute.createDiffuse(1,1,1,1));
        groundShape.mass = 0;

        for(int i = 0;i < 10;i++)
            for(int j = 0;j < 10;j++) {
                int e = world.create();
                PrimitiveShape shape = PhysicsSystem.primitives.create(e);
                shape.pos = new Vector3(i * STEP_X, 0, j * STEP_Z);
                shape.dim = new Vector3(2, 2, 2);
                shape.prim = PhysicsSystem.PRIMITIVE.values()[(int) (Math.random() * PhysicsSystem.PRIMITIVE.values().length)];
                shape.mat = new Material(ColorAttribute.createDiffuse(
                                (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random()));
            }
    }
}
