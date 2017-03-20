package com.schuhr.propgame.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.schuhr.propgame.PropGame;
import com.schuhr.propgame.Screens.Levels;

import java.util.Random;

/**
 * Created by schuhr on 1/18/2017.
 */

public class BlueEnemy extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private boolean destroyed;

    public BlueEnemy(Levels screen, float x, float y) {
        super(screen, x, y);
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 16, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / PropGame.PPM, 16 / PropGame.PPM);
        setToDestroy = false;
        destroyed = false;
        //overwrite velocity
        Random rand = new Random();
        int  n = rand.nextInt(3) + 1;
        velocity = new Vector2((float)Math.round(n),0);
        currentState = State.WALKING;
    }

    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("Enemies"), 48, 16, 16, 16));

            stateTime = 0;
        } else if (!destroyed) {
            //Checks to see if the enemy is stuck somehwere and if they are reverse their direction
            getUnstuck(dt);

            //The movement logic, and logic for falling off and edge correctly
            move();
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));

        }
    }

    public void move(){
        switch(currentState){
            case WALKING:
                b2body.setLinearVelocity(velocity);
                CheckIfBelowLevel();
                break;
            case FALLING:
                Vector2 help = new Vector2(0,0.1f);
                b2body.applyLinearImpulse(help, b2body.getWorldCenter(), true);
                currentState = State.DEAD;
                break;
            case DEAD:
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        setToDestroy = true;
                    }
                }, 1f);
                break;
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PropGame.PPM);
        fdef.filter.categoryBits = PropGame.ENEMY_BIT;
        fdef.filter.maskBits = PropGame.GROUND_BIT |
                PropGame.COIN_BIT |
                PropGame.BRICK_BIT |
                PropGame.ENEMY_BIT |
                PropGame.OBJECT_BIT |
                PropGame.MARY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create the head here
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / PropGame.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / PropGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / PropGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / PropGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = PropGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
        shape.dispose();
    }

    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
    }

}
