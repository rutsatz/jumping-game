package com.jumpdontdie.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by raffa on 31/10/17.
 */

public class ActorCrate extends Actor {
    private float escala = (float) 0.70; // Reduz 30 %
    private float height;
    private float width;

    private TextureRegion crate;

    public ActorCrate(TextureRegion crate){
        this.crate = crate;
        this.height = crate.getRegionHeight() * escala;
        this.width = crate.getRegionWidth() * escala;

        setSize(width, height);
    }

    @Override
    public void act(float delta) {
        setX(getX() - 250 * delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(crate, getX(), getY(), width, height);
    }
}
