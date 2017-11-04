package com.jumpdontdie.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.jumpdontdie.Constants.PIXELS_IN_METER;

/**
 * Created by raffa on 02/11/17.
 */

public class CrateEntity extends Actor {

    private Texture texture;

    private World world;

    private Body body;

    private Fixture fixture;

    public CrateEntity(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();

        // Defino a altura como -1
        def.position.set(position.x,position.y+0.5f);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(box, 1);
        box.dispose();

        setSize(0.7f*PIXELS_IN_METER, 0.7f*PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // No Scene2D, a origem dos objetos é no canto inferior esquerdo, porém no Box2D, a
        // origem é no centro do objeto. Por isso, quando vamos desenhar, temos que realizar
        // esse ajuste. Nesse caso, como o player tem 1 x 1 metros, vou descontar meio metro
        // da origem, tanto em x quanto em y. Realizo esse calculo antes de converter em pixels.
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Responsável pelo dispose.
     */
    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
