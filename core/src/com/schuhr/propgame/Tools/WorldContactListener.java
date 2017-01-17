package com.schuhr.propgame.Tools;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Timer;
import com.schuhr.propgame.PropGame;
import com.schuhr.propgame.Sprites.Enemy;
import com.schuhr.propgame.Sprites.InteractiveTileObject;
import com.schuhr.propgame.Sprites.Mary;

/**
 * Created by schuh on 12/11/2016.
 */

public class WorldContactListener implements ContactListener {
    private PropGame game;
    private Timer contactTime;
    boolean canContact = true;

    public WorldContactListener(PropGame game){
        this.game = game;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cdef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch (cdef) {
            case PropGame.ENEMY_HEAD_BIT | PropGame.MARY_BIT:
                if (fixA.getFilterData().categoryBits == PropGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead();
                else
                    ((Enemy) fixB.getUserData()).hitOnHead();
                canContact=false;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        canContact = true;
                    }
                }, .2f);
                break;
            case PropGame.ENEMY_BIT | PropGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == PropGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PropGame.MARY_BIT | PropGame.ENEMY_BIT:
                if(canContact) {
                    if (fixA.getFilterData().categoryBits == PropGame.MARY_BIT)
                        ((Mary) fixA.getUserData()).hit();
                    else
                        ((Mary) fixB.getUserData()).hit();
                }
                break;
            case PropGame.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PropGame.MARY_BIT | PropGame.END_BIT:
                game.getMusic().stop();
                Preferences prefs = Gdx.app.getPreferences("SETTINGS");
                int level = prefs.getInteger("Level");
                level++;
                prefs.putInteger("Level", level);
                prefs.flush();
                ((PropGame)game).CreateLevel();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
