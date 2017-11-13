package com.jumpdontdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by raffa on 11/11/17.
 */

public class MenuScreen extends BaseScreen {

    private Stage stage;

    private Skin skin;

    private Label titulo;

    private TextButton retry;

    public MenuScreen(final MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/rusty-robot-ui.json"));
        titulo = new Label("HatBoy", skin);
        retry = new TextButton("Play", skin);

        // Adiciona o listener do botão, para identificar o clique.
        retry.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.gameScreen);
            }
        });


        titulo.setPosition(320 - titulo.getWidth() / 2, 320 - titulo.getHeight());
        // aumenta fonte.
        titulo.setFontScale(2, 2);

        retry.setSize(300, 100);
        retry.setPosition(170, 50);
        stage.addActor(retry);
        stage.addActor(titulo);
    }

    @Override
    public void show() {
        // O stage implementa ou extende o input adapter.
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        // Limpa o input processor, pois não estarei vendo a tela e
        // terei o input processor, então pode gerar comportamentos
        // estranhos.
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        stage.draw();
    }
}
