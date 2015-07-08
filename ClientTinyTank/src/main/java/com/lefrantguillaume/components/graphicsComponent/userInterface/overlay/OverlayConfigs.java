package com.lefrantguillaume.components.graphicsComponent.userInterface.overlay;

import com.lefrantguillaume.utils.tools.StringTools;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by andres_k on 04/07/2015.
 */
public class OverlayConfigs {
    private Map<EnumOverlayElement, boolean[]> availablePreference;
    private JSONObject configPreference;
    private String filePreference;

    private Map<EnumOverlayElement, String> datas;
    private JSONObject configData;
    private String fileData;

    public OverlayConfigs(String filePreference, String fileData) throws JSONException {
        this.availablePreference = new LinkedHashMap<>();
        this.filePreference = filePreference;

        this.datas = new LinkedHashMap<>();
        this.fileData = fileData;

        this.initPreference();
        this.initData();
    }

    private void initPreference() throws JSONException {
        this.configPreference = new JSONObject(StringTools.readFile(this.filePreference));
        Iterator iterator = this.configPreference.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            JSONArray array = this.configPreference.getJSONArray(key);
            boolean[] values = new boolean[array.length()];
            for (int i = 0; i < array.length(); ++i) {
                values[i] = array.getBoolean(i);
            }
            this.availablePreference.put(EnumOverlayElement.getEnumByValue(key), values);
        }
    }

    private void initData() throws JSONException {
        this.configData = new JSONObject(StringTools.readFile(this.fileData));
        Iterator iterator = this.configData.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = this.configData.getString(key);
            this.datas.put(EnumOverlayElement.getEnumByValue(key), value);
        }
    }

    // GETTERS
    public Map<EnumOverlayElement, boolean[]> getAvailablePreference() {
        return this.availablePreference;
    }

    public boolean[] getPreferenceValues(EnumOverlayElement type) {
        return this.availablePreference.get(type);
    }

    public boolean getPreferenceValue(EnumOverlayElement type, int position){
        if (this.availablePreference.containsKey(type)){
            if (position < this.availablePreference.get(type).length){
                return this.availablePreference.get(type)[position];
            }
        }
        return true;
    }

    public String getData(EnumOverlayElement type){
        if (this.datas.containsKey(type)){
            return this.datas.get(type);
        }
        return "";
    }

    // SETTERS
    public boolean setAvailableInput(EnumOverlayElement type, boolean[] value) {
        if (this.availablePreference.containsKey(type)) {
            this.availablePreference.replace(type, value);
            JSONArray array = new JSONArray();
            for (int i = 0; i < value.length; ++i) {
                array.put(value[i]);
            }
            try {
                this.configPreference.put(type.getValue(), array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringTools.writeInFile(this.filePreference, this.configPreference.toString());
            return true;
        }
        return false;
    }
}
