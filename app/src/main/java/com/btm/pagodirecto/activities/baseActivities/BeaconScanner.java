package com.btm.pagodirecto.activities.baseActivities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.btm.pagodirecto.util.Util;
import com.btm.pagodirecto.domain.beacons.BeaconHandler;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by Edwin Alvarado on 04/06/17.
 *
 * Esta es la actividad base: Toda actividad que quiera escanear beacons debe extender a esta actividiad
 * Aqui si implementa la configuración de escaneo de beacons ya que cada vez que una actividad pasa por destroy
 * el servicio de beacons se desactiva, por lo cual es necesario reactivar el scan de beacons cada vez que se incia
 * una actividad.
 *
 * IMPORTANTE: El scan de beacon esta basado en la librería ALTBEACON)
 */

@EBean
public class BeaconScanner extends BaseActivity implements BeaconConsumer {

    // Manejador de beacons
    @Bean
    public BeaconHandler beaconHandler;

    //tags
    private final String URI_BEACON_LAYOUT = "s:0-1=fed8,m:2-2=00,p:3-3:-41,i:4-21v";
    private final String URI_BEACON_LAYOUT_EDDYSTONE = "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19";
    private final String URI_BEACON_LAYOUT_EDDISTONE_TLM = "x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15";

    // Si la app esta activa o en background
    public boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.pd = new ProgressDialog(this);
        Util.setActivity(this);     // Nos permite tener registro de la actividad actual
        configBeaconsDetection(0);  // Protocolos, tiempo de scan, etc
        setAutoBatterySafe(true);   // Ahorra hasta 60% batería de acuerdo a ALTBEACON
    }

    protected void onResume(){
        super.onResume();
        visible = true; // en onResume la actividad esta abierta
    }

    @Override
    protected void onPause(){
        super.onPause();
        visible = false; // en onPause la actividad no es visible
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("estatus","DESTROY");
        beaconHandler.beaconManager.unbind(this);
    }

    // Antes de activar el servicio de beacons pasa por onServiceConnect
    // Se configura la region de scan y se indica el RangeNotifier
    @Override
    public void onBeaconServiceConnect() {
        Log.e("onBeaconServiceConnect",":: getBeaconRegion ::");
        beaconHandler.beaconManager.addRangeNotifier(beaconHandler.rangeNotifier); // Aqui es donde ira el programa cuando beacons se detecten
        beaconHandler.beaconManager.setRegionStatePeristenceEnabled(true);

        // Generic region that accept all nearby listPromotions
        // Start scanning for listPromotions
        try {
            beaconHandler.beaconManager.startRangingBeaconsInRegion(beaconHandler.region); // Se activa la deteccion de beacons
        } catch (RemoteException e) {
            Log.i("RemoteException","startRangingBeacons - onBeaconServiceConnect - LoginActivityManager");
        }
    }

    // Se remueve la ocnfiguracion de la region de scan y se indica el stop del RangeNotifier
    public void onBeaconServiceDisconnect() {
        Log.e("BeaconServiceDisconnect",":: getBeaconRegion ::");
        beaconHandler.beaconManager.removeAllRangeNotifiers();
        beaconHandler.beaconManager.removeAllMonitorNotifiers();
        beaconHandler.beaconManager.setRegionStatePeristenceEnabled(false);

        // Generic region that accept all nearby listPromotions
        // Start scanning for listPromotions
        try {
            beaconHandler.beaconManager.stopMonitoringBeaconsInRegion(beaconHandler.region); // Se desactiva la deteccion de beacons
        } catch (RemoteException e) {
            Log.i("RemoteException","stopRangingBeacons - onBeaconServiceDiconnect - LoginActivityManager");
        }
    }
    // Configuration for AltBeacon library
    private void configBeaconsDetection(int protocolId){

        beaconHandler.beaconManager = BeaconManager.getInstanceForApplication(this);
        // Receive basic Eddystone and Telemetry data
        beaconHandler.beaconManager.getBeaconParsers().clear();

        //------------------ Setting protocols ----------------------------------------------------

        //Eddystone UID protocol
        beaconHandler.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));

        //Eddystone TLM protocol
        beaconHandler.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));

        /*
        ------------ Limit protocols to get only the TLM and beacons data through eddystone----------
        //Eddystone AltBeacon protocol
        beaconHandler.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

        //Eddystone URL protocol
        beaconHandler.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));

        //Eddystone URI protocol
        beaconHandler.beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
        */

        // Enable background detection
        beaconHandler.beaconManager.setBackgroundMode(true); // Important to continue scanning on background, also the scan periods below
        beaconHandler.beaconManager.setBackgroundBetweenScanPeriod(500l);// 500ms
        beaconHandler.beaconManager.setBackgroundScanPeriod(500l);
        beaconHandler.beaconManager.setAndroidLScanningDisabled(true);
        beaconHandler.beaconManager.setDebug(true);

        //Enable foreground detection
        beaconHandler.beaconManager.setForegroundBetweenScanPeriod(5000l);
        beaconHandler.beaconManager.setForegroundScanPeriod(1100l);

        beaconHandler.beaconManager.bind(this); // Aplicar toda la configuración
    }

    private void setAutoBatterySafe(boolean activateBatterySafe) {
        if (activateBatterySafe)
            beaconHandler.backgroundPowerSaver = new BackgroundPowerSaver(this);

    }

    public boolean isVisible() {
        return visible;
    }
}
