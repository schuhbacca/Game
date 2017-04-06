package com.schuhr.propgame.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.schuhr.propgame.PropGame;
import com.schuhr.propgame.Screens.Levels;

/**
 * Created by schuh on 12/12/2016.
 */

public abstract class Enemy extends Sprite {
    World world;
    protected Levels screen;
    public Body b2body;
    Vector2 velocity;
    private float mod;
    protected boolean setToDestroy;

    public enum State {WALKING, JUMPING, DEAD, FALLING}

    protected State currentState;
    protected State previousState;

    Enemy(Levels screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y + 0.0799f);
        defineEnemy();
        velocity = new Vector2(-1f, 0);
        b2body.setActive(false);
        currentState = State.WALKING;
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public abstract void update(float dt);

    public void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }

    public void getUnstuck(float dt) {
        if ((b2body.getLinearVelocity().x == 0)) {
            mod += dt;
            if (mod > .5) {
                reverseVelocity(true, false);
                mod = 0;
            }
        } else {
            mod = 0;
        }
    }
}
