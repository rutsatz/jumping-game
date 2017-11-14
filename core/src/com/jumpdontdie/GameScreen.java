package com.jumpdontdie;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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

    private Sound jumpSound, dieSound;

    private Music bgMusic;

    public GameScreen(final MainGame game) {
        super(game);

        jumpSound = game.getManager().get("jump.wav");
        dieSound = game.getManager().get("die.wav");
        bgMusic = game.getManager().get("song.wav");

        stage = new Stage(new FitViewport(640, 360));
        world = new World(new Vector2(0, -10), true);

        world.setContactListener(new ContactListener() {

            // Verifica se os objetos passados por parâmetro colidiram.
            private boolean areCollided(Contact contact, Object userA, Object userB) {
                return (contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB)) ||
                        (contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA));
            }

            @Override
            public void beginContact(Contact contact) {
                if (areCollided(contact, "player", "floor")) {
                    // Não permite múltiplos saltos.
                    player.setJumping(false);

                    // Se player mentem o botão pressionado, diz para pular novamente.
                    if (Gdx.input.isTouched()) {
                        jumpSound.play();
                        player.setMustJump(true);
                    }
                }

                if (areCollided(contact, "player", "crate")) {
                    if (player.isAlive()) {

                        player.setAlive(false);
                        bgMusic.stop();
                        dieSound.play();

                        // Actions são animações usadas para animar objetos.
                        // Temos a sequence que executa várias funções em sequencia,
                        // como se fossem callbacks.
                        // Temos também a Action.run, que executa um código.
                        // Assim, criamos duas Action.run, uma para dar um delay após
                        // a morte do player
                        // e outra para trocar para a tela de gameover. Após, passamos
                        // os dois para a sequence rodar em sequencia.
                        stage.addAction(
                                Actions.sequence(
                                        Actions.delay(1.5f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                game.setScreen(game.gameOverScreen);
                                            }
                                        })
                                )
                        );

                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

    }

    @Override
    public void show() {
        Texture playerTexture = game.getManager().get("Run__000.png");
        Texture floorTexture = game.getManager().get("floor.png");
        Texture crateTexture = game.getManager().get("Crate.png");

        player = new PlayerEntity(world, playerTexture, new Vector2(1.5f, 1.5f));

        floorList.add(new FloorEntity(world, floorTexture, new Vector2(10, 1), 1000));
        floorList.add(new FloorEntity(world, floorTexture, new Vector2(12, 2), 10));
        floorList.add(new FloorEntity(world, floorTexture, new Vector2(30, 2), 10));
        crateList.add(new CrateEntity(world, crateTexture, new Vector2(6, 1)));
        crateList.add(new CrateEntity(world, crateTexture, new Vector2(18, 1)));
        crateList.add(new CrateEntity(world, crateTexture, new Vector2(35, 2)));
        crateList.add(new CrateEntity(world, crateTexture, new Vector2(50, 1)));

        stage.addActor(player);

        for (FloorEntity floor : floorList) {
            //System.out.println(floor.getY());
            stage.addActor(floor);
        }
        for (CrateEntity crate : crateList) {
            stage.addActor(crate);
        }

        bgMusic.setVolume(0.75f);
        bgMusic.setLooping(true);
        bgMusic.play();
    }

    @Override
    public void hide() {
        bgMusic.stop();


        player.detach();
        player.remove();
        for (FloorEntity floor : floorList) {
            floor.detach();
            floor.remove();
        }
        for (CrateEntity crate : crateList) {
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

        // Move a camera.
        // Para acompanhar o player, pego a velocidade dele por segundo e multiplico por
        // delta para ajustar para microsegundos. Como é em pixels, converto de metroas para
        // pixels, pois a velocidade do player está em metros.
        // Somente move se o player avançou mais de 100px e está vivo.
        if (player.getX() > 100 && player.isAlive()) {
            stage.getCamera().translate(Constants.PLAYER_SPEED * delta * Constants.PIXELS_IN_METER, 0, 0);
        }

        if (Gdx.input.justTouched()) {
            jumpSound.play();
            player.jump();
        }

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
