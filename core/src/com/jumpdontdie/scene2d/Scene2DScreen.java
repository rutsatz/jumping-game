package com.jumpdontdie.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jumpdontdie.BaseScreen;
import com.jumpdontdie.MainGame;
import com.jumpdontdie.scene2d.ActorCrate;
import com.jumpdontdie.scene2d.ActorPlayer;

/**
 * Created by rutsa on 20/10/2017.
 */

public class Scene2DScreen extends BaseScreen {

    public Scene2DScreen(MainGame game) {
        super(game);
        texturePlayer = new Texture("Run__000.png");


        textureCrate = new Texture("Crate.png");
        regionCrate = new TextureRegion(textureCrate);
    }

    private Stage stage;

    private ActorPlayer player;
    private ActorCrate crate;

    private Texture texturePlayer, textureCrate;

    private TextureRegion regionCrate;

    @Override
    public void show() {

        stage = new Stage();
        stage.setDebugAll(true);

        player = new ActorPlayer(texturePlayer);

        crate = new ActorCrate(regionCrate);

        stage.addActor(player);
        stage.addActor(crate);

        player.setPosition(20, 100);
        crate.setPosition(400, 100);


    }

    @Override
    public void hide() {

        stage.dispose();


    }

    @Override
    public void render(float delta) {

        // Desenha o fundo azul, para não precisar carregar uma textura como céu.
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);

        // Antes de desenhar temos que limpar a memoria da placa,
        // para evitar fantasmas. Nesse caso, usamos o gl (OpenGL)
        // para limpar a tela antes de desenhar novamente.
        // No parametro, dizemos o que limpar, q nesse caso são
        // o tampão de bits (todas as cores q tem na tela)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Atualiza a tela.
        stage.act();

        verificarColisoes();

        // renderiza.
        stage.draw();

    }

    private void verificarColisoes() {


        if (player.isAlive() &&
                player.getX() + player.getWidth() > crate.getX()) {
            System.out.println("Colisão.");
            player.setAlive(false);
        }
    }

    @Override
    public void dispose() {
        texturePlayer.dispose();
        textureCrate.dispose();
    }
}
