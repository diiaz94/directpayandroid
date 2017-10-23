package com.btm.pagodirecto.domain.beacons;

import com.btm.pagodirecto.callbacks.altbeacon.BeaconsRangeNotifier;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by ealvarado on 05/06/17.
 */

// Singleton que maneja los beacons, solo es necesario un beaconManager para no tener que crear la instancia
// cada vez que se inicia una actividad

@EBean(scope = EBean.Scope.Singleton)
public class BeaconHandler {

    // Donde ocurre la logica de manejo de beacons (callbacks/altbeacon/rangenotifier)
    @Bean
    public BeaconsRangeNotifier rangeNotifier;

    // Instancia de configuracion de beacons
    public BeaconManager beaconManager;

    // Manejo de ahorro de bateria
    public BackgroundPowerSaver backgroundPowerSaver; //

    // Region de detección (null,null,null permite recibir todos los beacons -
    // siempre que cumplan con el protocolo configurado: ibeacon,eddystone etc)
    public final Region region = new Region("my-beacon-region", null, null, null);
}
