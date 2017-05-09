package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import components.PrimitiveShape;
import systems.PhysicsSystem;

public class FluidExample extends helpers.AppTestBase{
    PrimitiveShape groundShape,groundShape1;

	@Override
	public void create () {
        super.create();
        /*Gdx.input.setInputProcessor(new InputAdapter(){
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
        });*/

       /* int maxX = 100,maxZ = 100,step = 10;
        for(int i = 0;i < maxX;i += step)
            for(int j = 0;j < maxZ;j += step){
                int e_ground1 = world.create();
                groundShape1 = PhysicsSystem.primitives.create(e_ground1);
                groundShape1.pos = new Vector3(i, -10, j);
                groundShape1.dim = new Vector3(2, 10, 2);
                groundShape1.prim = PhysicsSystem.PRIMITIVE.BOX;
                groundShape1.mat = new Material(ColorAttribute.createDiffuse(1,1,1,1));
                groundShape1.mass = 0;
            }*/

        int e_ground1 = world.create();
        groundShape1 = PhysicsSystem.primitives.create(e_ground1);
        groundShape1.pos = new Vector3(5, -8, 30);
        groundShape1.dim = new Vector3(50, 3, 2);
        groundShape1.prim = PhysicsSystem.PRIMITIVE.BOX;
        groundShape1.mat = new Material(ColorAttribute.createDiffuse(1,1,1,1));
        groundShape1.mass = 0;

        int e_ground2 = world.create();
        groundShape1 = PhysicsSystem.primitives.create(e_ground2);
        groundShape1.pos = new Vector3(30, -8, 5);
        groundShape1.dim = new Vector3(2, 3, 50);
        groundShape1.prim = PhysicsSystem.PRIMITIVE.BOX;
        groundShape1.mat = new Material(ColorAttribute.createDiffuse(1,1,1,1));
        groundShape1.mass = 0;

        //Create ground.
        int e_ground = world.create();
        groundShape = PhysicsSystem.primitives.create(e_ground);
        groundShape.pos = new Vector3(5, -10, 5);
        groundShape.dim = new Vector3(50, 2, 50);
        groundShape.prim = PhysicsSystem.PRIMITIVE.BOX;
        groundShape.mat = new Material(ColorAttribute.createDiffuse(1,1,1,1));
        groundShape.mass = 0;

        //Read fluid description in.
        try(BufferedReader br = new BufferedReader(new FileReader(Gdx.files.internal("cube-drop.txt").file()))) {
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
