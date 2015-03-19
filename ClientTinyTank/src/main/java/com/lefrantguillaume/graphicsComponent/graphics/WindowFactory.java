package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowFactory {

    public static AppGameContainer windowFactory(List<Observer> observers, Class module, Object controller, boolean mode) throws SlickException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return new AppGameContainer((StateBasedGame) module.getDeclaredConstructor(List.class, Object.class).newInstance(observers, controller), WindowConfig.getSizeX(), WindowConfig.getSizeY(), mode);
    }
}
