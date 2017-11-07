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

public class FloorEntity extends Actor {

    private Texture texture;

    private World world;

    private Body body, leftBody;

    private Fixture fixture, leftFixture;

    public FloorEntity(World world, Texture texture, Vector2 position, float width) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();

        // Defino a altura como -1
//        def.position.set(position.x - 0.5f , position.y-0.5f);
        // Ajustar o x, pq axo q o tamanho tá com problema.
        def.position.set(position.x, position.y - 0.5f);
//        System.out.println(position.x);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, 0.5f);
//        box.setAsBox(width, 0.5f);
//        System.out.println(width / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("floor");
        box.dispose();


        // Utiliza uma borda transparente na esquerda para quando o usuário
        // colidir, ele tbm morrer.
        BodyDef leftDef = new BodyDef();
        leftDef.position.set(position.x - 0.55f, position.y - 0.55f);
        leftDef.type = BodyDef.BodyType.StaticBody; // Default é static
        leftBody = world.createBody(leftDef);

        PolygonShape leftBox = new PolygonShape();
        leftBox.setAsBox( 0.02f, 0.45f); // cria a bordinha transparente
        leftFixture = leftBody.createFixture(leftBox, 1);
        leftFixture.setUserData("crate");
        leftBox.dispose();

        setSize(width * PIXELS_IN_METER, PIXELS_IN_METER);
//        setSize(1 * PIXELS_IN_METER, PIXELS_IN_METER);
//        System.out.println(getHeight());

        setPosition(position.x * PIXELS_IN_METER, (position.y - 1) * PIXELS_IN_METER);
//        setPosition(1*PIXELS_IN_METER,(position.y - 1) * PIXELS_IN_METER);
//        System.out.println(getY());



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
