package com.lefrantguillaume.components.graphicsComponent.input;

import com.lefrantguillaume.Utils.tools.StringTools;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by andres_k on 22/05/2015.
 */
public class InputData {
    private Map<EnumInput, String> availableInput;
    private JSONObject configs;
    private String file;

    public InputData(String file) throws JSONException {
        this.availableInput = new LinkedHashMap<>();
        this.configs = new JSONObject(StringTools.readFile(file));
        this.file = file;

        Iterator iterator = this.configs.keys();
        while (iterator.hasNext()) {
            String input = (String) iterator.next();
            String value = this.configs.getString(input);
            this.availableInput.put(EnumInput.getEnumByValue(input), value);
        }
    }

    // GETTERS
    public Map<EnumInput, String> getAvailableInput(){
        return this.availableInput;
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

    // SETTERS

    public void setAvailableInput(EnumInput type, String value){
        if (this.availableInput.containsKey(type)){
            this.availableInput.replace(type, value);
//            this.configs.remove(type.getValue());
            try {
                this.configs.put(type.getValue(), value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringTools.writeInFile(this.file, this.configs.toString());
        }
    }
}
