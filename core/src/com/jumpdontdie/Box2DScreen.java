package com.jumpdontdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by raffa on 01/11/17.
 */

public class Box2DScreen extends BaseScreen {
    public Box2DScreen(MainGame game) {
        super(game);
    }

    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    // No Box2D, usamos Body para representar o objeto. Ele nos fornece alguns métodos.
    private Body run000Body, floorBody, crateBody;

    // Fixture do player e do solo.
    private Fixture run000Fixture, floorFixture, crateFixture;

    // indica se o player precisa pular, se está pulando e se está vivo.
    private boolean mustJump, isJumping, playerIsAlive = true;

    @Override
    public void show() {

        // Cria novo mundo do Box2D
        // Como o mundo real, ele possui gravidade, física, colisão.
        // O primeiro parametro define um vetor com a força da gravidade.
        world = new World(new Vector2(0, -10), true);

        // Cria um debug para ver as bordas no Box2D
        renderer = new Box2DDebugRenderer();

        // Cria uma camera Ortográfica
        // O primeiro e segundo parametros sao largura e altura.
        // Como o Box2D trabalha em metros, faço uma regra de três para fazer um ajuste.
        // (Para ajustar minha viewport)
        // Defino os metros máximos de altura e faço uma regra de 3 para a largura.
        // Como a maioria das telas é 16/9 (Widescreen), uso o resultado dessa divisão.
        // 16/9=7,11. Nesse exemplo, multiplicamos ambos por 2 (32 e 18)
//        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(16, 9);
        // Move a camera (viewport) 1 metro para baixo, para não ver tanto o solo.
        camera.translate(0, 1);

        // permite adicionar listener para quando forem detectadas colisões.
        // Nos listeners preciso usar variáveis para controlar as ações que devem ser
        // feitas, e implementá-las no método render.
        world.setContactListener(new ContactListener() {
            /**
             * Começou o contato com o solo, então não está mais saltando.
             * @param contact
             */
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

                // se tocou o chão.
                if (fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor")
                        || fixtureA.getUserData().equals("floor") || fixtureB.getUserData().equals("player")) {
                    // Se saltou e ficou pressionando a tela, mantém ele sempre pulando.
                    if (Gdx.input.isTouched()) {
                        mustJump = true;
                    }
                    isJumping = false;
                }

                // Se pexou na caixa.
                if (fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("crate")
                        || fixtureA.getUserData().equals("crate") || fixtureB.getUserData().equals("player")) {
                    playerIsAlive = false;
                }
            }

            /**
             * Terminou o contato com o solo, então está saltando.
             * @param contact
             */
            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
                if (fixtureA == run000Fixture && fixtureB == floorFixture) {
                    isJumping = true;
                }

                // Repete, pois testo as duas possibilidades.
                if (fixtureA == floorFixture && fixtureB == run000Fixture) {
                    isJumping = true;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });


        // Para criar o Body, temos que usar o método createBody do world.
        run000Body = world.createBody(createRun000BodyDef());
        floorBody = world.createBody(createFloorBodyDef());
        crateBody = world.createBody(createCrateBodyDef(1));

        // Precisamos de uma forma para representar o objeto. Podemos criar um shape e
        // passar como parametro, ou podemos usar o FixtureDef e passar como parâmetro.
        // Usar o FixtureDef nos traz opções adicionais de configuração.
        PolygonShape run000Shape = new PolygonShape();

        // Definimos as dimensões do objeto. Atenção: O tamanho é passado em metros
        // e não em pixels.
        run000Shape.setAsBox(0.5f, 0.5f);

        // Cria o Fixture.
        run000Fixture = run000Body.createFixture(run000Shape, 1);

        // Como já usamos o shape, podemos fazer o dispose().
        run000Shape.dispose();

        PolygonShape floorShape = new PolygonShape();
        floorShape.setAsBox(500, 1);
        floorFixture = floorBody.createFixture(floorShape, 1);
        floorShape.dispose();

        crateFixture = createCrateFixture(crateBody);

        // UserData é usado para associar atributos aos meus objetos.
        run000Fixture.setUserData("player");
        floorFixture.setUserData("floor");
        crateFixture.setUserData("crate");

    }

    private BodyDef createCrateBodyDef(float x) {
        BodyDef def = new BodyDef();

        // Defino a altura como -1
        def.position.set(x, 0.5f);

        // Como é o solo e ele fica parado, posso usar StaticBody.
        // Por default, eles são estáticos.
        //def.type = BodyDef.BodyType.StaticBody;

        return def;
    }

    private BodyDef createFloorBodyDef() {
        BodyDef def = new BodyDef();

        // Defino a altura como -1
        def.position.set(0, -1);

        // Como é o solo e ele fica parado, posso usar StaticBody.
        // Por default, eles são estáticos.
        //def.type = BodyDef.BodyType.StaticBody;

        return def;
    }

    /**
     * BodyDef contém os atributos de um Body, como velocidade, posição, etc.
     *
     * @return
     */
    private BodyDef createRun000BodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0, 10);
        // Como está em movimento, uso DynamicBody.
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    /**
     * Cria um vetor de vertices representando um triangulo equilátero
     * e passa para o Box2D desenhar.
     *
     * @param crateBody
     * @return
     */
    private Fixture createCrateFixture(Body crateBody) {
        Vector2[] vertices = new Vector2[3];
        // A posição de cada vértice sempre é calculada em relação ao centro do objeto.
        // Cria o triângulo equilátero.
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(0.5f, -0.5f);
        vertices[2] = new Vector2(0, 0.5f);

        PolygonShape shape = new PolygonShape();
        // Define os vertices da forma.
        shape.set(vertices);

        Fixture fix = crateBody.createFixture(shape, 1);
        shape.dispose();
        return fix;
    }

    @Override
    public void dispose() {
        floorBody.destroyFixture(floorFixture);
        run000Body.destroyFixture(run000Fixture);
        crateBody.destroyFixture(crateFixture);
        // Para o dispose do Body, temos que também chamar o método do world.
        world.destroyBody(run000Body);
        world.destroyBody(floorBody);
        world.destroyBody(crateBody);
        world.dispose();
        renderer.dispose();
    }

    @Override
    public void render(float delta) {

        // Limpa a tela, para evitar efeito fantasma.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (mustJump) {
            mustJump = false;
            jump();
        }

        // Se tocou a tela e não está saltando, deve saltar.
        if (Gdx.input.justTouched() && !isJumping) {
            mustJump = true;
        }

        // verifica se ainda está vivo.
        if (playerIsAlive) {
            // Define a velocidade que o jogador se move. Como somente quero mudar em X,
            // pego e mantenho e mesma velocidade em Y.
            float velocidadeY = run000Body.getLinearVelocity().y;
            run000Body.setLinearVelocity(8, velocidadeY);
        }

        // Calcula a física dos objetos. Sempre deve ser executado antes de renderizar.
        world.step(delta, 6, 2);

        // Atualiza a camera, caso tenhamos feito algum redimensionamento ou
        // coisa parecida.
        camera.update();

        // o segundo parametro é uma matriz 4x4, usada pelo OpenGL para
        // calcular o tamanho que aos objetos devem ser renderizado.
        renderer.render(world, camera.combined);

    }

    private void jump() {
        Vector2 position = run000Body.getPosition();
        run000Body.applyLinearImpulse(0, 5, position.x, position.y, true);
    }

}
