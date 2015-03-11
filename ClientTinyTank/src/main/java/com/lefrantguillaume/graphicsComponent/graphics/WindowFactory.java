package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.WindowConfig;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.SlickException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class WindowFactory {

    public static AppGameContainer windowFactory(List<Observer> observers, Class module, WindowConfig configs, boolean mode) throws SlickException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class myClass = List.class;
        return new AppGameContainer((BasicGame) module.getDeclaredConstructor(myClass).newInstance(observers), configs.getSizeX(), configs.getSizeY(), mode);
    }
}
