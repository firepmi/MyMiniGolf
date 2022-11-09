package com.wddonline.minigolf.myminigolfscorecard;

import org.json.JSONArray;
import org.json.JSONException;

public interface GetDataInterface {
    void onLoadedData(JSONArray resultArray) throws JSONException;
    void onFailedLoadingData();
}