package com.lefrantguillaume.Utils.tools;

import org.newdawn.slick.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andres_k on 03/07/2015.
 */
public class ColorTools {
    private static Map<Colors, Color> colors;

    public enum Colors {
        TRANSPARENT_GREYBLUE,
        TRANSPARENT_GREY,
        TRANSPARENT_YELLOW,
        TRANSPARENT_GREEN,
        TRANSPARENT_BLUE,
        TRANSPARENT_RED
    }

    public static void init(){
        colors = new HashMap<>();
        colors.put(Colors.TRANSPARENT_GREYBLUE, new Color(0.1f, 0.2f, 0.3f, 0.5f));
        colors.put(Colors.TRANSPARENT_GREY, new Color(0.6f, 0.6f, 0.6f, 0.5f));
        colors.put(Colors.TRANSPARENT_RED, new Color(1f, 0f, 0f, 0.5f));
        colors.put(Colors.TRANSPARENT_GREEN, new Color(0f, 1f, 0f, 0.5f));
        colors.put(Colors.TRANSPARENT_BLUE, new Color(0f, 0f, 1f, 0.5f));
        colors.put(Colors.TRANSPARENT_YELLOW, new Color(1f, 1f, 0f, 0.5f));
    }

    public static Color get(Colors color){
        if (colors == null){
            init();
        }
        return colors.get(color);
    }

    public static boolean compareColor(Color color, Colors type){
        Color compare = colors.get(type);

        if (compare.a == color.a &&
                compare.r == color.r &&
                compare.g == color.g &&
                compare.b == color.b){
            return true;
        }
        return false;
    }

    public static Color getGreenOrRed(boolean choice){
        if (choice == true){
            return colors.get(Colors.TRANSPARENT_GREEN);
        } else {
            return  colors.get(Colors.TRANSPARENT_RED);
        }
    }
}
