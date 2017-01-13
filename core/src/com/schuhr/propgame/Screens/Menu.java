package com.schuhr.propgame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

public class Menu implements Screen {
    private Viewport viewport;
    private Stage stage;
    TextureAtlas atlas;
    Skin skin;

    private PropGame game;

    public Menu(PropGame game){
        this.game = game;
        viewport = new FitViewport(PropGame.V_WIDTH, PropGame.V_HEIGHT, new OrthographicCamera());
        atlas = new TextureAtlas("Visui/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("Visui/uiskin.json"), atlas);
        stage = new Stage(viewport, ((PropGame) game).batch);
        game.setMusic(game.manager.get(PropGame.Songs.FoolsLove.getValue(), Music.class));
        game.getMusic().setLooping(true);
        game.getMusic().play();
        game.getMusic().setPosition(7);
    }

    @Override
    public void show() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Visui/Raiders.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        font.setUseIntegerPositions(false);
        generator.dispose();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Label titleLabel = new Label("The Game of Mare Bear",labelStyle);

        //Create buttons
        TextButton playButton = new TextButton("Play", skin, "blue");
        TextButton optionsButton = new TextButton("Options", skin, "blue");
        TextButton exitButton = new TextButton("Exit", skin, "blue");

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getMusic().stop();
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelsIntro(game));
            }
        });
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelUnlock(game));
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons/label to table
        mainTable.add(titleLabel).expandX().padTop(10f);
        mainTable.row();
        mainTable.add(playButton).padTop(10f);
        mainTable.row();
        mainTable.add(optionsButton).padTop(10f);
        mainTable.row();
        mainTable.add(exitButton).padTop(10f);

        Gdx.input.setInputProcessor(stage);

        //Add table to stage
        stage.addActor(mainTable);

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
        skin.dispose();
    }
}
