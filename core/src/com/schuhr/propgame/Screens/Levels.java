package com.schuhr.propgame.Screens;

import com.schuhr.propgame.PropGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.schuhr.propgame.Scenes.Hud;
import com.schuhr.propgame.Sprites.*;
import com.schuhr.propgame.Tools.*;
import com.schuhr.propgame.PropGame.Songs;

/**
 * Created by schuhr on 1/3/2017.
 */

public class Levels implements Screen {

    protected PropGame game;
    protected TextureAtlas atlas;

    protected OrthographicCamera gameCam;
    protected Hud hud;

    //TiledMapVariables
    protected TmxMapLoader mapLoader;
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer renderer;

    //Box2dVariables
    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected B2WorldCreator creator;

    protected Mary player;

    protected Viewport gamePort;

    protected Music music;

    protected String levelName;

    public Levels(PropGame game) {
        this.game = game;
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(PropGame.V_WIDTH / PropGame.PPM, PropGame.V_HEIGHT / PropGame.PPM, gameCam);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();

        map = mapLoader.load("level" + game.GetLevel() + ".tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / PropGame.PPM);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Mary(this);

        world.setContactListener(new WorldContactListener(game));

        SetMusic();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public PropGame getGame() {
        return game;
    }

    private void SetMusic() {
        switch (game.GetLevel()) {
            case 1:
                //music = game.manager.get("audio/music/mario_music.ogg", Music.class);
                music = game.manager.get(Songs.Saviour.getValue(), Music.class);
                break;
            case 2:
                music = game.manager.get(Songs.Sloth.getValue(), Music.class);
                break;
            case 3:
                music = game.manager.get(Songs.YouBelongWithMe.getValue(), Music.class);
                break;
            case 4:
                music = game.manager.get(Songs.Austin.getValue(), Music.class);
                break;
            case 5:
                music = game.manager.get(Songs.IveBeenEverywhere.getValue(), Music.class);
                break;
            case 6:
                music = game.manager.get(Songs.BonfireHeart.getValue(), Music.class);
                break;
            case 7:
                music = game.manager.get(Songs.HeyThereDelilah.getValue(), Music.class);
                break;
            case 8:
                music = game.manager.get(Songs.BestDayOfMyLife.getValue(), Music.class);
                break;
        }

        music.setLooping(true);
        music.play();
    }

    public void handleInput(float dt) {
        if (player.currentState != Mary.State.DEAD) {
            float accelY = Gdx.input.getAccelerometerY();

            if (Gdx.input.justTouched()) {
                player.jump();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if ((accelY > -1) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if ((accelY < 1) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        for (Enemy enemy : creator.getGoombas()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / PropGame.PPM)
                enemy.b2body.setActive(true);
        }
        hud.update(dt);

        if (player.currentState != Mary.State.DEAD)
            gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getGoombas()) {
            enemy.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {

    }

    public boolean gameOver() {
        if (player.currentState == Mary.State.DEAD && player.getStateTimer() > 3
                || (player.b2body.getPosition().y < (-100 / PropGame.PPM)))
            return true;
        else
            return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
