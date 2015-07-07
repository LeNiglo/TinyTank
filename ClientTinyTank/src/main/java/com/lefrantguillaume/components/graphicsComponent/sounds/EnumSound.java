package com.lefrantguillaume.components.graphicsComponent.sounds;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;

/**
 * Created by andres_k on 07/07/2015.
 */
public enum EnumSound {
    NOTHING(""),
    BACKGROUND("assets/old/music/intro.ogg"),
    SELECT("assets/old/music/select.ogg"),

    ROCKET(EnumGameObject.ROCKET, "assets/old/sound/rocketHit.wav"),
    LASER(EnumGameObject.LASER, "assets/old/sound/laserHit.wav"),
    MACHINE_GUN(EnumGameObject.MACHINE_GUN, "assets/old/sound/machineGun.wav"),
    EXPLOSION_MINE(EnumGameObject.MINE, "assets/old/sound/explosionMine.wav"),
    EXPLOSION_TANK("assets/old/sound/explosionTank.wav");

    private String path;
    private EnumGameObject object;

    EnumSound(String path){
        this.path = path;
        this.object = EnumGameObject.NULL;
    }

    EnumSound(EnumGameObject object, String path){
        this.path = path;
        this.object = object;
    }

    public String getPath(){
        return this.path;
    }

    public EnumGameObject getObject(){
        return this.object;
    }

    public static EnumSound getSound(EnumGameObject object){
        EnumSound[] sounds = EnumSound.values();

        for (int i = 0; i < sounds.length; ++i){
            if (sounds[i].getObject() == object){
                return sounds[i];
            }
        }
        return NOTHING;
    }
}
