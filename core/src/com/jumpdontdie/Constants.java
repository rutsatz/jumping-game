package com.jumpdontdie;

/**
 * Created by raffa on 02/11/17.
 */

public class Constants {

    /**
     * Representa os pixels necessários para representar um metro.
     * Isso ocorre pq o Scene2D trabalha em pixels e o Box2D trabalha em metros.
     * Então, se minha tela tem 360 px de altura e defino q Box2D tem 4 metros de altura,
     * preciso fazer a equivalência entre os dois. Dessa forma, 360/4 = 90. Logo,
     * 1 metro equivale a 90 pixels.
     */
    public static final float PIXELS_IN_METER = 90f;

}
