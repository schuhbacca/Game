package com.schuhr.propgame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.schuhr.propgame.PropGame;

/**
 * Created by schuhr on 1/6/2017.
 */

public class LevelUnlock implements Screen, InputProcessor {

    private Viewport viewport;
    private Stage stage;
    TextureAtlas atlas;
    Skin skin;
    private String passcode;
    private String passcodeAnswer = "8189";

    private PropGame game;

    public LevelUnlock(PropGame game) {
        this.game = game;
        viewport = new FitViewport(PropGame.V_WIDTH, PropGame.V_HEIGHT, new OrthographicCamera());
        atlas = new TextureAtlas("Visui/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("Visui/uiskin.json"), atlas);

        stage = new Stage(viewport, ((PropGame) game).batch);
    }

    @Override
    public void show() {
        Table titleTable = new Table();
        Table numberTable = new Table();
        titleTable.setFillParent(true);
        numberTable.setFillParent(true);
        titleTable.top();
        numberTable.top();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Visui/Raiders.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        font.setUseIntegerPositions(false);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Label titleLabel = new Label("LEVEL UNLOCK", labelStyle);
        titleTable.add(titleLabel).padTop(10f).center().expandX();

        numberTable.padTop(30f);
        //Create buttons
        TextButton[] buttons = new TextButton[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = new TextButton(String.valueOf(i + 1), skin, "default");
            //Add listeners to buttons
            final int h = i + 1;
            buttons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    passcode = passcode + String.valueOf(h);
                    CheckPasscode();
                }
            });
            if (i % 3 == 0) {
                numberTable.row();
            }
            numberTable.add(buttons[i]).padRight(10f).padTop(10f);
        }

        //Create the input processors and multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
        //Add table to stage
        stage.addActor(titleTable);
        stage.addActor(numberTable);
    }

    public void CheckPasscode() {
        if (passcode.equals(passcodeAnswer)) {
            //Set the preference to -1 so the last level can be accessed
        } else {
            if (passcode.length() > 4) {
                passcode = "";
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
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

    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        ((PropGame) game).setScreen(new Menu(game));
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
