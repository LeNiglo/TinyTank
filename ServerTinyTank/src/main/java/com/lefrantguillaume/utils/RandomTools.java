package com.lefrantguillaume.utils;

import java.util.Random;

/**
 * Created by andres_k on 31/03/2015.
 */
public class RandomTools {
    private static Random random = new Random();

    public static int getInt() {
        return random.nextInt();
    }

    public static int getInt(int bound) {
        return random.nextInt(bound);
    }

    public static float getFloat() {
        return random.nextFloat();
    }

    public static boolean getBoolean() {
        return random.nextBoolean();
    }

}
