package com.schuhr.propgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.schuhr.propgame.Screens.*;

public class PropGame extends Game {
    public SpriteBatch batch;
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    public static final float PPM = 100;

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARY_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short END_BIT = 256;

    public AssetManager manager;

    Preferences prefs;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    private Music music;

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("SETTINGS");
        if (GetLevel() == 0) {
            prefs.putInteger("Level", 1);
            prefs.flush();
        }
        batch = new SpriteBatch();
        manager = new AssetManager();
        loadAssets();
        manager.finishLoading();
        setScreen(new Menu(this));
    }

    public void CreateLevel() {
        if (GetLevel() != -1) {
            setScreen(new LevelsIntro(this));
        } else {
            setScreen(new GameOverScreen(this));
        }
    }

    public void loadAssets() {
        for (Songs s : Songs.values()) {
            manager.load(s.getValue(), Music.class);
        }
        manager.load("audio/sounds/coin.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.load("audio/sounds/mariodie.wav", Sound.class);
    }

    public int GetLevel() {
        return prefs.getInteger("Level");
    }

    public int GetLast(){ return prefs.getInteger("Last");}

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        music.dispose();
    }

    public enum Songs {
        Saviour("audio/music/Saviour.mp3"),
        Sloth("audio/music/SlothsRevenge.mp3"),
        AMomentLikeThis("audio/music/AMomentLikeThis.mp3"),
        Austin("audio/music/Austin.mp3"),
        IveBeenEverywhere("audio/music/IveBeenEverywhere.mp3"),
        BonfireHeart("audio/music/BonfireHeart.mp3"),
        HeyThereDelilah("audio/music/HeyThereDelilah.mp3"),
        BestDayOfMyLife("audio/music/BestDayOfMyLife.mp3"),
        FoolsLove("audio/music/FoolsLove.mp3"),
        SomethingWild("audio/music/SomethingWild.mp3"),
        ImYours("audio/music/ImYours.mp3"),
        PerfectForMe("audio/music/PerfectForMe.mp3"),
        Hobbits("audio/music/Hobbits.mp3");


        private final String name;

        Songs(String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }
}
