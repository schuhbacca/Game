package com.schuhr.propgame.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.schuhr.propgame.PropGame;
import com.schuhr.propgame.Screens.*;
import com.schuhr.propgame.Sprites.Brick;
import com.schuhr.propgame.Sprites.Coin;
import com.schuhr.propgame.Sprites.Goomba;

/**
 * Created by schuh on 12/11/2016.
 */

public class B2WorldCreator {

    private enum ObjectIndexes{
        Background(0),
        Graphics(1),
        Ground(2),
        Pipes(3),
        Coins(4),
        Bricks(5),
        Goombas(6),
        Turtles(7),
        End(8);

        private final int id;
        ObjectIndexes(int id) { this.id = id; }
        public int getValue() { return id; }
    }

    private Array<Goomba> goombas;

    public B2WorldCreator(Levels screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for (MapObject object : map.getLayers().get(ObjectIndexes.Ground.id).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)  / PropGame.PPM, (rect.getY() + rect.getHeight() / 2)  / PropGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PropGame.PPM, rect.getHeight() / 2 / PropGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PropGame.GROUND_BIT;
            body.createFixture(fdef);
        }

        //create pipe bodies/fixtures
        for (MapObject object : map.getLayers().get(ObjectIndexes.Pipes.id).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)  / PropGame.PPM, (rect.getY() + rect.getHeight() / 2)  / PropGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PropGame.PPM, rect.getHeight() / 2 / PropGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PropGame.OBJECT_BIT;
            body.createFixture(fdef);
        }

        //create end of game bodies/fixtures
        for (MapObject object : map.getLayers().get(ObjectIndexes.End.id).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)  / PropGame.PPM, (rect.getY() + rect.getHeight() / 2)  / PropGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PropGame.PPM, rect.getHeight() / 2 / PropGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PropGame.END_BIT;
            body.createFixture(fdef);
        }


        //create bricks bodies/fixtures
        for (MapObject object : map.getLayers().get(ObjectIndexes.Bricks.id).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Brick(screen, rect);
        }

        //create coins bodies/fixtures
        for (MapObject object : map.getLayers().get(ObjectIndexes.Coins.id).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Coin(screen,rect);
        }

        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(ObjectIndexes.Goombas.id).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / PropGame.PPM, rect.getY() / PropGame.PPM));
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
}
