package com.jumpdontdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {

    private AssetManager manager;

    public GameScreen gameScreen;

    public GameOverScreen gameOverScreen;

    public MenuScreen menuScreen;

    public LoadingScreen loadingScreen;

    public AssetManager getManager() {
        return manager;
    }

    @Override
    public void create() {

        manager = new AssetManager();
        manager.load("floor.png", Texture.class);
//        manager.load("overfloor.png", Texture.class);
        manager.load("Crate.png", Texture.class);
        manager.load("Run__000.png", Texture.class);

        manager.load("game-over-pixel-text.png", Texture.class);
        manager.load("skin/rusty-robot-ui.png", Texture.class);

        manager.load("die.wav", Sound.class);
        manager.load("jump.wav", Sound.class);
        manager.load("song.wav", Music.class);

        // carrega de modo sincrono.
//        manager.finishLoading();

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public void finishLoading() {
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }
}
