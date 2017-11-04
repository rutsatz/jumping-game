package com.jumpdontdie.scene2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by rutsa on 20/10/2017.
 */

public class ActorPlayer extends Actor {

    private float escala = (float) 0.30; // Reduz 70 %
    private float height;
    private float width;

    private Texture player;

    private boolean alive;

    public ActorPlayer(Texture player){
        this.player = player;
        this.height = player.getHeight() * escala;
        this.width = player.getWidth() * escala;
        setSize(width, height);

        this.alive = true;

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //batch.draw(player, getX(), getY());
        batch.draw(player, getX(), getY(), width, height);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
