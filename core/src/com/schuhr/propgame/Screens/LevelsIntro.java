package com.schuhr.propgame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.schuhr.propgame.PropGame;

/**
 * Created by schuhr on 1/4/2017.
 */

public class LevelsIntro implements Screen, InputProcessor {
    private Viewport viewport;
    private Stage stage;
    Label levelLabel;

    private PropGame game;

    public LevelsIntro(PropGame game) {
        this.game = game;
        viewport = new FitViewport(PropGame.V_WIDTH, PropGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((PropGame) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        levelLabel = new Label("LEVEL: " + ((PropGame) game).GetLevel(), font);
        Label playAgainLabel = new Label("Click to play!", font);
        table.add(levelLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        stage.addActor(table);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            try{
                if(((PropGame) game).GetLevel() == 12 && ((PropGame) game).GetLast() != 1) {
                    throw new Exception();
                }else {
                    game.setScreen(new Levels((PropGame) game));
                }
            }catch (Exception ex){
                levelLabel.setText("Unable to play level.");
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        ((PropGame) game).setScreen(new Menu(game));
        dispose();
        return false;
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
