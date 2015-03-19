package com.lefrantguillaume;

import com.lefrantguillaume.master.Master;
import org.newdawn.slick.SlickException;

/**
 * Created by andres_k on 10/03/2015.
 */
public class Main {

    public static void main(String args[]) {
        try {
            Master launcher = new Master();
            launcher.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
