package com.schuhr.propgame.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.schuhr.propgame.PropGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.schuhr.propgame.Scenes.Hud;
import com.schuhr.propgame.Sprites.*;
import com.schuhr.propgame.Sprites.Enemies.Enemy;
import com.schuhr.propgame.Tools.*;
import com.schuhr.propgame.PropGame.Songs;

/**
 * Created by schuhr on 1/3/2017.
 */

public class Levels implements Screen, InputProcessor {

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
    protected B2WorldCreator creator;

    protected Mary player;

    protected Viewport gamePort;

    protected Controller controller;

    boolean isAndroid = false;
    boolean hide = false;

    public Levels(PropGame game) {
        this.game = game;
        atlas = new TextureAtlas("Levels/Characters.pack");

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(PropGame.V_WIDTH / PropGame.PPM, PropGame.V_HEIGHT / PropGame.PPM, gameCam);

        hud = new Hud(game);

        mapLoader = new TmxMapLoader();

        map = mapLoader.load("Levels/Level" + game.GetLevel() + ".tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / PropGame.PPM);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);

        creator = new B2WorldCreator(this);

        player = new Mary(this);

        world.setContactListener(new WorldContactListener(game));

        controller = new Controller(game);

        if (!game.getMusic().isPlaying())
            SetMusic();

        isAndroid = (Gdx.app.getType() == Application.ApplicationType.Android);

        //Create the input processors and multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(controller.stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
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
        int skipForward = 0;
        switch (game.GetLevel()) {
            case 1://Hiking Up North
                game.setMusic(game.manager.get(Songs.SomethingWild.getValue(), Music.class));
                break;
            case 2://LOTR, Milkshakes, Italian
                game.setMusic(game.manager.get(Songs.Hobbits.getValue(), Music.class));
                skipForward = 5;
                break;
            case 3://Traverse City
                game.setMusic(game.manager.get(Songs.Sloth.getValue(), Music.class));
                break;
            case 4://Chill on the hill
                game.setMusic(game.manager.get(Songs.Saviour.getValue(), Music.class));
                break;
            case 5://Mary's Place
                game.setMusic(game.manager.get(Songs.WeAreYoung.getValue(), Music.class));
                skipForward = 8;
                break;
            case 6://Visit to St. Clair
                game.setMusic(game.manager.get(Songs.Austin.getValue(), Music.class));
                skipForward = 7;
                break;
            case 7://Europe
                game.setMusic(game.manager.get(Songs.IveBeenEverywhere.getValue(), Music.class));
                break;
            case 8://Labor Day
                game.setMusic(game.manager.get(Songs.BonfireHeart.getValue(), Music.class));
                skipForward = 4;
                break;
            case 9://Visit St. Joe
                game.setMusic(game.manager.get(Songs.HeyThereDelilah.getValue(), Music.class));
                skipForward = 9;
                break;
            case 10://Christmas
                game.setMusic(game.manager.get(Songs.PerfectForMe.getValue(), Music.class));
                skipForward = 3;
                break;
            case 11://Picture Level
                game.setMusic(game.manager.get(Songs.ImYours.getValue(), Music.class));
                skipForward = 24;
                break;
            case 12://Proposal
                game.setMusic(game.manager.get(Songs.BestDayOfMyLife.getValue(), Music.class));
                break;
        }
        game.getMusic().setLooping(true);
        game.getMusic().play();
        game.getMusic().setPosition(skipForward);
    }

    public void handleInput(float dt) {
        if (player.currentState != Mary.State.DEAD) {
            //Controls for desktop
            if (!isAndroid) {
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                }
                if (Gdx.input.justTouched()) {
                    player.jump();
                }
            } else {
                //Controls for Android
                if (controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 2) {
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                }
                if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -2) {
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                }
                if (controller.isCanJump()) {
                    player.jump();
                    controller.setCanJump(false);
                }
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 90f, 6, 2);
        player.update(dt);
        for (Enemy enemy : creator.getRedEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / PropGame.PPM)
                enemy.b2body.setActive(true);
        }
        for (Enemy enemy : creator.getBlueEnemies()) {
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
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getRedEnemies()) {
            enemy.draw(game.batch);
        }
        for (Enemy enemy : creator.getBlueEnemies()) {
            enemy.draw(game.batch);
        }
        game.batch.end();

        if (isAndroid)
            controller.draw();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {
        hide = false;
    }

    public boolean gameOver() {
        return (player.currentState == Mary.State.DEAD && player.getStateTimer() > 3
                || (player.b2body.getPosition().y < (-100 / PropGame.PPM))
                || hud.getWorldTimer() == 0);
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);

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
        controller.dispose();
        hud.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            ((PropGame) game).setScreen(new Menu(game));
            dispose();
            return true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
