package com.lefrantguillaume.graphicsComponent.graphics;

import com.lefrantguillaume.Utils.configs.WindowConfig;
import com.lefrantguillaume.gameComponent.animations.AnimatorData;
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

    public static AppGameContainer windowFactory(List<Observer> observers, Class module, WindowConfig configs, AnimatorData animatorData, Object controller, boolean mode) throws SlickException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return new AppGameContainer((BasicGame) module.getDeclaredConstructor(List.class, AnimatorData.class, Object.class).newInstance(observers, animatorData, controller), configs.getSizeX(), configs.getSizeY(), mode);
    }
}
