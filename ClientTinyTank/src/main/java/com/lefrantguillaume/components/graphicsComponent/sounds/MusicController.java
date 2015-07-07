package com.lefrantguillaume.components.graphicsComponent.sounds;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andres_k on 07/07/2015.
 */
public class MusicController {
    private static Map<EnumSound, Music> musics;
    private static boolean needInit = true;
    private static float pitch;
    private static float volume;


    public static void init() throws SlickException {
        if (needInit == true) {
            musics = new HashMap<>();
            musics.put(EnumSound.BACKGROUND, new Music(EnumSound.BACKGROUND.getPath()));
            musics.put(EnumSound.SELECT, new Music(EnumSound.SELECT.getPath()));
            pitch = 1.0f;
            volume = 1.0f;
            needInit = false;
        }
    }

    public static boolean play(EnumSound value) {
        if (needInit == false) {
            if (musics.containsKey(value)){
                if (musics.get(value).playing()){
                    musics.get(value).resume();
                } else {
                    musics.get(value).play(pitch, volume);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean loop(EnumSound value) {
        if (needInit == false) {
            if (musics.get(value).playing()){
                musics.get(value).resume();
            } else {
                musics.get(value).loop(pitch, volume);
            }
            return true;
        }
        return false;
    }

    public static boolean stop(EnumSound value){
        if (needInit == false) {
            if (musics.containsKey(value)){
                musics.get(value).stop();
                return true;
            }
        }
        return false;
    }

    public static void changeVolume(float value){
        volume = value;

        for (Map.Entry<EnumSound, Music> entry : musics.entrySet()){
            entry.getValue().setVolume(volume);
        }
    }
}
