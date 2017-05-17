package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import components.PrimitiveShape;
import systems.PhysicsSystem;

public class FluidExample extends helpers.AppTestBase{
    PrimitiveShape groundShape,groundShape1;

	@Override
	public void create () {
        super.create();
        Gdx.input.setInputProcessor(new InputAdapter(){
            float lastX = 0,lastY = 0;
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button){
                lastX = screenX;lastY = screenY;
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer){
                renderer.rotate(new Vector3(0,1,0),screenX - lastX);
                lastX = screenX;
                return true;
            }

            @Override
            public boolean keyDown (int keycode){
                switch(keycode){
                    case Input.Keys.Q: renderer.zoomIn();return true;
                    case Input.Keys.W: renderer.zoomOut();return true;
                    default: renderer.noZoom();
                }
                return false;
            }

            @Override public boolean keyUp(int keycode){renderer.noZoom();return true;}
        });

        //Create ground.
        PhysicsSystem.addObstruction(new Vector3(5, -8, 29),
                                     new Vector3(50, 3, 2),world.create());
        PhysicsSystem.addObstruction(new Vector3(29,-8, 5),
                                     new Vector3(2, 3, 50),world.create());
        PhysicsSystem.addObstruction(new Vector3(-19,-8, 5),
                                     new Vector3(2, 3, 50),world.create());
        PhysicsSystem.addObstruction(new Vector3(5,-8, -19),
                                     new Vector3(50, 3, 2),world.create());
        PhysicsSystem.addObstruction(new Vector3(5, -10, 5),
                                     new Vector3(50, 2, 50),world.create());

        //Read fluid description in.
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Gdx.files.internal("cube-drop.txt").read()))) {
            br.readLine(); //skip the count, don't need it
            String line = br.readLine();
            while (line != null) {
                String[] splitRes = line.split(" ");
                int e = world.create();
                PrimitiveShape shape = PhysicsSystem.primitives.create(e);
                shape.pos = new Vector3(Float.parseFloat(splitRes[0]), Float.parseFloat(splitRes[1]), Float.parseFloat(splitRes[2]));
                shape.dim = new Vector3(2, 2, 2);
                shape.prim = PhysicsSystem.PRIMITIVE.SPHERE;
                shape.mat = new Material(ColorAttribute.createDiffuse(
                        (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random()));
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
