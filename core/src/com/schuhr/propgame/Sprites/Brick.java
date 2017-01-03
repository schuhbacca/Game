package com.schuhr.propgame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.schuhr.propgame.PropGame;
import com.schuhr.propgame.Scenes.Hud;
import com.schuhr.propgame.Screens.*;

/**
 * Created by schuh on 12/11/2016.
 */

public class Brick extends InteractiveTileObject {
    public Brick(Levels screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PropGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(PropGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        PropGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
