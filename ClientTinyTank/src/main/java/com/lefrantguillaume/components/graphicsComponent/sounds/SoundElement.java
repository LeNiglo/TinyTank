package com.lefrantguillaume.components.graphicsComponent.sounds;

import org.newdawn.slick.Sound;

import java.util.UUID;

/**
 * Created by andres_k on 07/07/2015.
 */
public class SoundElement {
    private Sound sound;
    private String id;
    private EnumSound type;

    public SoundElement(Sound sound, EnumSound type){
        this.sound = sound;
        this.type = type;
        this.id = UUID.randomUUID().toString();
    }

    //GETTER
    public String getId() {
        return this.id;
    }

    public EnumSound getType() {
        return type;
    }

    public Sound getSound(){
        return this.sound;
    }
}
