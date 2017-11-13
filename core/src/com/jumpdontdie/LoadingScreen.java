package com.jumpdontdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by raffa on 12/11/17.
 */

public class LoadingScreen extends BaseScreen {

    private Stage stage;

    private Skin skin;

    private Label loading;

    public LoadingScreen(MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/rusty-robot-ui.json"));
        loading = new Label("Loading...", skin);
        loading.setPosition(320 - loading.getWidth() / 2, 180 - loading.getHeight() / 2);

        stage.addActor(loading);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Carrega de modo assincrono.
        // Update retorna verdadeiro quando carregou tudo.
        if (game.getManager().update()) {
            game.finishLoading();
        } else {
            int progess = (int) (game.getManager().getProgress() * 100);
            loading.setText("Loading... " + progess + "%");
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
