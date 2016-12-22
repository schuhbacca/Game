package com.schuhr.propgame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.schuhr.propgame.PropGame;
import com.schuhr.propgame.Scenes.Hud;
import com.schuhr.propgame.Screens.PlayScreen;

/**
 * Created by schuh on 12/11/2016.
 */

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds){
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
