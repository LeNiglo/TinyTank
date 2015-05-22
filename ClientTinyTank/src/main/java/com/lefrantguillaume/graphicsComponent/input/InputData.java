package com.lefrantguillaume.graphicsComponent.input;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by andres_k on 22/05/2015.
 */
public class InputData {
    Map<EnumInput, String> availableInput;

    public InputData(String configsFile) throws JSONException {
        this.availableInput = new HashMap<>();
        JSONObject configs = new JSONObject(configsFile);
        Iterator iterator = configs.keys();

        while (iterator.hasNext()) {
            String input = (String) iterator.next();
            String value = configs.getString(input);
            this.availableInput.put(EnumInput.getEnumByValue(input), value);
        }
    }

    public String getInputValue(EnumInput input) {
        return this.availableInput.get(input);
    }

    public String getInputByValue(String value){
        for (Map.Entry entry : this.availableInput.entrySet()){
            if (entry.getValue().equals(value)){
                return ((EnumInput)entry.getKey()).getValue();
            }
        }
        return "";
    }
}
