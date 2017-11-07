package com.jumpdontdie.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.jumpdontdie.Constants.*;

/**
 * Created by raffa on 02/11/17.
 */

public class PlayerEntity extends Actor {

    private Texture texture;

    private World world;

    private Body body;

    private Fixture fixture;

    private boolean alive = true;
    private boolean jumping = false;
    private boolean mustJump = false;

    public PlayerEntity(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();

        // Defino a altura como -1
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(box, 3);
        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

        fixture.setUserData("player");

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

    @Override
    public void act(float delta) {



        // pular se o jogador tocou na tela.
        if (Gdx.input.justTouched() || mustJump) {
            mustJump = false;
            jump();
        }

        // avançar se continua vivo.
        if (alive) {
            float speedY = body.getLinearVelocity().y;
            body.setLinearVelocity(PLAYER_SPEED, speedY);
        }

        // Aplica uma força para baixo, evitando o player saltar muito alto.
        if (jumping) {
            body.applyForceToCenter(0, -IMPULSE_JUMP * 0.5f, true);
        }
    }

    public void jump() {

        if (!isJumping() && alive) {
            setJumping(true);
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);
        }
    }


    /**
     * Responsável pelo dispose.
     */
    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }


    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }


    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    public boolean isMustJump() {
        return mustJump;
    }

    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }
}
