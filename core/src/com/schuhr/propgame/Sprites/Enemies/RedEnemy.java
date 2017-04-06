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
 * Created by schuh on 12/12/2016.
 */

public class RedEnemy extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private boolean destroyed;

    public RedEnemy(Levels screen, float x, float y) {
        super(screen, x, y);
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemies"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / PropGame.PPM, 16 / PropGame.PPM);
        setToDestroy = false;
        destroyed = false;
        Random rand = new Random();
        jumpCondition = rand.nextInt((6 - 1) + 1) + 1;
        currentState = State.WALKING;
    }

    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("Enemies"), 48, 0, 16, 16));

            stateTime = 0;
        } else if (!destroyed) {
            //Checks to see if the enemy is stuck somehwere and if they are reverse their direction
            getUnstuck(dt);

            //Implement the movement logic which includes jumping
            move(dt);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
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

    private float jumpTimer = 0;
    private float jumpCondition;

    public void move(float dt) {
        switch (currentState) {
            case WALKING:
                b2body.setLinearVelocity(velocity);
                break;
            case JUMPING:
                jump();
                break;
            case FALLING:
                if (b2body.getLinearVelocity().y == 0) {
                    currentState = State.WALKING;
                }

                break;
            case DEAD:
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        setToDestroy = true;
                    }
                }, 1f);
                break;
            default:
                currentState = State.WALKING;
        }

        previousState = currentState;

        jumpTimer += dt;
        if (jumpTimer > jumpCondition && currentState != State.JUMPING && currentState != State.FALLING) {
            currentState = State.JUMPING;
        }
    }

    public void jump() {
        Vector2 jumpVelocity = new Vector2(0, 3f);
        if (velocity.x < 0)
            jumpVelocity.x = -1f;
        else
            jumpVelocity.x = 1f;

        b2body.applyLinearImpulse(jumpVelocity, b2body.getWorldCenter(), true);
        jumpTimer = 0;
        Random rand = new Random();
        jumpCondition = rand.nextInt((4 - 1) + 1) + 1;
        currentState = State.FALLING;
    }
}
