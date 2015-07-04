package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.Utils.tools.StringTools;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by andres_k on 04/07/2015.
 */
public class OverlayData {
    private Map<EnumOverlayElement, boolean[]> availableData;
    private JSONObject configs;
    private String file;

    public OverlayData(String file) throws JSONException {
        this.availableData = new LinkedHashMap<>();
        this.configs = new JSONObject(StringTools.readFile(file));
        this.file = file;

        Iterator iterator = this.configs.keys();
        while (iterator.hasNext()) {
            String input = (String) iterator.next();
            JSONArray array = this.configs.getJSONArray(input);
            boolean[] values = new boolean[array.length()];
            for (int i = 0; i < array.length(); ++i) {
                values[i] = array.getBoolean(i);
            }
            this.availableData.put(EnumOverlayElement.getEnumByValue(input), values);
        }
    }

    // GETTERS
    public Map<EnumOverlayElement, boolean[]> getAvailableData() {
        return this.availableData;
    }

    public boolean[] getValues(EnumOverlayElement type) {
        return this.availableData.get(type);
    }

    public boolean getValue(EnumOverlayElement type, int position){
        if (this.availableData.containsKey(type)){
            if (position < this.availableData.get(type).length){
                return this.availableData.get(type)[position];
            }
        }
        return true;
    }

    // SETTERS
    public boolean setAvailableInput(EnumOverlayElement type, boolean[] value) {
        if (this.availableData.containsKey(type)) {
            this.availableData.replace(type, value);
            JSONArray array = new JSONArray();
            for (int i = 0; i < value.length; ++i) {
                array.put(value[i]);
            }
            try {
                this.configs.put(type.getValue(), array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringTools.writeInFile(this.file, this.configs.toString());
            return true;
        }
        return false;
    }
}
