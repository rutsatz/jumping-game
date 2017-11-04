package com.jumpdontdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {

    private AssetManager manager;

    public AssetManager getManager(){
        return manager;
    }

    @Override
    public void create() {

        manager = new AssetManager();
        manager.load("floor.png", Texture.class);
//        manager.load("overfloor.png", Texture.class);
        manager.load("Crate.png", Texture.class);
        manager.load("Run__000.png", Texture.class);
        // carrega de modo sincrono.
        manager.finishLoading();

        setScreen(new GameScreen(this));
    }
}