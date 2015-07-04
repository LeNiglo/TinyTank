package com.lefrantguillaume.components.gameComponent.gameObject.tanks.equipment;

import com.lefrantguillaume.components.gameComponent.gameObject.EnumGameObject;

/**
 * Created by andres_k on 04/07/2015.
 */
public class TankBox {
    private EnumGameObject box;

    public TankBox(EnumGameObject box){
        this.box = box;
    }

    public TankBox(TankBox tankBox){
        this.box = tankBox.box;
    }

    // GETTERS

    public EnumGameObject getBox(){
        return this.box;
    }
}
