package test;

import org.lwjgl.input.Mouse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class testClass extends BasicGame {
    private String test = "nothing";

    public testClass(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        int x = Mouse.getX();
        int y = Mouse.getY();
        test = "(" + x + ", " + y + ")";
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawString(test, 50, 50);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new testClass("Simple Slick Game"));
            appgc.setDisplayMode(640, 480, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(testClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}