package com.schuhr.propgame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.schuhr.propgame.Screens.Levels;

/**
 * Created by schuh on 12/12/2016.
 */

public abstract class Enemy extends Sprite {
    protected World world;
    protected Levels screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(Levels screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1,0);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public abstract void update(float dt);

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
