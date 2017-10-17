package com.btm.pagodirecto.domain.beacons;

import android.util.Log;

import com.btm.pagodirecto.util.BeaconUtil;
import org.altbeacon.beacon.Beacon;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.UUID;

/**
 * Created by ealvarado on 06/09/16.
 */
public class Beacons {

    private String providerId;
    private String beaconId;
    private int rssi;
    private double distance;
    private String bluetoothAddress;
    private double battery;
    private double temperature;
    private boolean compatibility;
    private int id;
    private String location;
    private String correlationId;
    private String regionStatus;
    private boolean arrive = false;
    private boolean insideRegion = false;
    private boolean registerStatus = false;
    private boolean checkStatus = false;
    private boolean activated;
    private long inTime = 0;
    private long outTime = 0;
    private long visitTime = 0;
    private List<String> tags;
    private static JSONArray registeredBeacons = null;

    public boolean isArrive() {
        return arrive;
    }

    public void setArrive(boolean arrive) {
        this.arrive = arrive;
    }

    public void setProviderId(String providerId) {
        // Remove "0x" from Hex String and make UpperCase
        this.providerId = providerId.substring(2).toUpperCase();
    }

    public String getProviderId() {
        return this.providerId;
    }

    public void setBeaconId(String beaconId) {
        // Remove "0x" from Hex String and make UpperCase
        this.beaconId = beaconId.substring(2).toUpperCase();
    }

    public String getBeaconId() {
        return this.beaconId;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setBluetoothAddress(String bluetoothAddress){
        this.bluetoothAddress = bluetoothAddress;
    }

    public String getBluetoothAddress(){
        return this.bluetoothAddress;
    }

    public void setDistance (double distance) {
        this.distance =  distance;
    }

    public double getDistance() {
        return Double.parseDouble(String.valueOf(this.distance));
    }

    public double getAccurateDistance(){

        /*One of the simplest formulas for calculating distance (found it in this paper657) is:

        RSSI = -20 * logd + TxPower (where d = distance)

        Which gives you this:

        d = 10 ^ ((TxPower - RSSI) / 20)*/

        double txPower = (this.rssi - (-18 * Math.log(this.distance)));
        double distance = Math.pow(10,((txPower - this.rssi) /20));

        return distance;
    }


    public void setBattery (float battery) {
        this.battery =  battery/32;
    }

    public double getBattery() {
        return this.battery;
    }

    public void setTemperature (long temperature) {
        long unsignedTemp = temperature >> 8;
        double temp = unsignedTemp > 128 ?
                unsignedTemp - 256 :
                unsignedTemp +(temperature & 0xff)/256.0;
        this.temperature =  temperature;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public String getUniqueId(){
        return getProviderId() + getBeaconId();
    }

    public void setCompatibility (boolean compatibility) {
        this.compatibility =  compatibility;
    }

    public boolean getCompatibility(){
        return this.compatibility;
    }

    public void setRegisterStatus (boolean registerStatus) {
        this.registerStatus =  registerStatus;
    }
    
    public boolean getRegisterStatus(){
        return this.registerStatus;
    }

    public void setTags(List<String> tags){
        this.tags = tags;
    }

    public List<String> getTags(){;
        return this.tags;
    }

    public void setLocation(String location) {
        this. location = location;
    }

    public String getLocation(){
        return this.location;
    }

    public void setCheckStatus(boolean checkStatus){
        this.checkStatus = checkStatus;
    }

    public boolean getCheckStatus(){
        return this.checkStatus;
    }

    public void setInTime(long inTime){
        this.inTime = inTime;
    }

    public long getInTime(){
        return inTime;
    }

    public void setOutTime(long outTime){
        this.outTime = outTime;
    }

    public long getOutTime(){
        return outTime;
    }

    public void setVisitTime(long visitTime){
        this.visitTime = visitTime;
    }

    public long getVisitTime(){
        return visitTime;
    }
    public void setCorrelationId(String correlationId){
        this.correlationId = correlationId;
    }

    public String getCorrelationId(){
        return correlationId;
    }

    public void setInsideRegion(boolean insideRegion){
        this.insideRegion = insideRegion;
    }

    public boolean getInsideRegion(){
        return this.insideRegion;
    }

    public void setRegionStatus(String regionStatus){
        this.regionStatus = regionStatus;
    }

    public String getRegionStatus(){
        return this.regionStatus;
    }

    public Beacons updateBeaconValues(Beacon beacon){
        setProviderId(beacon.getId1().toString());
        setBeaconId(beacon.getId2().toString());
        setDistance(beacon.getDistance());
        setRssi(beacon.getRssi());
        setBluetoothAddress(beacon.getBluetoothAddress());
        return this;
    }

    public boolean isRegistered(){
        initializeRegisteredBeacons();
        if(!this.getCheckStatus()){
            setCheckStatus(true);
            if(isRegisteredInDataBase()){
                this.setRegisterStatus(true);
                return true;
            }
        }
        return this.getRegisterStatus();
    }

    private boolean isRegisteredInDataBase(){
        for(int i = 0; i < registeredBeacons.length(); i++){
            if(this.getProviderId().equalsIgnoreCase(getFromRegisteredList(i,BeaconUtil.TAG_PROVIDER_ID)) &&
               this.getBeaconId().equalsIgnoreCase(getFromRegisteredList(i,BeaconUtil.TAG_BEACON_ID))){
                return true;
            }
        }
        return false;
    }

    private void initializeRegisteredBeacons(){
        if(registeredBeacons == null){
            try {
                registeredBeacons = new JSONArray(BeaconUtil.getRegisteredBeacons());
            } catch (JSONException e) {
                Log.i("Exception","JSONException-Beacons.java-initializeRegisteredBeacons");
            }
        }
    }

    private String getFromRegisteredList(int position, String data){
        try {
            return registeredBeacons.getJSONObject(position).getString(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setInStatus(){
       setInsideRegion(true);   // Is inside region
       setRegionStatus("IN");   // IN and OUT possible values
       setCorrelationId(UUID.randomUUID().toString());  // correlationId for inside event
       setInTime(System.currentTimeMillis());           // Record the time when entered the zone
    }

    public void setOutStatus(){
        setInsideRegion(false);   // Is inside region
        setRegionStatus("OUT");   // IN and OUT possible values
        setCorrelationId(getCorrelationId());  // correlationId for inside event
        setOutTime(System.currentTimeMillis());           // Record the time when entered the zone
        setVisitTime(getOutTime()-getInTime());
    }
}
