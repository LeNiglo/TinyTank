package com.lefrantguillaume.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by leniglo on 23/05/15.
 */
public class UserInterface {
    private List<UIComponent> uiComponents;
    private UUID focusedElement = null;

    public UserInterface() {
        this.uiComponents = new ArrayList<>();
    }


}
