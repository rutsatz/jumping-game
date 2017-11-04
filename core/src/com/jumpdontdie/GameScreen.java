package com.jumpdontdie;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jumpdontdie.entities.CrateEntity;
import com.jumpdontdie.entities.FloorEntity;
import com.jumpdontdie.entities.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raffa on 02/11/17.
 */

public class GameScreen extends BaseScreen {
    private Stage stage;

    private World world;

    private PlayerEntity player;

    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();

    private List<CrateEntity> crateList = new ArrayList<CrateEntity>();

    public GameScreen(MainGame game) {
        super(game);
        stage = new Stage(new FitViewport(640, 360));
        world = new World(new Vector2(0, -10), true);
    }

    @Override
    public void show() {
        Texture playerTexture = game.getManager().get("Run__000.png");
        Texture floorTexture = game.getManager().get("floor.png");
        Texture crateTexture = game.getManager().get("Crate.png");

        player = new PlayerEntity(world, playerTexture, new Vector2(1.5f, 1.5f));

        floorList.add(new FloorEntity(world,floorTexture, new Vector2(0,1), 1000));
        crateList.add(new CrateEntity(world, crateTexture, new Vector2(6,1)));

        stage.addActor(player);

        for(FloorEntity floor : floorList){
            //System.out.println(floor.getY());
            stage.addActor(floor);
        }
        for (CrateEntity crate : crateList){
            stage.addActor(crate);
        }
    }

    @Override
    public void hide() {
        player.detach();
        player.remove();
        for(FloorEntity floor : floorList){
            floor.detach();
            floor.remove();
        }
        for(CrateEntity crate : crateList){
            crate.detach();
            crate.remove();
        }
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

        world.step(delta, 6, 2);

        // renderiza.
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }
}
