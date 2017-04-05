package com.schuhr.propgame.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.schuhr.propgame.PropGame;
import com.schuhr.propgame.Screens.Levels;

/**
 * Created by schuh on 12/10/2016.
 */

public class Mary extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, DEAD}

    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion maryStand;
    private TextureRegion maryDead;
    private Animation maryRun;
    private Animation maryJump;
    private float stateTimer;
    private boolean runningRight;
    private boolean maryIsDead;

    private Levels screen;

    public Mary(Levels screen) {
        super(screen.getAtlas().findRegion("Mary"));
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Mary"), i * 16, 0, 16, 16));
        }
        maryRun = new Animation(0.1f, frames);

        frames.clear();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("Mary"), 48, 0, 16, 16));

        maryJump = new Animation(0.3f, frames);

        maryStand = new TextureRegion(screen.getAtlas().findRegion("Mary"), 0, 0, 16, 16);

        maryDead = new TextureRegion(screen.getAtlas().findRegion("Mary"), 0, 0, 16, 16);

        defineMary();
        setBounds(0, 0, 16 / PropGame.PPM, 16 / PropGame.PPM);
        setRegion(maryStand);

    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(Float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = maryDead;
                break;
            case JUMPING:
                region = maryJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = maryRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = maryStand;
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (maryIsDead)
            return State.DEAD;
        else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void defineMary() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PropGame.PPM, 32 / PropGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PropGame.PPM);
        fdef.filter.categoryBits = PropGame.MARY_BIT;
        fdef.filter.maskBits = PropGame.GROUND_BIT |
                PropGame.COIN_BIT |
                PropGame.BRICK_BIT |
                PropGame.OBJECT_BIT |
                PropGame.ENEMY_BIT |
                PropGame.ENEMY_HEAD_BIT |
                PropGame.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / PropGame.PPM, 6 / PropGame.PPM), new Vector2(2 / PropGame.PPM, 6 / PropGame.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
        shape.dispose();
    }

    public void hit() {
        screen.getGame().manager.get("audio/sounds/mariodie.wav", Sound.class).play();
        maryIsDead = true;
        Filter filter = new Filter();
        filter.maskBits = PropGame.NOTHING_BIT;
        for (Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void jump() {
        if (currentState != State.JUMPING) {
            b2body.applyLinearImpulse(new Vector2(0, 3.5f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }
}
