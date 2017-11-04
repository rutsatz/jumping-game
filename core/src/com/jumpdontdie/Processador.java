package com.jumpdontdie;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by rutsa on 19/10/2017.
 *
 * InputProcessor usa funções de callback e o libgdx que decide quando
 * acionar os métodos que implementamos. (Muito mais rápido q ficar fazer
 * ifs 30-60 vezes por segundo no render.)
 *
 * No caso, vamos estender de InputAdapter, pois ele continua sendo um InputProcessor,
 * mas nele nós só precisamos implementar os métodos que vamos usar.
 *
 * Os métodos sempre retornam boolean pois o libGDX usa o retorno para gerenciar
 * e distribuir recursos e se o método retorna false, dizemos q ele não é usado, então
 * pode liberar seus recursos. Por isso, quando implementamos um método, sempre
 * temos que retornar true.
 */

public class Processador extends InputAdapter {

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("Tocou na posição " + screenX + "," + screenY);
        System.out.println("Usou o dedo " + pointer + " e o botao " + button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }
}
