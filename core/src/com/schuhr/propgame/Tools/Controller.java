package com.schuhr.propgame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.schuhr.propgame.PropGame;

/**
 * Created by schuh on 1/9/2017.
 */

public class Controller {
    Viewport viewport;
    public Stage stage;
    boolean leftPressed;
    boolean rightPressed;

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean isCanJump() {
        return canJump;
    }

    boolean canJump = false;
    OrthographicCamera cam;

    public Controller(PropGame game) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(PropGame.V_WIDTH, PropGame.V_HEIGHT, cam);
        stage = new Stage(viewport, game.batch);

        Table table = new Table();

        Actor screenActor = CreateScreenActor();

        table.left().bottom();
        Image leftImage = createLeft();

        Image rightImage = createRight();

        table.add(leftImage).size(leftImage.getWidth(), leftImage.getHeight());
        table.add();
        table.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight());
        table.row().padBottom(10f);

        stage.addActor(screenActor);
        stage.addActor(table);

    }

    public void draw() {
        stage.draw();
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        stage.dispose();
    }

    public Image createLeft() {

        Image leftImage = new Image(new Texture("Arrows/arrow_left.png"));
        leftImage.setSize(50, 50);
        leftImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
        return leftImage;
    }

    public Image createRight() {
        Image rightImage = new Image(new Texture("Arrows/arrow_right.png"));
        rightImage.setSize(50, 50);
        rightImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });
        return rightImage;
    }

    public Actor CreateScreenActor() {
        Actor screenActor = new Actor();

        screenActor.setWidth(viewport.getScreenWidth());
        screenActor.setHeight(viewport.getScreenHeight());
        screenActor.setY(0);
        screenActor.setX(0);

        screenActor.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                canJump = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                canJump = false;
            }
        });
        return screenActor;
    }
}
