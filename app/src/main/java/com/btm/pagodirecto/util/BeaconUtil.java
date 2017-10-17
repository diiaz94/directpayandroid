package com.btm.pagodirecto.util;

import com.btm.pagodirecto.domain.beacons.RegisteredBeacons;
import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by ealvarado on 05/06/17.
 */

public class BeaconUtil {

    private static final String TAG_REGISTERED_BEACONS = "REGISTERED_BEACONS";
    public static final String TAG_PROVIDER_ID = "providerId";
    public static final String TAG_BEACON_ID = "beaconId";

    private static JSONArray registeredBeacons = null;

    public static void setRegisteredBeacons(){
        List<RegisteredBeacons> beaconList = null;

        //Mint beacon
        beaconList.add(new RegisteredBeacons("eddd1ebeac04e5defa99:ccf165e85832",
                                             "ccf165e85832",
                                             "eddd1ebeac04e5defa99",
                                             "Caracas",
                                             "EST",
                                             "Estimote"));

        //Blueberry beacon
        beaconList.add(new RegisteredBeacons("eddd1ebeac04e5defa99:d48a9c6a9c90",
                "d48a9c6a9c90",
                "eddd1ebeac04e5defa99",
                "Caracas",
                "EST",
                "Estimote"));

        //Ice beacon
        beaconList.add(new RegisteredBeacons("eddd1ebeac04e5defa99:d165024a8efc",
                "d165024a8efc",
                "eddd1ebeac04e5defa99",
                "Caracas",
                "EST",
                "Estimote"));
        BeaconUtil.saveRegisteredBeaconsList(beaconList);
    }

    public static void saveRegisteredBeaconsList(List<RegisteredBeacons> beaconsList){
        String registeredBeacons = new Gson().toJson(beaconsList);
        Util.saveInSharedPreferences(TAG_REGISTERED_BEACONS,registeredBeacons);
    }

    public static String getRegisteredBeacons(){
        return Util.getFromSharedPreferences(TAG_REGISTERED_BEACONS);
    }

    public static boolean getBeaconIndexInList(Beacon beacon){
        if(registeredBeacons == null){
            try {
                registeredBeacons = new JSONArray(getRegisteredBeacons());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0 ; i < registeredBeacons.length(); i++){
            if (getFromJsonArray(registeredBeacons,i, TAG_PROVIDER_ID).equalsIgnoreCase(beacon.getId1().toString().substring(2)) &&
                getFromJsonArray(registeredBeacons,i, TAG_BEACON_ID).equalsIgnoreCase(beacon.getId2().toString().substring(2))) {

                return true;
            }
        }
        return false;

    }

    private static String getFromJsonArray(JSONArray array , int position, String data){
        try {
            return array.getJSONObject(position).getString(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private JSONArray getRegisteredListBeacons(){
        try {
            registeredBeacons = new JSONArray(Util.getFromSharedPreferences(TAG_REGISTERED_BEACONS));
            return registeredBeacons;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
