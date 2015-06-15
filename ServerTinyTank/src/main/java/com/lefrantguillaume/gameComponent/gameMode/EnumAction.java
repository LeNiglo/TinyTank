package com.lefrantguillaume.gameComponent.gameMode;

import com.lefrantguillaume.gameComponent.EnumCollision;

/**
 * Created by andres_k on 13/05/2015.
 */
public enum EnumAction {
    NOTHING,
    KILL,
    IN,
    OUT;

    public static EnumAction getEnumByOther(EnumCollision type){
        if (type == EnumCollision.IN){
            return EnumAction.IN;
        } else if (type == EnumCollision.OUT){
            return EnumAction.OUT;
        }
        return EnumAction.NOTHING;
    }
}
